package model;

import java.sql.Time;

public class Turno {

    private int id;
    private String nombre;     // AM / PM
    private Time horaInicio;   // hora_inicio
    private Time horaFin;      // hora_fin

    public Turno() {
    }

    public Turno(int id, String nombre, Time horaInicio, Time horaFin) {
        this.id = id;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // NOMBRE (AM / PM)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // HORA INICIO
    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    // HORA FIN
    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void setActivo(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
