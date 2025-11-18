<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Enviar archivos para impresión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>
<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<div class="container mt-4">

    <div class="card shadow">
        <div class="card-header text-white">
            <h4 class="mb-0">Enviar archivo para impresión</h4>
        </div>
        <div class="card-body">

            <form action="archivos" method="post" enctype="multipart/form-data" class="row g-3">
                <input type="hidden" name="accion" value="subir">

                <div class="col-md-6">
                    <label>Archivo (PDF o Imagen)</label>
                    <input type="file" name="archivo" accept=".pdf,image/*" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Nombre / Descripción</label>
                    <input type="text" name="descripcion" class="form-control" required>
                </div>

                <div class="col-md-6">
                    <label>Código / Identificador</label>
                    <input type="text" name="codigo" class="form-control"
                           placeholder="Ej: código QR, carnet, etc.">
                </div>

                <div class="col-12">
                    <button class="btn btn-success">Enviar archivo</button>
                    <a href="${pageContext.request.contextPath}/home.jsp" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>

            <%
                String msg = (String) request.getAttribute("mensaje");
                String err = (String) request.getAttribute("error");
                if (msg != null) {
            %>
            <div class="alert alert-success mt-3"><%= msg %></div>
            <% } else if (err != null) { %>
            <div class="alert alert-danger mt-3"><%= err %></div>
            <% } %>

        </div>
    </div>

</div>

</body>
</html>
