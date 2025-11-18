package model;

import java.util.Date;
import java.util.List;

public class Venta {
    private int id;
    private Usuario usuario;
    private String tipoVenta;   // COPIA / PRODUCTO
    private Date fechaHora;
    private Turno turno;
    private double total;
    private boolean activo;
    private List<DetalleVenta> detalles;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getTipoVenta() { return tipoVenta; }
    public void setTipoVenta(String tipoVenta) { this.tipoVenta = tipoVenta; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
