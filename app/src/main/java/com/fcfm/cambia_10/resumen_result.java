package com.fcfm.cambia_10;

import java.io.Serializable;

public class resumen_result{
    private int id;
    private String nombre;
    private String numero;
    private String nacional;

    public resumen_result(int id, String nombre, String numero, String nacional) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.nacional = nacional;
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

    public String getNacional() {
        return nacional;
    }

    public void setNacional(String nacional) {
        this.nacional = nacional;
    }
}
