package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.RolDAO;
import dao.UsuarioDAO;
import model.Rol;
import model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsuarioServlet", urlPatterns = {"/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO;

    // 👉 Rol por defecto cuando NO viene del form (CLIENTE)
    private static final int ROL_CLIENTE = 3;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();
    }

    // 👉 Obtener usuario en sesión (o redirigir a login)
    private Usuario getUsuarioSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession(false);
        Usuario u = (sesion != null) ? (Usuario) sesion.getAttribute("usuarioLogueado") : null;

        if (u == null) {
            response.sendRedirect("auth?accion=login");
        }
        return u;
    }

    // 👉 Verificar que sea ADMINISTRADOR
    private boolean noEsAdmin(Usuario u, HttpServletResponse response) throws IOException {
        if (u == null || u.getRol() == null || u.getRol().getNombre() == null) {
            response.sendRedirect("home.jsp");
            return true;
        }

        String rolNombre = u.getRol().getNombre();
        if (!"ADMINISTRADOR".equalsIgnoreCase(rolNombre)) {
            // No tiene permiso para gestionar usuarios
            response.sendRedirect("home.jsp");
            return true;
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario actual = getUsuarioSesion(request, response);
        if (actual == null) return;
        if (noEsAdmin(actual, response)) return;   // 🚫 Bloquea OPERADOR y CLIENTE

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                request.setAttribute("listaRoles", rolDAO.listar());
                request.getRequestDispatcher("usuarios/form.jsp").forward(request, response);
                break;

            case "editar": {
                int id = Integer.parseInt(request.getParameter("id"));
                Usuario u = usuarioDAO.buscarPorId(id);
                request.setAttribute("usuario", u);
                request.setAttribute("listaRoles", rolDAO.listar());
                request.getRequestDispatcher("usuarios/form.jsp").forward(request, response);
                break;
            }

            case "desactivar": {
                int idDel = Integer.parseInt(request.getParameter("id"));
                usuarioDAO.desactivar(idDel);
                response.sendRedirect("usuarios?accion=listar");
                break;
            }

            default:
                List<Usuario> lista = usuarioDAO.listar();
                request.setAttribute("listaUsuarios", lista);
                request.getRequestDispatcher("usuarios/lista.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario actual = getUsuarioSesion(request, response);
        if (actual == null) return;
        if (noEsAdmin(actual, response)) return;   // 🚫 Solo ADMIN puede crear/editar usuarios

        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        // Rol que viene del form, si no viene → CLIENTE
        String rolStr = request.getParameter("rolId");
        int rolId = (rolStr == null || rolStr.isEmpty())
                ? ROL_CLIENTE
                : Integer.parseInt(rolStr);

        boolean activo = request.getParameter("activo") != null;

        Rol rol = new Rol();
        rol.setId(rolId);

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setUsuario(usuario);
        u.setContrasena(contrasena);
        u.setRol(rol);
        u.setActivo(activo);

        if (idStr == null || idStr.isEmpty()) {
            usuarioDAO.registrar(u);
        } else {
            u.setId(Integer.parseInt(idStr));
            usuarioDAO.actualizar(u);
        }

        response.sendRedirect("usuarios?accion=listar");
    }
}
