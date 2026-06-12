/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;

/**
 *
 * @author jonelvper
 */
    public class Mina extends Celda {

    public Mina(int fila, int columna) {
        super(fila, columna);
    }

    @Override
    public void revelar() {
        this.estadoVisibilidad = "DESCUBIERTA";
    }
    }
