package model;

public class DetalleVenta {
    private int id;
    private Venta venta;
    private Producto producto;  // null si es copia
    private String tipoCopia;   // BN / COLOR
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getTipoCopia() { return tipoCopia; }
    public void setTipoCopia(String tipoCopia) { this.tipoCopia = tipoCopia; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
