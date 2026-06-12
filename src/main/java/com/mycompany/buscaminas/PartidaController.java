package com.mycompany.buscaminas;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PartidaController {
    
    private Tablero tableroActual;
    private GestorBD baseDatos;
    private VentanaPrincipal vista;
    
    private int minasTotales;
    private int banderasColocadas;
    private Timeline cronometro;
    private int segundosTranscurridos;

    private String jugadorActual;
    private String dificultadActual;

    public PartidaController(VentanaPrincipal vista) {
        this.vista = vista;
        this.baseDatos = new GestorBD();
    }

    public void configurarYEmpezarPartida(String dificultad, String nombreJugador) {
        int filas = 0, columnas = 0;
        
        this.jugadorActual = nombreJugador;
        this.dificultadActual = dificultad;
        
        switch (dificultad) {
            case "Fácil":
                filas = 12; columnas = 10; minasTotales = 10;
                break;
            case "Medio":
                filas = 16; columnas = 16; minasTotales = 40;
                break;
            case "Difícil":
                filas = 16; columnas = 30; minasTotales = 99;
                break;
        }
        
        iniciarNuevaPartida(filas, columnas, minasTotales, nombreJugador);
    }

    private void iniciarNuevaPartida(int filas, int columnas, int minas, String nombreJugador) {
        this.banderasColocadas = 0;
        this.segundosTranscurridos = 0;
        
        this.tableroActual = new Tablero(filas, columnas, minas);
        
        vista.mostrarPantallaJuego(filas, columnas);
        vista.actualizarHUD(minasTotales, minasTotales, "00:00");
        
        iniciarCronometro();
    }
    
    private void iniciarCronometro() {
        if (cronometro != null) {
            cronometro.stop();
        }
        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundosTranscurridos++;
            vista.actualizarHUD(minasTotales, (minasTotales - banderasColocadas), formatearTiempo(segundosTranscurridos));
        }));
        cronometro.setCycleCount(Animation.INDEFINITE);
        cronometro.play();
    }
    
    private String formatearTiempo(int segundosTotales) {
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    public void procesarClicIzquierdo(int fila, int columna) {
        Celda celdaPulsada = tableroActual.getMatrizCeldas()[fila][columna];
        
        if (celdaPulsada.isTieneBandera() || celdaPulsada.getEstadoVisibilidad().equals("DESCUBIERTA")) {
            return; 
        }
        
        if (celdaPulsada instanceof Mina) {
            celdaPulsada.revelar();
            vista.actualizarBotonCelda(fila, columna, "💣", "#ef4444"); 
            gameOver(false); 
        } else if (celdaPulsada instanceof Numero) {
            celdaPulsada.revelar();
            Numero num = (Numero) celdaPulsada;
            vista.actualizarBotonCelda(fila, columna, String.valueOf(num.getNumero()), "#f1f5f9");
        } else if (celdaPulsada instanceof Vacio) {
            // CORREGIDO: En lugar de abrir una sola celda, disparamos la recursividad
            revelarRecursivo(fila, columna);
        }
    }

    // =====================================================================
    // NUEVO MÉTODO: ALGORITMO RECURSIVO DE EXPANSIÓN (FLOOD FILL)
    // =====================================================================
    private void revelarRecursivo(int f, int c) {
        // 1. Control de límites: Si nos salimos del tablero, paramos
        if (f < 0 || f >= tableroActual.getMatrizCeldas().length || c < 0 || c >= tableroActual.getMatrizCeldas()[0].length) {
            return;
        }
        
        Celda celda = tableroActual.getMatrizCeldas()[f][c];
        
        // 2. Si ya está descubierta o tiene una bandera puesta, ignoramos esta celda
        if (celda.getEstadoVisibilidad().equals("DESCUBIERTA") || celda.isTieneBandera()) {
            return;
        }
        
        // 3. Revelamos la celda lógicamente
        celda.revelar();
        
        // 4. Si la celda es Vacía, se limpia visualmente y se extiende a sus 8 vecinas
        if (celda instanceof Vacio) {
            vista.actualizarBotonCelda(f, c, "", "#f1f5f9");
            
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) { // Evita llamarse a sí misma
                        revelarRecursivo(f + i, c + j);
                    }
                }
            }
        } 
        // 5. Si la celda es un Número, lo mostramos en la interfaz pero NO seguimos extendiendo
        else if (celda instanceof Numero) {
            Numero num = (Numero) celda;
            vista.actualizarBotonCelda(f, c, String.valueOf(num.getNumero()), "#f1f5f9");
        }
    }

    public void procesarClicDerecho(int fila, int columna) {
        Celda celdaPulsada = tableroActual.getMatrizCeldas()[fila][columna];
        
        if (!celdaPulsada.getEstadoVisibilidad().equals("DESCUBIERTA")) {
            if (!celdaPulsada.isTieneBandera() && banderasColocadas < minasTotales) {
                celdaPulsada.alternarBandera();
                banderasColocadas++;
                vista.actualizarBotonCelda(fila, columna, "🚩", "#fde047");
            } else if (celdaPulsada.isTieneBandera()) {
                celdaPulsada.alternarBandera();
                banderasColocadas--;
                vista.actualizarBotonCelda(fila, columna, "", "#cbd5e1");
            }
            vista.actualizarHUD(minasTotales, (minasTotales - banderasColocadas), formatearTiempo(segundosTranscurridos));
        }
    }

    public void gameOver(boolean victoria) {
        if (cronometro != null) cronometro.stop();
        String tiempoFinalizado = formatearTiempo(segundosTranscurridos);
        
        if (baseDatos.conectar()) {
            int idUsuario = baseDatos.registrarUsuario(jugadorActual);
            
            Partida partida = new Partida(idUsuario, dificultadActual);
            partida.setResultado(victoria ? "Ganada" : "Perdida");
            int idPartida = baseDatos.guardarPartida(partida);
            
            if(idPartida != -1) {
                Celda[][] matriz = tableroActual.getMatrizCeldas();
                for(int f = 0; f < matriz.length; f++) {
                    for(int c = 0; c < matriz[0].length; c++) {
                        baseDatos.guardarCelda(matriz[f][c], idPartida);
                    }
                }
            }
            baseDatos.desconectar();
        }
        
        vista.mostrarPantallaResultado(victoria, tiempoFinalizado);
    }
}