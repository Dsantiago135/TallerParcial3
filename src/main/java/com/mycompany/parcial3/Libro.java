package com.mycompany.parcial3;

import java.util.List;

public class Libro {
    public String nombre;
    public String tipo;
    public String editorial;
    public int año;
    public List<Autor> autorRef;
    public List<Copia> copiaRef;
    public Multa multaRef;

    public Libro(String nombre, String tipo, String editorial, int año) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.editorial = editorial;
        this.año = año;
        this.autorRef = autorRef;
        this.copiaRef = copiaRef;
        this.multaRef = multaRef;
    }
    
    
}
