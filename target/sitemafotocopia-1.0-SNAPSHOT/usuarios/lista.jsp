<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Usuario"%>
<!DOCTYPE html>
<html>
<head>
    <title>Usuarios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0">Usuarios</h4>
        </div>
        <div class="card-body">
            <a href="usuarios?accion=nuevo" class="btn btn-primary mb-3">Nuevo Usuario</a>

            <table class="table table-bordered table-striped align-middle">
                <thead>
                <tr>
                    <th>ID</th><th>Nombre</th><th>Correo</th><th>Usuario</th><th>Rol</th><th>Activo</th><th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
                    if (lista != null) {
                        for(Usuario u : lista){
                %>
                <tr>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNombre() %></td>
                    <td><%= u.getCorreo() %></td>
                    <td><%= u.getUsuario() %></td>
                    <td><%= u.getRol() != null ? u.getRol().getNombre() : "" %></td>
                    <td><span class="badge bg-<%= u.isActivo() ? "success" : "secondary" %>">
                        <%= u.isActivo() ? "Sí" : "No" %>
                    </span></td>
                    <td>
                        <a href="usuarios?accion=editar&id=<%=u.getId()%>" class="btn btn-warning btn-sm">Editar</a>
                        <a href="usuarios?accion=desactivar&id=<%=u.getId()%>" class="btn btn-danger btn-sm"
                           onclick="return confirm('¿Desactivar este usuario?');">Desactivar</a>
                    </td>
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
