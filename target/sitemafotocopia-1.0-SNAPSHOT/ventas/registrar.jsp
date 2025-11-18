<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Producto"%>
<%@ page import="model.Precio"%>

<!DOCTYPE html>
<html>
<head>
    <title>Registrar venta</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/styles.css" rel="stylesheet">
</head>

<body>

<jsp:include page="../includes/menu.jsp"></jsp:include>

<%
    List<Producto> productos = (List<Producto>) request.getAttribute("listaProductos");
    List<Precio>   precios   = (List<Precio>)   request.getAttribute("listaPrecios");

    boolean hayPrecios = (precios != null && !precios.isEmpty());

    String error   = (String) request.getAttribute("error");
    String mensaje = (String) request.getAttribute("mensaje");
%>

<div class="container mt-4">
    <div class="card shadow">

        <div class="card-header text-white bg-primary">
            <h4 class="mb-0">Registrar venta</h4>
        </div>

        <div class="card-body">

            <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <% if (mensaje != null) { %>
            <div class="alert alert-success"><%= mensaje %></div>
            <% } %>

            <% if (!hayPrecios) { %>
            <div class="alert alert-warning">
                ⚠ Aún no hay registros en la tabla <strong>precios</strong>.
                Solo podrás registrar ventas de productos hasta que se agreguen precios.
            </div>
            <% } %>

            <form action="ventas" method="post" class="row g-3" id="formVenta">

                <!-- TIPO DE VENTA -->
                <div class="col-md-4">
                    <label class="form-label">Tipo de venta</label>
                    <select name="tipoVenta" id="tipoVenta" class="form-select" required>
                        <% if (hayPrecios) { %>
                            <option value="COPIA">Copias</option>
                        <% } %>
                        <option value="PRODUCTO">Producto</option>
                    </select>
                    <small class="text-muted">
                        Para copias se usará el <strong>precio seleccionado</strong> de la tabla <strong>precios</strong>.
                    </small>
                </div>

                <!-- BLOQUE COPIAS: usa TODOS los registros de la tabla PRECIOS -->
                <div class="col-md-4 <%= hayPrecios ? "" : "d-none" %>" id="bloqueCopias">
                    <label class="form-label">Tipo de copia / precio</label>
                    <select name="precioId" id="precioId" class="form-select">
                        <option value="">-- Seleccione precio --</option>
                        <%
                            if (precios != null) {
                                for (Precio pr : precios) {
                        %>
                            <option value="<%= pr.getId() %>">
                                <%= pr.getTipo() %> ($<%= pr.getPrecio() %>)
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <small class="text-muted">
                        Aquí aparecen <strong>todos los registros</strong> de la tabla <code>precios</code>.
                    </small>
                </div>

                <!-- BLOQUE PRODUCTOS: usa tabla PRODUCTOS -->
                <div class="col-md-4" id="bloqueProductos">
                    <label class="form-label">Producto</label>
                    <select name="productoId" id="productoId" class="form-select">
                        <option value="">-- Seleccione --</option>
                        <%
                            if (productos != null) {
                                for (Producto p : productos) {
                        %>
                            <option value="<%= p.getId() %>">
                                <%= p.getNombre() %> ($<%= p.getPrecioUnitario() %>)
                            </option>
                        <%
                                }
                            }
                        %>
                    </select>
                    <small class="text-muted">
                        Los productos y manuales se configuran desde el módulo <strong>Productos</strong>.
                    </small>
                </div>

                <!-- CANTIDAD -->
                <div class="col-md-4">
                    <label class="form-label">Cantidad</label>
                    <input type="number" min="1" name="cantidad" class="form-control" required>
                </div>

                <div class="col-12">
                    <button class="btn btn-success">Registrar venta</button>
                    <a href="${pageContext.request.contextPath}/home.jsp" class="btn btn-secondary">Cancelar</a>
                </div>

            </form>

        </div>
    </div>
</div>

<script>
    const hayPrecios = <%= hayPrecios ? "true" : "false" %>;

    function actualizarUI() {
        const tipoVenta       = document.getElementById("tipoVenta");
        const tipo            = tipoVenta.value;
        const bloqueCopias    = document.getElementById("bloqueCopias");
        const bloqueProductos = document.getElementById("bloqueProductos");
        const precioId        = document.getElementById("precioId");
        const producto        = document.getElementById("productoId");

        if (tipo === "COPIA") {
            if (!hayPrecios) {
                alert("No hay precios configurados en la tabla PRECIOS.");
                tipoVenta.value = "PRODUCTO";
                actualizarUI();
                return;
            }
            if (bloqueCopias)    bloqueCopias.classList.remove("d-none");
            if (bloqueProductos) bloqueProductos.classList.add("d-none");

            if (precioId) precioId.required = true;
            if (producto) {
                producto.required = false;
                producto.value = "";
            }
        } else { // PRODUCTO
            if (bloqueCopias)    bloqueCopias.classList.add("d-none");
            if (bloqueProductos) bloqueProductos.classList.remove("d-none");

            if (precioId) precioId.required = false;
            if (producto) producto.required = true;
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        if (!hayPrecios) {
            const tipoVenta = document.getElementById("tipoVenta");
            tipoVenta.value = "PRODUCTO";
        }
        actualizarUI();
        document.getElementById("tipoVenta").addEventListener("change", actualizarUI);
    });
</script>

</body>
</html>
