<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inicio - Sistema Fotocopia</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="includes/menu.jsp"></jsp:include>

<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
%>

<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h3 class="mb-0">Bienvenido, <%= u != null ? u.getNombre() : "" %></h3>
            <small class="text-muted">Rol: <strong><%= u != null && u.getRol() != null ? u.getRol().getNombre() : "" %></strong></small>
        </div>
    </div>

    <div class="row g-3">
        <div class="col-md-4">
            <div class="card p-3">
                <h5>Usuarios</h5>
                <p class="text-muted">Gestión de cuentas, roles y estado de acceso.</p>
                <a href="usuarios?accion=listar" class="btn btn-primary btn-sm">Ver usuarios</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3">
                <h5>Ventas</h5>
                <p class="text-muted">Registro de copias y manuales por turno.</p>
                <a href="ventas?accion=nuevo" class="btn btn-primary btn-sm">Registrar venta</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-3">
                <h5>Reportes</h5>
                <p class="text-muted">Consultas por fecha, usuario y turno.</p>
                <a href="reportes?tipo=fecha" class="btn btn-primary btn-sm">Ver reportes</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
