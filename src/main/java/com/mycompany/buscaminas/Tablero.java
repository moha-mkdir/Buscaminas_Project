/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;

import java.util.Random;

public class Tablero {
    private int filas;
    private int columnas;
    private int numMinas;
    private Celda[][] matrizCeldas;

    public Tablero(int filas, int columnas, int numMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.numMinas = numMinas;
        this.matrizCeldas = new Celda[filas][columnas];
        
        inicializarTablero();
        colocarMinas();
        calcularNumerosAdyacentes();
    }

    private void inicializarTablero() {
        // Llenamos el tablero de vacíos al empezar
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                matrizCeldas[f][c] = new Vacio(f, c);
            }
        }
    }

    private void colocarMinas() {
        Random rand = new Random();
        int minasColocadas = 0;

        while (minasColocadas < numMinas) {
            int fAleatoria = rand.nextInt(filas);
            int cAleatoria = rand.nextInt(columnas);

            // Si en esa posición NO hay ya una mina, la colocamos
            if (!(matrizCeldas[fAleatoria][cAleatoria] instanceof Mina)) {
                matrizCeldas[fAleatoria][cAleatoria] = new Mina(fAleatoria, cAleatoria);
                minasColocadas++;
            }
        }
    }

    private void calcularNumerosAdyacentes() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                // Si la celda es una Mina, la ignoramos
                if (matrizCeldas[f][c] instanceof Mina) continue;

                int minasAlrededor = contarMinasAlrededor(f, c);

                // Si hay minas cerca, reemplazamos el Vacio por un Numero
                if (minasAlrededor > 0) {
                    matrizCeldas[f][c] = new Numero(f, c, minasAlrededor);
                }
            }
        }
    }

    private int contarMinasAlrededor(int f, int c) {
        int contador = 0;
        // Comprobamos las 8 casillas de alrededor asegurándonos de no salirnos del tablero
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nuevaFila = f + i;
                int nuevaColumna = c + j;

                if (nuevaFila >= 0 && nuevaFila < filas && nuevaColumna >= 0 && nuevaColumna < columnas) {
                    if (matrizCeldas[nuevaFila][nuevaColumna] instanceof Mina) {
                        contador++;
                    }
                }
            }
        }
        return contador;
    }

    public Celda[][] getMatrizCeldas() {
        return matrizCeldas;
    }
}