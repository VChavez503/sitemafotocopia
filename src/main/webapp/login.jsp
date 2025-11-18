<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Iniciar Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="col-md-4 offset-md-4">
        <div class="card shadow">
            <div class="card-header text-center bg-primary text-white">
                <h4>Inicio de Sesión</h4>
            </div>
            <div class="card-body">
                <form action="auth" method="post">
                    <input type="hidden" name="accion" value="login"/>

                    <div class="mb-3">
                        <label>Usuario</label>
                        <input type="text" name="usuario" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Contraseña</label>
                        <input type="password" name="contrasena" class="form-control" required>
                    </div>

                    <button class="btn btn-primary w-100">Ingresar</button>
                </form>

                <%
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                <div class="alert alert-danger mt-3"><%= error %></div>
                <% } %>

                <div class="mt-3 text-center">
                    <a href="auth?accion=registro">Crear cuenta</a>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
