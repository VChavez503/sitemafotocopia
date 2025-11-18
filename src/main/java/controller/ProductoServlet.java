package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ProductoDAO;
import model.Producto;
import model.Usuario;

import java.io.IOException;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private ProductoDAO productoDAO;

    @Override
    public void init() {
        productoDAO = new ProductoDAO();
    }

    // 👉 Obtener usuario en sesión
    private Usuario getUsuarioSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession(false);
        Usuario u = (sesion != null) ? (Usuario) sesion.getAttribute("usuarioLogueado") : null;

        if (u == null) {
            response.sendRedirect("auth?accion=login");
        }
        return u;
    }

    // 👉 Validar que el rol sea ADMINISTRADOR o OPERADOR
    private boolean sinPermiso(Usuario u, HttpServletResponse response) throws IOException {

        if (u == null || u.getRol() == null || u.getRol().getNombre() == null) {
            response.sendRedirect("home.jsp");
            return true;
        }

        String rol = u.getRol().getNombre();

        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) return false;
        if ("OPERADOR".equalsIgnoreCase(rol)) return false;

        // 👉 Si es CLIENTE → NO entra
        response.sendRedirect("home.jsp");
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = getUsuarioSesion(request, response);
        if (u == null) return;
        if (sinPermiso(u, response)) return;

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                request.getRequestDispatcher("productos/form.jsp").forward(request, response);
                break;

            case "editar":
                int id = Integer.parseInt(request.getParameter("id"));
                Producto p = productoDAO.buscarPorId(id);
                request.setAttribute("producto", p);
                request.getRequestDispatcher("productos/form.jsp").forward(request, response);
                break;

            default:
                request.setAttribute("listaProductos", productoDAO.listar());
                request.getRequestDispatcher("productos/lista.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = getUsuarioSesion(request, response);
        if (u == null) return;
        if (sinPermiso(u, response)) return;

        String idStr       = request.getParameter("id");
        String nombre      = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr   = request.getParameter("precio");
        boolean activo     = request.getParameter("activo") != null;

        // 🔒 Validar que venga precio
        if (precioStr == null || precioStr.trim().isEmpty()) {
            request.setAttribute("error", "Debes ingresar un precio.");

            if (idStr != null && !idStr.isEmpty()) {
                Producto pEdit = productoDAO.buscarPorId(Integer.parseInt(idStr));
                request.setAttribute("producto", pEdit);
            }
            request.getRequestDispatcher("productos/form.jsp").forward(request, response);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El precio no es válido.");

            if (idStr != null && !idStr.isEmpty()) {
                Producto pEdit = productoDAO.buscarPorId(Integer.parseInt(idStr));
                request.setAttribute("producto", pEdit);
            }
            request.getRequestDispatcher("productos/form.jsp").forward(request, response);
            return;
        }

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecioUnitario(precio);
        p.setActivo(activo);

        if (idStr == null || idStr.isEmpty()) {
            // nuevo
            productoDAO.insertar(p);
        } else {
            // editar
            p.setId(Integer.parseInt(idStr));
            productoDAO.actualizar(p);
        }

        response.sendRedirect("productos?accion=listar");
    }
}
