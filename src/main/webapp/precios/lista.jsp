<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Precio"%>
<%@ page import="model.Usuario"%>
<!DOCTYPE html>
<html>
<head>
    <title>Precios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    String rol = (u != null && u.getRol() != null && u.getRol().getNombre() != null)
                 ? u.getRol().getNombre()
                 : "";
%>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0">Precios de copias</h4>
        </div>
        <div class="card-body">

            <%-- SOLO ADMINISTRADOR PUEDE CREAR NUEVO PRECIO --%>
            <% if ("ADMINISTRADOR".equalsIgnoreCase(rol)) { %>
                <a href="precios?accion=nuevo" class="btn btn-primary mb-3">Nuevo precio</a>
            <% } %>

            <table class="table table-bordered table-striped align-middle">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tipo / Descripción</th>
                    <th>Precio ($)</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Precio> lista = (List<Precio>) request.getAttribute("listaPrecios");
                    if (lista != null && !lista.isEmpty()) {
                        for (Precio p : lista) {
                %>
                <tr>
                    <td><%= p.getId() %></td>
                    <td>
                        <strong><%= p.getTipo() %></strong><br>
                        <small class="text-muted"><%= p.getDescripcion() != null ? p.getDescripcion() : "" %></small>
                    </td>
                    <td>$<%= String.format(java.util.Locale.US, "%.2f", p.getPrecio()) %></td>
                    <td>
                        <%-- ADMIN: EDITAR + ELIMINAR --%>
                        <% if ("ADMINISTRADOR".equalsIgnoreCase(rol)) { %>
                            <a href="precios?accion=editar&id=<%= p.getId() %>"
                               class="btn btn-warning btn-sm">Editar</a>

                            <a href="precios?accion=eliminar&id=<%= p.getId() %>"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Seguro que deseas eliminar este precio?');">
                                Eliminar
                            </a>

                        <%-- OPERADOR: SOLO EDITAR --%>
                        <% } else if ("OPERADOR".equalsIgnoreCase(rol)) { %>
                            <a href="precios?accion=editar&id=<%= p.getId() %>"
                               class="btn btn-warning btn-sm">Editar</a>

                        <%-- CLIENTE u OTRO: SIN ACCIONES --%>
                        <% } else { %>
                            <span class="text-muted">Sin permisos</span>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4" class="text-center">No hay precios registrados.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
