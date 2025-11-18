<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="model.Usuario"%>
<%@ page import="model.Rol"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <title>Usuario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    Usuario u = (Usuario) request.getAttribute("usuario");
    boolean editar = u != null;
%>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0"><%= editar ? "Editar Usuario" : "Nuevo Usuario" %></h4>
        </div>
        <div class="card-body">

            <form action="usuarios" method="post" class="row g-3">

                <% if(editar){ %>
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                <% } %>

                <div class="col-md-6">
                    <label>Nombre</label>
                    <input type="text" name="nombre" value="<%= editar ? u.getNombre() : "" %>" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Correo</label>
                    <input type="email" name="correo" value="<%= editar ? u.getCorreo() : "" %>" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Usuario</label>
                    <input type="text" name="usuario" value="<%= editar ? u.getUsuario() : "" %>" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Contraseña</label>
                    <input type="text" name="contrasena" value="<%= editar ? u.getContrasena() : "" %>" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Rol</label>
                    <select name="rolId" class="form-select">
                        <%
                            List<Rol> roles = (List<Rol>) request.getAttribute("listaRoles");
                            if (roles != null) {
                                for(Rol r : roles){
                                    boolean sel = editar && u.getRol() != null && r.getId() == u.getRol().getId();
                        %>
                        <option value="<%= r.getId() %>" <%= sel?"selected":"" %>><%= r.getNombre() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <% if(editar){ %>
                <div class="col-md-6 d-flex align-items-center">
                    <div class="form-check mt-3">
                        <input class="form-check-input" type="checkbox" name="activo" <%= u.isActivo()?"checked":"" %>>
                        <label class="form-check-label">Activo</label>
                    </div>
                </div>
                <% } %>

                <div class="col-12">
                    <button class="btn btn-success">Guardar</button>
                    <a href="usuarios?accion=listar" class="btn btn-secondary">Regresar</a>
                </div>
            </form>

        </div>
    </div>
</div>

</body>
</html>
