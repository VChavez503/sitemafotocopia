<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Archivo" %>

<%
    List<Archivo> lista = (List<Archivo>) request.getAttribute("listaArchivos");
    if (lista == null) lista = java.util.Collections.emptyList();
%>

<!DOCTYPE html>
<html>
<head>
<head>
    <title>Archivos recibidos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>

<body class="bg-light">
    
<jsp:include page="../includes/menu.jsp"></jsp:include>


<div class="container mt-4">
    <h3 class="mb-3">📄 Mis archivos enviados</h3>

    <table class="table table-bordered table-striped shadow">
        <thead class="table-primary">
            <tr>
                <th>Código</th>
                <th>Archivo</th>
                <th>Fecha</th>
                <th>Estado</th>
            </tr>
        </thead>

        <tbody>
        <% for (Archivo a : lista) { %>
            <tr>
                <td><%= a.getCodigo() %></td>
                <td><%= a.getNombreArchivo() %></td>
                <td><%= a.getFechaSubida() %></td>
                <td>
                    <% if (a.isProcesado()) { %>
                        <span class="badge bg-success">Listo / Completado</span>
                    <% } else { %>
                        <span class="badge bg-warning text-dark">En proceso</span>
                    <% } %>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>
