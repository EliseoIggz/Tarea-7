package com.example.tarea_7;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class Deberes implements Parcelable {

    private long id;
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

    // Parcelable implementation
    protected Deberes(Parcel in) {
        id = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        asignatura = in.readString();
        fecha = in.readString();
        hora = in.readString();
        estado = in.readByte() != 0;
    }

    public static final Creator<Deberes> CREATOR = new Creator<Deberes>() {
        @Override
        public Deberes createFromParcel(Parcel in) {
            return new Deberes(in);
        }

        @Override
        public Deberes[] newArray(int size) {
            return new Deberes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(asignatura);
        dest.writeString(fecha);
        dest.writeString(hora);
        dest.writeByte((byte) (estado ? 1 : 0));
    }


    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

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
