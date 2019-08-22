package com.fcfm.cambia_10;

import java.io.Serializable;

public class contact_result implements Serializable {
    private int id;
    private String nombre;
    private String nacional;
    private String aquitar;
    private String numero;
    private String tipo;
    private boolean isSelected;

    //Constructor

    public contact_result(int id, String nombre, String nacional, String aquitar, String numero, boolean isSelected) {
        this.id = id;
        this.nombre = nombre;
        this.nacional = nacional;
        this.aquitar = aquitar;
        this.numero = numero;
        this.isSelected = isSelected;
    }

    //Steller, getter

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

    public String getNacional() {
        return nacional;
    }

    public void setNacional(String nacional) {
        this.nacional = nacional;
    }

    public String getAquitar() {
        return aquitar;
    }

    public void setAquitar(String aquitar) {
        this.aquitar = aquitar;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String numero) {
        this.tipo = tipo;
    }

    public boolean isSelected(){ return isSelected;}

    public void setSelected(boolean selected){isSelected = selected;}


}
