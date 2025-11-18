package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.VentaDAO;
import model.Usuario;
import model.Venta;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HistorialServlet", urlPatterns = {"/historial"})
public class HistorialServlet extends HttpServlet {

    private VentaDAO ventaDAO;

    @Override
    public void init() {
        ventaDAO = new VentaDAO();
    }

    // Obtener usuario logueado
    private Usuario getUsuarioSesion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession(false);
        Usuario u = (sesion != null) ? (Usuario) sesion.getAttribute("usuarioLogueado") : null;

        if (u == null) {
            response.sendRedirect("auth?accion=login");
        }
        return u;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario u = getUsuarioSesion(request, response);
        if (u == null) return;

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        if ("anular".equalsIgnoreCase(accion)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);
                ventaDAO.anular(id);
            }
            response.sendRedirect("historial?accion=listar");
            return;
        }

        // LISTAR
        List<Venta> lista;

        String rol = (u.getRol() != null && u.getRol().getNombre() != null)
                ? u.getRol().getNombre()
                : "";

        if ("ADMINISTRADOR".equalsIgnoreCase(rol)) {
            // admin ve TODAS las ventas
            lista = ventaDAO.listarTodas();
        } else {
            // operador ve SOLO lo que él ha vendido
            lista = ventaDAO.listarPorUsuario(u.getId());
        }

        request.setAttribute("listaVentas", lista);
        request.getRequestDispatcher("ventas/historial.jsp").forward(request, response);
    }
}
