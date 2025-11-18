<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container mt-5">
    <div class="col-md-5 offset-md-3">
        <div class="card shadow">
            <div class="card-header bg-primary text-white text-center">
                <h4>Crear Cuenta</h4>
            </div>
            <div class="card-body">
                <form action="auth" method="post">
                    <input type="hidden" name="accion" value="registro"/>

                    <div class="mb-3">
                        <label>Nombre completo</label>
                        <input type="text" name="nombre" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Correo</label>
                        <input type="email" name="correo" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Usuario</label>
                        <input type="text" name="usuario" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label>Contraseña</label>
                        <input type="password" name="contrasena" class="form-control" required>
                    </div>

                    <button class="btn btn-success w-100">Registrar</button>
                </form>

                <%
                    String err = (String) request.getAttribute("error");
                    if (err != null) {
                %>
                <div class="alert alert-danger mt-3"><%= err %></div>
                <% } %>

                <%
                    String msg = (String) request.getAttribute("mensaje");
                    if (msg != null) {
                %>
                <div class="alert alert-success mt-3"><%= msg %></div>
                <% } %>

                <div class="mt-3 text-center">
                    <a href="auth?accion=login">Ya tengo cuenta, iniciar sesión</a>
                </div>

            </div>
        </div>
    </div>
</div>

</body>
</html>
