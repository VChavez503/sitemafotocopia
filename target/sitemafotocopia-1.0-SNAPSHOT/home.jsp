<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inicio - Sistema Fotocopia</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">

    <style>
        .card-home {
            border-radius: 12px;
            transition: 0.2s;
        }
        .card-home:hover {
            transform: scale(1.03);
        }
    </style>
</head>
<body>

<jsp:include page="includes/menu.jsp"></jsp:include>

<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    String rol = (u != null && u.getRol() != null) ? u.getRol().getNombre() : "";
%>

<div class="container mt-4">

    <!-- BIENVENIDA -->
    <div class="row mb-4">
        <div class="col">
            <h3 class="mb-0">Bienvenido, <%= u != null ? u.getNombre() : "" %></h3>
            <small class="text-muted">
                Rol: <strong><%= rol %></strong>
            </small>
        </div>
    </div>

    <div class="row g-4">

        <!-- ======== OPCIONES PARA ADMIN ======== -->
        <% if ("ADMINISTRADOR".equalsIgnoreCase(rol)) { %>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Usuarios</h5>
                    <p class="text-muted">Administrar cuentas y roles.</p>
                    <a href="usuarios?accion=listar" class="btn btn-primary btn-sm">Gestionar usuarios</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Ventas</h5>
                    <p class="text-muted">Registrar copias y productos.</p>
                    <a href="ventas?accion=nuevo" class="btn btn-primary btn-sm">Registrar venta</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Productos</h5>
                    <p class="text-muted">Administrar inventario.</p>
                    <a href="productos?accion=listar" class="btn btn-primary btn-sm">Ver productos</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Precios</h5>
                    <p class="text-muted">Configurar precios de copias.</p>
                    <a href="precios?accion=listar" class="btn btn-primary btn-sm">Gestionar precios</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Reportes</h5>
                    <p class="text-muted">Consultas por fecha, usuario y turno.</p>
                    <a href="reportes?tipo=fecha" class="btn btn-primary btn-sm">Ver reportes</a>
                </div>
            </div>

        <% } %>

        <!-- ======== OPCIONES PARA OPERADOR ======== -->
        <% if ("OPERADOR".equalsIgnoreCase(rol)) { %>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Registrar venta</h5>
                    <p class="text-muted">Registrar copias y productos.</p>
                    <a href="ventas?accion=nuevo" class="btn btn-primary btn-sm">Registrar</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Historial</h5>
                    <p class="text-muted">Ver ventas realizadas.</p>
                    <a href="historial" class="btn btn-primary btn-sm">Ver historial</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card p-3 shadow card-home">
                    <h5>Reportes</h5>
                    <p class="text-muted">Reportes de sus ventas.</p>
                    <a href="reportes?tipo=usuario" class="btn btn-primary btn-sm">Ver reportes</a>
                </div>
            </div>

        <% } %>

        <!-- ======== OPCIONES PARA CLIENTE ======== -->
        <% if ("CLIENTE".equalsIgnoreCase(rol)) { %>

            <div class="col-md-12">
                <div class="alert alert-info">
                    Bienvenido al sistema.  
                    <br>No posee accesos administrativos.  
                    <br>Consulte con el administrador.
                </div>
            </div>

        <% } %>

    </div> <!-- row -->

</div>

</body>
</html>
