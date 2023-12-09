package com.mycompany.parcial3;

public class Copia {
    public String identificador;
    public String estado;
    public String nombre;
    public Libro libroRef;
    public Lector lectorRef;

    public Copia(String identificador, String estado,String nombre) {
        this.identificador = identificador;
        this.estado = estado;
        this.nombre = nombre;
    }  

    public Copia() {
    }
    
}
