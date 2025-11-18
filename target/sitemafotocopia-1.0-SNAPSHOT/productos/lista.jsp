<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Producto"%>
<!DOCTYPE html>
<html>
<head>
    <title>Productos / Manuales</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0">Productos / Manuales</h4>
        </div>
        <div class="card-body">
            <a href="productos?accion=nuevo" class="btn btn-primary mb-3">Nuevo producto</a>

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
                    if (lista != null) {
                        for(Producto pr : lista){
                %>
                <tr>
                    <td><%= pr.getId() %></td>
                    <td><%= pr.getNombre() %></td>
                    <td><%= pr.getDescripcion() %></td>
                    <td><%= pr.getPrecioUnitario() %></td>
                    <td>
                        <span class="badge bg-<%= pr.isActivo() ? "success" : "secondary" %>">
                            <%= pr.isActivo() ? "Sí" : "No" %>
                        </span>
                    </td>
                    <td>
                        <a href="productos?accion=editar&id=<%= pr.getId() %>"
                           class="btn btn-warning btn-sm">Editar</a>
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
