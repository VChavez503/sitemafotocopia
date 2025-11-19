<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Producto"%>
<%@ page import="model.Usuario"%>
<!DOCTYPE html>
<html>
<head>
    <title>Productos / Manuales</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    // Sacar usuario y rol de sesión
    Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
    String rol = (user != null && user.getRol() != null && user.getRol().getNombre() != null)
                    ? user.getRol().getNombre().toUpperCase()
                    : "";
%>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white bg-primary">
            <h4 class="mb-0">Productos</h4>
        </div>
        <div class="card-body">

            <!-- SOLO ADMIN y OPERADOR pueden crear productos -->
            <% if ("ADMINISTRADOR".equals(rol) || "OPERADOR".equals(rol)) { %>
                <a href="productos?accion=nuevo" class="btn btn-primary mb-3">Nuevo producto</a>
            <% } %>

            <table class="table table-bordered table-striped align-middle">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Precio ($)</th>
                        <th>Activo</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Producto> lista = (List<Producto>) request.getAttribute("listaProductos");
                    if (lista != null && !lista.isEmpty()) {
                        for (Producto pr : lista) {
                %>
                    <tr>
                        <td><%= pr.getId() %></td>
                        <td><%= pr.getNombre() %></td>
                        <td><%= pr.getDescripcion() %></td>
                        <td>$<%= String.format(java.util.Locale.US, "%.2f", pr.getPrecioUnitario()) %></td>
                        <td>
                            <span class="badge bg-<%= pr.isActivo() ? "success" : "secondary" %>">
                                <%= pr.isActivo() ? "Sí" : "No" %>
                            </span>
                        </td>
                        <td>
                            <%-- ADMIN: EDITAR + ELIMINAR --%>
                            <% if ("ADMINISTRADOR".equals(rol)) { %>

                                <a href="productos?accion=editar&id=<%= pr.getId() %>"
                                   class="btn btn-warning btn-sm">
                                    Editar
                                </a>

                                <a href="productos?accion=eliminar&id=<%= pr.getId() %>"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('¿Seguro que deseas eliminar este producto?');">
                                    Eliminar
                                </a>

                            <%-- OPERADOR: SOLO EDITAR --%>
                            <% } else if ("OPERADOR".equals(rol)) { %>

                                <a href="productos?accion=editar&id=<%= pr.getId() %>"
                                   class="btn btn-warning btn-sm">
                                    Editar
                                </a>

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
                        <td colspan="6" class="text-center">No hay productos registrados.</td>
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
