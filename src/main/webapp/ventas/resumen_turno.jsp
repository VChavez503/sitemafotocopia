<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Turno" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Venta" %>

<%
    Turno turno = (Turno) request.getAttribute("turno");
    Double totalGlobal = (Double) request.getAttribute("totalGlobal");
    if (totalGlobal == null) totalGlobal = 0.0;

    List<Venta> listaTurno = (List<Venta>) request.getAttribute("listaTurno");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Resumen por turno</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body class="bg-light">

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">

    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Resumen general del turno</h4>
        </div>

        <div class="card-body">

            <% if (turno == null) { %>
                <div class="alert alert-warning">
                    No se pudo identificar el turno actual. Verifique la tabla <strong>turnos</strong>.
                </div>
            <% } else { %>

                <!-- Información del turno -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <div class="border rounded p-3 bg-white">
                            <h5 class="mb-3">Turno actual</h5>
                            <p><strong>Nombre:</strong> <%= turno.getNombre() %></p>
                            <p><strong>Horario:</strong> <%= turno.getHoraInicio() %> - <%= turno.getHoraFin() %></p>
                        </div>
                    </div>

                    <!-- Total Global -->
                    <div class="col-md-6">
                        <div class="border rounded p-3 bg-white text-center">
                            <h5>Total vendido en este turno (TODOS)</h5>
                            <p class="display-5 fw-bold text-success">
                                $<%= String.format(java.util.Locale.US, "%.2f", totalGlobal) %>
                            </p>
                        </div>
                    </div>
                </div>

                <!-- Tabla de ventas del turno -->
                <h5>Ventas registradas en este turno</h5>
                <table class="table table-bordered table-striped mt-3">
                    <thead class="table-primary">
                        <tr>
                            <th>ID</th>
                            <th>Usuario</th>
                            <th>Tipo venta</th>
                            <th>Fecha</th>
                            <th>Total ($)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (listaTurno != null) {
                            for (Venta v : listaTurno) { %>
                                <tr>
                                    <td><%= v.getId() %></td>
                                    <td><%= v.getUsuario().getNombre() %></td>
                                    <td><%= v.getTipoVenta() %></td>
                                    <td><%= v.getFechaHora() %></td>
                                    <td>$<%= String.format(java.util.Locale.US, "%.2f", v.getTotal()) %></td>
                                </tr>
                        <%  }
                        } else { %>
                            <tr>
                                <td colspan="5" class="text-center">No hay ventas en este turno.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>

                <a href="${pageContext.request.contextPath}/home.jsp" class="btn btn-secondary mt-3">Volver</a>

            <% } %>

        </div>
    </div>
</div>

</body>
</html>
