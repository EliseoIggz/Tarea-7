package com.example.tarea_7;

import java.time.LocalDateTime;

public class Deberes {
    private String titulo;
    private String descripcion;
    private String asignatura;
    private String fecha;
    private String hora;
    private boolean estado;

    public Deberes(String titulo, String descripcion, String asignatura, String fecha, String hora, boolean estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.asignatura = asignatura;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getEstado(){
        if (estado) {
            return "Completado";
        }else{
            return "No completado";
        }
    }
}
