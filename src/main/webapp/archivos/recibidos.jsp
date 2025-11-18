<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Archivo" %>
<%@ page import="model.Usuario" %>

<%
    List<Archivo> lista = (List<Archivo>) request.getAttribute("listaArchivos");
    if (lista == null) lista = java.util.Collections.emptyList();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Archivos recibidos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>

<body class="bg-light">
    
<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">
    <h3 class="mb-3">📥 Archivos recibidos</h3>

    <table class="table table-bordered table-striped shadow">
        <thead class="table-primary">
            <tr>
                <th>ID</th>
                <th>Código</th>
                <th>Cliente</th>
                <th>Archivo</th>
                <th>Fecha</th>
                <th>Estado</th>
                <th>Acción</th>
            </tr>
        </thead>

        <tbody>
        <% for (Archivo a : lista) { %>
            <tr>
                <td><%= a.getId() %></td>
                <td><%= a.getCodigo() %></td>

                <td>
                    <% if (a.getUsuario() != null) { %>
                        <%= a.getUsuario().getNombre() %>
                    <% } else { %>
                        <i>Sin usuario</i>
                    <% } %>
                </td>

                <td>
                    <a class="btn btn-sm btn-success"
                       href="<%= request.getContextPath() + "/descargar?ruta=" + a.getRutaArchivo() %>">
                        Descargar
                    </a>
                </td>

                <td><%= a.getFechaSubida() %></td>

                <td>
                    <% if (a.isProcesado()) { %>
                        <span class="badge bg-success">Completado</span>
                    <% } else { %>
                        <span class="badge bg-warning text-dark">Pendiente</span>
                    <% } %>
                </td>

                <td>
                    <% if (!a.isProcesado()) { %>
                        <a class="btn btn-sm btn-primary"
                           href="<%= request.getContextPath() + "/archivos?accion=marcarProcesado&id=" + a.getId() %>">
                            Marcar completado
                        </a>
                    <% } else { %>
                        <span class="text-muted">Sin acción</span>
                    <% } %>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>
