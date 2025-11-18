package model;

import java.util.Date;

public class Archivo {
    private int id;
    private Usuario usuario;
    private String codigo;
    private String mensaje;        // 🆕 NUEVO CAMPO
    private String nombreArchivo;
    private String rutaArchivo;
    private Date fechaSubida;
    private boolean procesado;

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public String getCodigo() { 
        return codigo; 
    }
    public void setCodigo(String codigo) { 
        this.codigo = codigo; 
    }

    // 🆕 GETTER/SETTER DEL MENSAJE
    public String getMensaje() { 
        return mensaje; 
    }
    public void setMensaje(String mensaje) { 
        this.mensaje = mensaje; 
    }

    public String getNombreArchivo() { 
        return nombreArchivo; 
    }
    public void setNombreArchivo(String nombreArchivo) { 
        this.nombreArchivo = nombreArchivo; 
    }

    public String getRutaArchivo() { 
        return rutaArchivo; 
    }
    public void setRutaArchivo(String rutaArchivo) { 
        this.rutaArchivo = rutaArchivo; 
    }

    public Date getFechaSubida() { 
        return fechaSubida; 
    }
    public void setFechaSubida(Date fechaSubida) { 
        this.fechaSubida = fechaSubida; 
    }

    public boolean isProcesado() { 
        return procesado; 
    }
    public void setProcesado(boolean procesado) { 
        this.procesado = procesado; 
    }
}
