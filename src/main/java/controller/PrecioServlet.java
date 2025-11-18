package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PrecioDAO;
import model.Precio;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PrecioServlet", urlPatterns = {"/precios"})
public class PrecioServlet extends HttpServlet {

    private PrecioDAO precioDAO;

    @Override
    public void init() {
        precioDAO = new PrecioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        switch (accion) {
            case "nuevo":
                request.getRequestDispatcher("precios/form.jsp").forward(request, response);
                break;

            case "editar": {
                int id = Integer.parseInt(request.getParameter("id"));
                Precio p = precioDAO.buscarPorId(id);
                request.setAttribute("precio", p);
                request.getRequestDispatcher("precios/form.jsp").forward(request, response);
                break;
            }

            default: {
                List<Precio> lista = precioDAO.listar();
                request.setAttribute("listaPrecios", lista);
                request.getRequestDispatcher("precios/lista.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr       = request.getParameter("id");
        String tipo        = request.getParameter("tipo");
        String descripcion = request.getParameter("descripcion");
        String precioStr   = request.getParameter("precio");
        boolean activo     = request.getParameter("activo") != null;

        double precio = 0.0;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (Exception e) {
            // si viene vacío o malo, lo dejamos 0
        }

        Precio p = new Precio();
        p.setTipo(tipo);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setActivo(activo);

        if (idStr == null || idStr.isEmpty()) {
            // INSERT
            precioDAO.insertar(p);
        } else {
            // UPDATE
            p.setId(Integer.parseInt(idStr));
            precioDAO.actualizar(p);
        }

        response.sendRedirect("precios?accion=listar");
    }
}
