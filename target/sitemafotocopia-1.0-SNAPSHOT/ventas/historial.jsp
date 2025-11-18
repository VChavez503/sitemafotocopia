<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Venta" %>
<%@ page import="model.Usuario" %>
<%@ page import="model.Turno" %>

<%
    List<Venta> lista = (List<Venta>) request.getAttribute("listaVentas");
    if (lista == null) lista = java.util.Collections.emptyList();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Historial de ventas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white bg-primary">
            <h4 class="mb-0">Historial de ventas</h4>
        </div>

        <div class="card-body">

            <table class="table table-striped table-bordered align-middle">
                <thead class="table-primary">
                    <tr>
                        <th>ID</th>
                        <th>Fecha / hora</th>
                        <th>Usuario</th>
                        <th>Tipo venta</th>
                        <th>Turno</th>
                        <th>Total ($)</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (Venta v : lista) {
                        Usuario usu = v.getUsuario();
                        Turno t = v.getTurno();
                %>
                    <tr>
                        <td><%= v.getId() %></td>
                        <td><%= v.getFechaHora() %></td>
                        <td><%= (usu != null ? usu.getNombre() : "") %></td>
                        <td><%= v.getTipoVenta() %></td>
                        <td><%= (t != null ? t.getNombre() : "-") %></td>
                        <td>$<%= String.format("%.2f", v.getTotal()) %></td>
                        <td>
                            <% if (v.isActivo()) { %>
                                <span class="badge bg-success">Activa</span>
                            <% } else { %>
                                <span class="badge bg-secondary">Anulada</span>
                            <% } %>
                        </td>
                        <td>
    <a class="btn btn-sm btn-warning"
       href="<%= request.getContextPath() %>/historial?accion=editar&id=<%= v.getId() %>">
        Editar
    </a>

    <a class="btn btn-sm btn-danger"
       href="<%= request.getContextPath() %>/historial?accion=eliminar&id=<%= v.getId() %>"
       onclick="return confirm('¿Seguro que quieres eliminar esta venta?');">
        Eliminar
    </a>
</td>

                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>

        </div>
    </div>
</div>

</body>
</html>
