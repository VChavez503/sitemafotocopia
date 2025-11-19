<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Usuario" %>
<%@ page import="model.Rol" %>

<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    String rol = "";
    if (u != null && u.getRol() != null && u.getRol().getNombre() != null) {
        rol = u.getRol().getNombre();  // ADMINISTRADOR / OPERADOR / CLIENTE
    }
%>

<nav class="navbar navbar-expand-lg navbar-fotocopia">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home.jsp">
            FotocopiaSV
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#mainNav">
            <span class="navbar-toggler-icon" style="filter: invert(1);"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                <!-- HOME -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home.jsp">Inicio</a>
                </li>

                <%-- ================= ADMINISTRADOR ================= --%>
                <% if ("ADMINISTRADOR".equalsIgnoreCase(rol)) { %>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/usuarios?accion=listar">
                            Usuarios
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/precios?accion=listar">
                            Precios
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/productos?accion=listar">
                            Productos
                        </a>
                    </li>

                    <!-- VENTAS (FUNCIONANDO) -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                            Ventas
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ventas?accion=nuevo">Registrar venta</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/historial?accion=listar">Historial de ventas</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ventas?accion=resumenTurno">Resumen por turno</a></li>
                        </ul>
                    </li>

                    <!-- REPORTES -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                            Reportes
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reportes?tipo=fecha">Por fecha</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reportes?tipo=usuario">Por usuario</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reportes?tipo=turno">Por turno</a></li>
                        </ul>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/archivos?accion=recibidos">
                            Archivos recibidos
                        </a>
                    </li>

                <% } %>

                <%-- ================= OPERADOR ================= --%>
                <% if ("OPERADOR".equalsIgnoreCase(rol)) { %>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/precios?accion=listar">Precios</a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/productos?accion=listar">Productos</a>
                    </li>
                    

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                            Ventas
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ventas?accion=nuevo">Registrar venta</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/historial?accion=listar">Historial de ventas</a></li>
                        </ul>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/archivos?accion=recibidos">Archivos recibidos</a>
                    </li>

                <% } %>

                <%-- ================= CLIENTE ================= --%>
                <% if ("CLIENTE".equalsIgnoreCase(rol)) { %>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/precios?accion=listar">
                            Ver precios
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/productos?accion=listar">
                            Productos
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/archivos?accion=subir">
                            Enviar archivo
                        </a>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/archivos?accion=mis">
                            Mis archivos
                        </a>
                    </li>

                <% } %>

            </ul>

            <!-- Usuario y logout -->
            <ul class="navbar-nav ms-auto">
                <% if (u != null) { %>
                    <li class="nav-item">
                        <span class="nav-link disabled">
                            <strong><%= u.getUsuario() %></strong> (<%= rol %>)
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/auth?accion=logout">Cerrar sesión</a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/auth?accion=login">Iniciar sesión</a>
                    </li>
                <% } %>
            </ul>

        </div>
    </div>
</nav>

<!-- ESTE SCRIPT ES OBLIGATORIO PARA QUE EL DROPDOWN FUNCIONE -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
