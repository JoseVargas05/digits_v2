package com.fcfm.cambia_10.entidades;

import java.util.Date;

public class contact {
    private int id;
    private String nombre;
    private String numero;
    private Date fecha;

    public contact(int id, String nombre, String numero, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
