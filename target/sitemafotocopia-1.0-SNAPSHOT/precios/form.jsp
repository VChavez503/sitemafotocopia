<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Precio" %>
<!DOCTYPE html>
<html>
<head>
    <title>Precio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    Precio p = (Precio) request.getAttribute("precio");
    boolean editar = (p != null);
%>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header bg-primary text-white">
            <h4 class="mb-0"><%= editar ? "Editar precio" : "Nuevo precio" %></h4>
        </div>
        <div class="card-body">

            <form action="precios" method="post" class="row g-3">
                <% if (editar) { %>
                    <input type="hidden" name="id" value="<%= p.getId() %>">
                <% } %>

                <div class="col-md-4">
                    <label class="form-label">Tipo (clave)</label>
                    <input type="text" name="tipo" class="form-control"
                           value="<%= editar ? p.getTipo() : "" %>" required>
                    <small class="text-muted">
                        Ej: COPIA_BN, COPIA_COLOR, MANUAL, etc.
                    </small>
                </div>

                <div class="col-md-4">
                    <label class="form-label">Descripción</label>
                    <input type="text" name="descripcion" class="form-control"
                           value="<%= editar ? p.getDescripcion() : "" %>">
                </div>

                <div class="col-md-4">
                    <label class="form-label">Precio ($)</label>
                    <input type="number" step="0.01" min="0" name="precio" class="form-control"
                           value="<%= editar ? p.getPrecio() : "" %>" required>
                </div>

                <div class="col-md-4 d-flex align-items-center">
                    <div class="form-check mt-4">
                        <input class="form-check-input" type="checkbox" name="activo"
                               <%= (!editar || p.isActivo()) ? "checked" : "" %>>
                        <label class="form-check-label">Activo</label>
                    </div>
                </div>

                <div class="col-12">
                    <button class="btn btn-success">Guardar</button>
                    <a href="precios?accion=listar" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>

        </div>
    </div>
</div>

</body>
</html>
