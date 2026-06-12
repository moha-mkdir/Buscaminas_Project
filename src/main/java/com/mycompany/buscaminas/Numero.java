/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;

/**
 *
 * @author jonelvper
 */
public class Numero extends Celda {
    private int numero; // Cantidad de minas colindantes (1-8)

    public Numero(int fila, int columna, int numero) {
        super(fila, columna);
        this.numero = numero;
    }

    @Override
    public void revelar() {
        this.estadoVisibilidad = "DESCUBIERTA";
    }

    // Getter y Setter específico
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
}
