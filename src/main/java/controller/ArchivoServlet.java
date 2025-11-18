package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import dao.ArchivoDAO;
import model.Archivo;
import model.Usuario;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet(name = "ArchivoServlet", urlPatterns = {"/archivos"})
@MultipartConfig
public class ArchivoServlet extends HttpServlet {

    private ArchivoDAO archivoDAO;

    @Override
    public void init() {
        archivoDAO = new ArchivoDAO();
    }

    // --------------------------------------------------------------------
    // 🔒 Obtener usuario en sesión
    // --------------------------------------------------------------------
    private Usuario getUsuarioSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession(false);
        Usuario u = (sesion != null) ? (Usuario) sesion.getAttribute("usuarioLogueado") : null;

        if (u == null) {
            System.out.println("[ArchivoServlet] No hay sesión, redirigiendo a login");
            response.sendRedirect("auth?accion=login");
        }
        return u;
    }

    // --------------------------------------------------------------------
    // 🔒 Validar permisos
    // CLIENTE → solo "subir" y "mis"
    // ADMIN y OPERADOR → todo
    // --------------------------------------------------------------------
    private boolean sinPermiso(String accion, Usuario u, HttpServletResponse response) throws IOException {

        if (u == null || u.getRol() == null || u.getRol().getNombre() == null) {
            response.sendRedirect("home.jsp");
            return true;
        }

        String rol = u.getRol().getNombre();
        System.out.println("[ArchivoServlet] Rol = " + rol + ", accion = " + accion);

        if ("CLIENTE".equalsIgnoreCase(rol)) {
            if (!"subir".equalsIgnoreCase(accion) && 
                !"mis".equalsIgnoreCase(accion)) {
                response.sendRedirect("home.jsp");
                return true;
            }
        }

        // ADMINISTRADOR y OPERADOR → OK
        return false;
    }

    // --------------------------------------------------------------------
    // GET
    // --------------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = getUsuarioSesion(request, response);
        if (u == null) return;

        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) accion = "subir";

        if (sinPermiso(accion, u, response)) return;

        switch (accion) {

            case "verPorCodigo": {
                String codigo = request.getParameter("codigo");
                request.setAttribute("listaArchivos", archivoDAO.listarPorCodigo(codigo));
                request.getRequestDispatcher("archivos/lista.jsp").forward(request, response);
                break;
            }

            case "recibidos": {
                // ADMIN y OPERADOR → bandeja completa
                request.setAttribute("listaArchivos", archivoDAO.listarTodos());
                request.getRequestDispatcher("archivos/recibidos.jsp").forward(request, response);
                break;
            }

            case "mis": {
                // Cliente (o cualquier usuario) ve sus propios archivos y estado
                request.setAttribute("listaArchivos", archivoDAO.listarPorUsuario(u.getId()));
                request.getRequestDispatcher("archivos/mis_archivos.jsp").forward(request, response);
                break;
            }

            case "marcarProcesado": {
                // Solo ADMIN / OPERADOR deberían poder hacer esto (cliente bloqueado por sinPermiso)
                String idStr = request.getParameter("id");
                try {
                    int id = Integer.parseInt(idStr);
                    archivoDAO.marcarProcesado(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Regresar a bandeja
                response.sendRedirect("archivos?accion=recibidos");
                break;
            }

            default:
                // Formulario subir
                request.getRequestDispatcher("archivos/subir.jsp").forward(request, response);
        }
    }

    // --------------------------------------------------------------------
    // POST (subir archivo)
    // --------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = getUsuarioSesion(request, response);
        if (u == null) return;

        String codigo = request.getParameter("codigo");
        String mensaje = request.getParameter("mensaje");
        Part filePart = request.getPart("archivo");

        if (codigo == null || codigo.isEmpty() || filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "Debe ingresar un código y seleccionar un archivo.");
            doGet(request, response);
            return;
        }

        if (mensaje == null) mensaje = "";

        String nombreOriginal = filePart.getSubmittedFileName();
        String nombreGuardado = UUID.randomUUID() + "_" + nombreOriginal;

        String rutaBase = getServletContext().getRealPath("/uploads");
        if (rutaBase == null) {
            rutaBase = System.getProperty("user.dir") + File.separator + "uploads";
        }

        File dir = new File(rutaBase);
        if (!dir.exists()) dir.mkdirs();

        File archivoDestino = new File(dir, nombreGuardado);
        Files.copy(filePart.getInputStream(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);

        Archivo a = new Archivo();
        a.setCodigo(codigo);
        a.setMensaje(mensaje);
        a.setNombreArchivo(nombreOriginal);
        a.setRutaArchivo("uploads/" + nombreGuardado);
        a.setProcesado(false);  // aún no está “completado”
        a.setUsuario(u);

        boolean ok = archivoDAO.insertar(a);

        if (ok) {
            request.setAttribute("mensaje", "Archivo enviado correctamente.");
        } else {
            request.setAttribute("error", "No se pudo guardar el archivo.");
        }

        doGet(request, response);
    }
}
