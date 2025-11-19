package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PrecioDAO;
import dao.ProductoDAO;
import dao.TurnoDAO;
import dao.VentaDAO;
import model.DetalleVenta;
import model.Precio;
import model.Producto;
import model.Turno;
import model.Usuario;
import model.Venta;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VentaServlet", urlPatterns = {"/ventas"})
public class VentaServlet extends HttpServlet {

    private PrecioDAO precioDAO;
    private ProductoDAO productoDAO;
    private TurnoDAO turnoDAO;
    private VentaDAO ventaDAO;

    @Override
    public void init() {
        precioDAO = new PrecioDAO();
        productoDAO = new ProductoDAO();
        turnoDAO = new TurnoDAO();
        ventaDAO = new VentaDAO();
    }

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
        if (accion == null) accion = "nuevo";

        switch (accion) {

            case "resumenTurno": {
                // Solo ADMIN puede ver el resumen general
                String rol = (u.getRol() != null && u.getRol().getNombre() != null)
                        ? u.getRol().getNombre()
                        : "";

                if (!"ADMINISTRADOR".equalsIgnoreCase(rol)) {
                    response.sendRedirect("home.jsp");
                    return;
                }

                Turno turno = turnoDAO.obtenerTurnoActual();
                if (turno != null) {
                    double totalGlobal = ventaDAO.totalGlobalPorTurnoHoy(turno.getId());
                    List<Venta> listaTurno = ventaDAO.listarPorTurnoHoy(turno.getId());

                    request.setAttribute("turno", turno);
                    request.setAttribute("totalGlobal", totalGlobal);
                    request.setAttribute("listaTurno", listaTurno);
                } else {
                    request.setAttribute("turno", null);
                }

                request.getRequestDispatcher("ventas/resumen_turno.jsp").forward(request, response);
                break;
            }

            default: // "nuevo"
                request.setAttribute("listaPrecios", precioDAO.listar());
                request.setAttribute("listaProductos", productoDAO.listar());
                request.getRequestDispatcher("ventas/registrar.jsp").forward(request, response);
        }
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    Usuario u = getUsuarioSesion(request, response);
    if (u == null) return;

    String tipoVenta = request.getParameter("tipoVenta"); // COPIA o PRODUCTO

    Venta v = new Venta();
    v.setUsuario(u);
    v.setTipoVenta(tipoVenta);

    Turno turno = turnoDAO.obtenerTurnoActual();
    v.setTurno(turno);

    DetalleVenta det = new DetalleVenta();

    try {

        // ===================== VENTA DE COPIA =====================
        if ("COPIA".equalsIgnoreCase(tipoVenta)) {

            String precioIdStr = request.getParameter("precioId");
            if (precioIdStr == null || precioIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Debe seleccionar un precio de copia.");
                doGet(request, response);
                return;
            }

            int precioId = Integer.parseInt(precioIdStr);
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));

            Precio p = precioDAO.buscarPorId(precioId);
            if (p == null) {
                request.setAttribute("error", "Precio de copia no válido.");
                doGet(request, response);
                return;
            }

            double precioUnit = p.getPrecio();
            double subtotal = precioUnit * cantidad;

            det.setTipoCopia(p.getTipo());   // Ej: COPIA_BN, COPIA_COLOR, etc.
            det.setCantidad(cantidad);
            det.setPrecioUnitario(precioUnit);
            det.setSubtotal(subtotal);

            v.setTotal(subtotal);
        }

        // ===================== VENTA DE PRODUCTO =====================
        else if ("PRODUCTO".equalsIgnoreCase(tipoVenta)) {

            String prodIdStr = request.getParameter("productoId");
            if (prodIdStr == null || prodIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Debe seleccionar un producto.");
                doGet(request, response);
                return;
            }

            int productoId = Integer.parseInt(prodIdStr);
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));

            Producto prod = productoDAO.buscarPorId(productoId);
            if (prod == null) {
                request.setAttribute("error", "Producto no válido.");
                doGet(request, response);
                return;
            }

            double precioUnit = prod.getPrecioUnitario();
            double subtotal   = precioUnit * cantidad;

            det.setProducto(prod);          // para guardar producto_id en detalle_venta
            det.setCantidad(cantidad);
            det.setPrecioUnitario(precioUnit);
            det.setSubtotal(subtotal);

            v.setTotal(subtotal);
        }

        // Si no es COPIA ni PRODUCTO (algo raro)
        else {
            request.setAttribute("error", "Tipo de venta no válido.");
            doGet(request, response);
            return;
        }

        boolean ok = ventaDAO.registrarVenta(v, det);

        if (!ok) {
            request.setAttribute("error", "No se pudo registrar la venta");
        } else {
            request.setAttribute("mensaje", "Venta registrada correctamente");
        }

    } catch (NumberFormatException e) {
        e.printStackTrace();
        request.setAttribute("error", "Datos numéricos inválidos (cantidad / precio / id).");
    }

    doGet(request, response);
}

}