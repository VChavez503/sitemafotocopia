<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Venta" %>
<%@ page import="model.Usuario" %>

<%
    String desde = (String) request.getAttribute("desde");
    String hasta = (String) request.getAttribute("hasta");

    List<Venta> lista = (List<Venta>) request.getAttribute("listaVentas");
    if (lista == null) {
        lista = java.util.Collections.emptyList();
    }

    double totalPeriodo = 0.0;
    for (Venta v : lista) {
        if (v != null) {
            totalPeriodo += v.getTotal();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Reporte por fecha</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body class="bg-light">

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">

    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Reporte de ventas por fecha</h4>
        </div>

        <div class="card-body">

            <!-- Filtros por fecha -->
            <form class="row g-3 mb-3" method="get" action="reportes">
                <input type="hidden" name="tipo" value="fecha"/>

                <div class="col-md-4">
                    <label class="form-label">Desde</label>
                    <input type="date" name="desde" class="form-control"
                           value="<%= (desde != null ? desde : "") %>" required>
                </div>

                <div class="col-md-4">
                    <label class="form-label">Hasta</label>
                    <input type="date" name="hasta" class="form-control"
                           value="<%= (hasta != null ? hasta : "") %>" required>
                </div>

                <div class="col-md-4 d-flex align-items-end">
                    <button class="btn btn-primary me-2">Filtrar</button>
                    <a href="${pageContext.request.contextPath}/reportes?tipo=fecha"
                       class="btn btn-outline-secondary">Limpiar</a>
                </div>
            </form>

            <!-- Tabla de resultados -->
            <div class="table-responsive">
                <table class="table table-bordered table-striped align-middle">
                    <thead class="table-primary">
                    <tr>
                        <th>ID</th>
                        <th>Fecha y hora</th>
                        <th>Usuario</th>
                        <th>Tipo de venta</th>
                        <th>Total ($)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        if (lista.isEmpty()) {
                    %>
                        <tr>
                            <td colspan="5" class="text-center">No hay ventas en el rango seleccionado.</td>
                        </tr>
                    <%
                        } else {
                            for (Venta v : lista) {
                                if (v == null) continue;
                                String nombreUsuario = "";
                                if (v.getUsuario() != null && v.getUsuario().getNombre() != null) {
                                    nombreUsuario = v.getUsuario().getNombre();
                                }
                    %>
                        <tr>
                            <td><%= v.getId() %></td>
                            <td><%= v.getFechaHora() %></td>
                            <td><%= nombreUsuario %></td>
                            <td><%= v.getTipoVenta() %></td>
                            <td>$<%= String.format(java.util.Locale.US, "%.2f", v.getTotal()) %></td>
                        </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                    <tfoot>
                    <tr class="table-secondary">
                        <th colspan="4" class="text-end">Total del período:</th>
                        <th>$<%= String.format(java.util.Locale.US, "%.2f", totalPeriodo) %></th>
                    </tr>
                    </tfoot>
                </table>
            </div>

            <a href="${pageContext.request.contextPath}/home.jsp"
               class="btn btn-secondary mt-3">Volver</a>

        </div>
    </div>
</div>

</body>
</html>
