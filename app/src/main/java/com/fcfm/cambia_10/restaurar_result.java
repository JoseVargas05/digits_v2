package com.fcfm.cambia_10;

public class restaurar_result {
    private int id;
    private String nombre;
    private String numero;
    private String estado;
    private boolean isSelected;

    //Constructor
    public restaurar_result(int id, String nombre, String numero, String estado, boolean isSelected) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.estado = estado;
        this.isSelected = isSelected;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
