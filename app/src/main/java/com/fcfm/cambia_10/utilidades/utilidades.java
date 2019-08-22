package com.fcfm.cambia_10.utilidades;

public class utilidades {
    //Constantes
    public static final String TABLA_CONTACTO="contact";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_ID_CONTACT="idContact";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_NUMERO="numero";
    public static final String CAMPO_FECHA="date";

   public static final String CREAR_TABLA_CONTACTO = "CREATE TABLE "+TABLA_CONTACTO+"("+CAMPO_ID+" INTEGER, "+CAMPO_ID_CONTACT+" INTEGER, "+CAMPO_NOMBRE+" TEXT, "+CAMPO_NUMERO+" TEXT, "+CAMPO_FECHA+" DATETIME DEFAULT CURRENT_TIMESTAMP)";
}
