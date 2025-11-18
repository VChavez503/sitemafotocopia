package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.VentaDAO;
import model.Venta;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReporteServlet", urlPatterns = {"/reportes"})
public class ReporteServlet extends HttpServlet {

    private VentaDAO ventaDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void init() {
        ventaDAO = new VentaDAO();
    }

    // 👉 Método seguro para obtener parámetros sin NPE
    private String p(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return (v != null) ? v : "";  // nunca regresa null
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // -------------------------
        // 1) Parámetros seguros
        // -------------------------
        String tipo = p(request, "tipo");
        if (tipo.isEmpty()) tipo = "fecha";

        String f1 = p(request, "desde");
        String f2 = p(request, "hasta");

        // Si están vacíos → usar fecha de HOY
        String hoy = sdf.format(new Date());
        if (f1.isEmpty()) f1 = hoy;
        if (f2.isEmpty()) f2 = hoy;

        // -------------------------
        // 2) Convertir fechas
        // -------------------------
        try {
            Date desde = sdf.parse(f1);
            Date hasta = sdf.parse(f2);

            List<Venta> lista = ventaDAO.listarPorFecha(desde, hasta);

            request.setAttribute("listaVentas", lista);
            request.setAttribute("desde", f1);
            request.setAttribute("hasta", f2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // -------------------------
        // 3) Enviar al JSP correcto
        // -------------------------
        switch (tipo) {
            case "usuario":
                request.getRequestDispatcher("reportes/por_usuario.jsp").forward(request, response);
                break;
            case "turno":
                request.getRequestDispatcher("reportes/por_turno.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("reportes/por_fecha.jsp").forward(request, response);
        }
    }
}
