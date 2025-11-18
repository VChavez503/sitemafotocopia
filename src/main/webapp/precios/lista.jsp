<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Precio"%>
<!DOCTYPE html>
<html>
<head>
    <title>Precios</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">
    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0">Precios de copias y manuales</h4>
        </div>
        <div class="card-body">
            <a href="precios?accion=nuevo" class="btn btn-primary mb-3">Nuevo precio</a>

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
                    if (lista != null) {
                        for(Precio p : lista){
                %>
                <tr>
                    <td><%= p.getId() %></td>
                    <td><%= p.getTipo() %></td>
                    <td><%= p.getPrecio() %></td>
                    <td>
                        <a href="precios?accion=editar&id=<%= p.getId() %>"
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
