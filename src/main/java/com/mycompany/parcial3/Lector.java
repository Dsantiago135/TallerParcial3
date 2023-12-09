package com.mycompany.parcial3;
import java.util.List;

public class Lector{
    public String numSocio;
    public String nombre;
    public String apellido;
    public String direccion;
    public List<Copia> copiaLec;

    public Lector(String numSocio, String nombre, String apellido, String direccion) {
        this.numSocio = numSocio;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
    }

    public Lector() {
    }
    
}
