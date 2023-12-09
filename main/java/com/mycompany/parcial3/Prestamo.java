package com.mycompany.parcial3;

public class Prestamo {
    public int fechaInicio;
    public int fechaFin;
    public Multa multaPrest;
    public Lector lectorPrest;
    public Copia copiaPrest;

    public Prestamo(int fechaInicio, int fechaFin, Multa multaPrest, Lector lectorPrest, Copia copiaPrest) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.multaPrest = multaPrest;
        this.lectorPrest = lectorPrest;
        this.copiaPrest = copiaPrest;
    }
    
}
