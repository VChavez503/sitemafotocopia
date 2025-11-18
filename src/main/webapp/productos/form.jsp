<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="model.Producto"%>
<!DOCTYPE html>
<html>
<head>
    <title>Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    Producto pr = (Producto) request.getAttribute("producto");
    boolean editar = pr != null;
    String error = (String) request.getAttribute("error");
%>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0"><%= editar ? "Editar producto" : "Nuevo producto" %></h4>
        </div>
        <div class="card-body">

            <% if (error != null) { %>
                <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <form action="productos" method="post" class="row g-3">

                <% if (editar) { %>
                <input type="hidden" name="id" value="<%= pr.getId() %>">
                <% } %>

                <div class="col-md-6">
                    <label class="form-label">Nombre</label>
                    <input type="text" name="nombre" class="form-control"
                           value="<%= editar ? pr.getNombre() : "" %>" required>
                </div>

                <div class="col-md-6">
                    <label class="form-label">Descripción</label>
                    <input type="text" name="descripcion" class="form-control"
                           value="<%= editar && pr.getDescripcion() != null ? pr.getDescripcion() : "" %>">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Precio unitario ($)</label>
                    <!-- 🔴 name CAMBIADO a "precio" para que coincida con el servlet -->
                    <input type="number" step="0.01" min="0" name="precio" class="form-control"
                           value="<%= editar ? pr.getPrecioUnitario() : "" %>" required>
                </div>

                <% if (editar) { %>
                <div class="col-md-4 d-flex align-items-center">
                    <div class="form-check mt-4">
                        <input class="form-check-input" type="checkbox" name="activo"
                               <%= pr.isActivo() ? "checked" : "" %>>
                        <label class="form-check-label">Activo</label>
                    </div>
                </div>
                <% } %>

                <div class="col-12">
                    <button class="btn btn-success">Guardar</button>
                    <a href="productos?accion=listar" class="btn btn-secondary">Cancelar</a>
                </div>

            </form>

        </div>
    </div>
</div>

</body>
</html>
