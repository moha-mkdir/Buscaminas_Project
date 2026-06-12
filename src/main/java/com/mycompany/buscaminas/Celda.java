/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;

/**
 *
 * @author jonelvper
 */
public abstract class Celda {
    protected int fila;
    protected int columna;
    protected String estadoVisibilidad;
    protected boolean tieneBandera;
    
   public Celda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.estadoVisibilidad = "OCULTA";
        this.tieneBandera = false;
    }
   
   public int getFila() { return fila; }
    public void setFila(int fila) { 
        this.fila = fila;
    }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { 
        this.columna = columna;
    }

    public String getEstadoVisibilidad() { 
        return estadoVisibilidad; 
    }
    public void setEstadoVisibilidad(String estadoVisibilidad) { 
        this.estadoVisibilidad = estadoVisibilidad; 
    }

    public boolean isTieneBandera() { 
        return tieneBandera; 
    }
    public void setTieneBandera(boolean tieneBandera) { 
        this.tieneBandera = tieneBandera; 
    }
    public abstract void revelar();

    public void alternarBandera() {
        this.tieneBandera = !this.tieneBandera;
    }
}
