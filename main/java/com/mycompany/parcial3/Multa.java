package com.mycompany.parcial3;

public class Multa {
    public int fechInicio;
    public int fechFin;
    public Lector lectorMul;
    public Prestamo prestMult;

    public Multa(int fechInicio, int fechFin, Lector lectorMul, Prestamo prestMult) {
        this.fechInicio = fechInicio;
        this.fechFin = fechFin;
        this.lectorMul = lectorMul;
        this.prestMult = prestMult;
    }  
}