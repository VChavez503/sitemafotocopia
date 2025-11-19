package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UsuarioDAO;
import dao.VentaDAO;
import model.Usuario;
import model.Venta;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReporteServlet", urlPatterns = {"/reportes"})
public class ReporteServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private VentaDAO ventaDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        ventaDAO   = new VentaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        if (tipo == null) tipo = "usuario";

        switch (tipo) {

            // ======================================
            //         REPORTE POR USUARIO
            // ======================================
            case "usuario": {

                // Cargar TODOS los usuarios
                List<Usuario> usuarios = usuarioDAO.listar();

                // Filtrar: NO mostrar CLIENTE
                List<Usuario> filtrados = new ArrayList<>();
                if (usuarios != null) {
                    for (Usuario u : usuarios) {
                        if (u.getRol() != null &&
                            u.getRol().getNombre() != null &&
                            !u.getRol().getNombre().equalsIgnoreCase("CLIENTE")) {

                            filtrados.add(u);
                        }
                    }
                }

                request.setAttribute("listaUsuarios", filtrados);

                // Si ya seleccionaron un usuario
                String usuarioIdStr = request.getParameter("usuarioId");

                if (usuarioIdStr != null && !usuarioIdStr.isEmpty()) {

                    int usuarioId = Integer.parseInt(usuarioIdStr);

                    // Rango de fechas opcional
                    String desdeStr = request.getParameter("desde");
                    String hastaStr = request.getParameter("hasta");

                    Date desde = null;
                    Date hasta = null;

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        if (desdeStr != null && !desdeStr.isEmpty()) {
                            desde = sdf.parse(desdeStr);
                        }
                        if (hastaStr != null && !hastaStr.isEmpty()) {
                            Date h = sdf.parse(hastaStr);
                            // sumar 1 día para incluir TODO el día "hasta"
                            h.setTime(h.getTime() + (24L * 60L * 60L * 1000L));
                            hasta = h;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Obtener ventas del usuario (todas)
                    List<Venta> ventas = ventaDAO.listarPorUsuario(usuarioId);

                    // Filtrar por rango de fechas SI ambos existen
                    if (desde != null && hasta != null && ventas != null) {
                        List<Venta> filtradas = new ArrayList<>();
                        for (Venta v : ventas) {
                            Date f = v.getFechaHora();
                            if (f != null &&
                                !f.before(desde) &&   // f >= desde
                                !f.after(hasta)) {    // f <= hasta
                                filtradas.add(v);
                            }
                        }
                        ventas = filtradas;
                    }

                    request.setAttribute("listaVentas", ventas);
                }

                request.getRequestDispatcher("reportes/por_usuario.jsp").forward(request, response);
                break;
            }

            // ======================================
            //         REPORTE POR FECHA
            // ======================================
            case "fecha": {

                String desdeStr = request.getParameter("desde");
                String hastaStr = request.getParameter("hasta");

                List<Venta> ventas = null;

                if (desdeStr != null && !desdeStr.isEmpty() &&
                    hastaStr != null && !hastaStr.isEmpty()) {

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date desde = sdf.parse(desdeStr);
                        Date h = sdf.parse(hastaStr);
                        // incluir día completo "hasta"
                        h.setTime(h.getTime() + (24L * 60L * 60L * 1000L));

                        ventas = ventaDAO.listarPorFecha(desde, h);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                request.setAttribute("listaVentas", ventas);
                request.getRequestDispatcher("reportes/por_fecha.jsp").forward(request, response);
                break;
            }

            // ======================================
            //         REPORTE POR TURNO (AM/PM)
            // ======================================
            case "turno": {

                String turnoSel = request.getParameter("turno");   // "AM" o "PM"
                String fechaStr = request.getParameter("fecha");   // yyyy-MM-dd

                List<Venta> ventas = null;

                if (turnoSel != null && !turnoSel.isEmpty() &&
                    fechaStr != null && !fechaStr.isEmpty()) {

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date dia = sdf.parse(fechaStr);

                        Calendar calDesde = Calendar.getInstance();
                        Calendar calHasta = Calendar.getInstance();
                        calDesde.setTime(dia);
                        calHasta.setTime(dia);

                        if ("AM".equalsIgnoreCase(turnoSel)) {
                            // 00:00:00 a 11:59:59
                            calDesde.set(Calendar.HOUR_OF_DAY, 0);
                            calDesde.set(Calendar.MINUTE, 0);
                            calDesde.set(Calendar.SECOND, 0);
                            calDesde.set(Calendar.MILLISECOND, 0);

                            calHasta.set(Calendar.HOUR_OF_DAY, 11);
                            calHasta.set(Calendar.MINUTE, 59);
                            calHasta.set(Calendar.SECOND, 59);
                            calHasta.set(Calendar.MILLISECOND, 999);

                        } else { // "PM"
                            // 12:00:00 a 23:59:59
                            calDesde.set(Calendar.HOUR_OF_DAY, 12);
                            calDesde.set(Calendar.MINUTE, 0);
                            calDesde.set(Calendar.SECOND, 0);
                            calDesde.set(Calendar.MILLISECOND, 0);

                            calHasta.set(Calendar.HOUR_OF_DAY, 23);
                            calHasta.set(Calendar.MINUTE, 59);
                            calHasta.set(Calendar.SECOND, 59);
                            calHasta.set(Calendar.MILLISECOND, 999);
                        }

                        Date desde = calDesde.getTime();
                        Date hasta = calHasta.getTime();

                        // Reutilizamos listarPorFecha
                        ventas = ventaDAO.listarPorFecha(desde, hasta);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                request.setAttribute("listaVentas", ventas);
                request.getRequestDispatcher("reportes/por_turno.jsp").forward(request, response);
                break;
            }

            // ======================================
            // DEFAULT → HOME
            // ======================================
            default:
                response.sendRedirect("home.jsp");
                break;
        }
    }
}
