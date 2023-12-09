package com.mycompany.parcial3;

import java.util.List;

public class Autor {
    public String nombre;
    public String nacionalidad;
    public String fechaNac;
    public List<Libro> libroRef;

    public Autor(String nombre, String nacionalidad, String fechaNac, List<Libro> libroRef) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fechaNac = fechaNac;
        this.libroRef = libroRef;
    }
    
    
}
