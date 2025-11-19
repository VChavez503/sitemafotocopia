<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Venta"%>
<!DOCTYPE html>
<html>
<head>
    <title>Reporte por turno</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">

    <!-- Filtros -->
    <div class="card shadow mb-3">
        <div class="card-header text-white bg-primary">
            <h4 class="mb-0">Reporte por turno</h4>
        </div>
        <div class="card-body">
            <form action="reportes" method="get" class="row g-3">
                <input type="hidden" name="tipo" value="turno">

                <div class="col-md-4">
                    <label class="form-label">Turno</label>
                    <select name="turno" class="form-select" required>
                        <option value="AM">Mañana (AM)</option>
                        <option value="PM">Tarde (PM)</option>
                    </select>
                </div>

                <div class="col-md-4">
                    <label class="form-label">Fecha</label>
                    <input type="date" name="fecha" class="form-control" required>
                </div>

                <div class="col-md-4 d-flex align-items-end">
                    <button class="btn btn-primary">Buscar</button>
                </div>
            </form>
        </div>
    </div>

    <!-- Resultados -->
    <div class="card shadow">
        <div class="card-header text-white bg-primary">
            <h5 class="mb-0">Resultados</h5>
        </div>
        <div class="card-body">
            <%
                List<Venta> lista = (List<Venta>) request.getAttribute("listaVentas");
                if (lista == null || lista.isEmpty()) {
            %>
                <div class="alert alert-info">
                    No hay ventas para el turno y fecha seleccionados.
                </div>
            <%
                } else {
            %>
            <table class="table table-bordered table-striped align-middle">
                <thead>
                <tr>
                    <th>Fecha / Hora</th>
                    <th>Usuario</th>
                    <th>Tipo de venta</th>
                    <th>Total ($)</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Venta v : lista) {
                %>
                <tr>
                    <td><%= v.getFechaHora() %></td>
                    <!-- Aquí el cambio: mostramos el NOMBRE del usuario -->
                    <td><%= (v.getUsuario() != null && v.getUsuario().getNombre() != null) ? v.getUsuario().getNombre() : "" %></td>
                    <td><%= v.getTipoVenta() %></td>
                    <td>$<%= String.format(java.util.Locale.US, "%.2f", v.getTotal()) %></td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
            <%
                }
            %>
        </div>
    </div>

</div>

</body>
</html>
