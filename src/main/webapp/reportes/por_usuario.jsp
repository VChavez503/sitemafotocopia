<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Usuario"%>
<%@ page import="model.Venta"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reporte por usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">

    <!-- ==================== FILTROS ==================== -->
    <div class="card shadow mb-3">
        <div class="card-header text-white">
            <h4 class="mb-0">Reporte por usuario</h4>
        </div>
        <div class="card-body">

            <form action="reportes" method="get" class="row g-3">
                <input type="hidden" name="tipo" value="usuario">

                <%
                    // recuperar valores enviados (para que no se borren al filtrar)
                    String selectedId   = request.getParameter("usuarioId");
                    String fechaDesde   = request.getParameter("desde");
                    String fechaHasta   = request.getParameter("hasta");
                %>

                <!-- Usuario -->
                <div class="col-md-6">
                    <label class="form-label">Usuario</label>
                    <select name="usuarioId" class="form-select" required>
                        <option value="">-- Seleccione --</option>

                        <%
                            List<Usuario> usuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
                            if (usuarios != null) {
                                for (Usuario u : usuarios) {
                                    String selected = (selectedId != null && selectedId.equals(String.valueOf(u.getId())))
                                            ? "selected" : "";
                        %>
                                <option value="<%= u.getId() %>" <%= selected %>>
                                    <%= u.getUsuario() %> - <%= u.getNombre() %>
                                </option>
                        <%      }
                            }
                        %>
                    </select>
                </div>

                <!-- Desde -->
                <div class="col-md-3">
                    <label class="form-label">Desde</label>
                    <input type="date" name="desde" class="form-control"
                           value="<%= fechaDesde != null ? fechaDesde : "" %>">
                </div>

                <!-- Hasta -->
                <div class="col-md-3">
                    <label class="form-label">Hasta</label>
                    <input type="date" name="hasta" class="form-control"
                           value="<%= fechaHasta != null ? fechaHasta : "" %>">
                </div>

                <div class="col-12">
                    <button class="btn btn-primary">Buscar</button>
                </div>
            </form>

        </div>
    </div>

    <!-- ==================== RESULTADOS ==================== -->
    <div class="card shadow">
        <div class="card-header text-white">
            <h5 class="mb-0">Resultados</h5>
        </div>

        <div class="card-body">

            <table class="table table-bordered table-striped align-middle">
                <thead class="table-primary">
                    <tr>
                        <th>Fecha / Hora</th>
                        <th>Tipo de venta</th>
                        <th>Total ($)</th>
                        <th>Turno</th>
                    </tr>
                </thead>

                <tbody>
                <%
                    List<Venta> lista = (List<Venta>) request.getAttribute("listaVentas");

                    if (lista == null || lista.isEmpty()) {
                %>
                        <tr>
                            <td colspan="4" class="text-center text-muted">
                                No hay ventas para este usuario.
                            </td>
                        </tr>
                <%
                    } else {
                        for (Venta v : lista) {
                %>
                        <tr>
                            <td><%= v.getFechaHora() %></td>
                            <td><%= v.getTipoVenta() %></td>
                            <td>$<%= String.format(java.util.Locale.US, "%.2f", v.getTotal()) %></td>
                            <td><%= v.getTurno() != null ? v.getTurno().getNombre() : "-" %></td>
                        </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>

        </div>
    </div>

</div>

</body>
</html>
