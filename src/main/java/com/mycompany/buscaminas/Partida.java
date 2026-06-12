package com.mycompany.buscaminas;

import java.sql.Timestamp;

public class Partida {
    private int idPartida;
    private Timestamp inicio;
    private int idUsuario;
    private String dificultad;
    private String resultado;
    private Tablero tableroJuego; 

    public Partida(int idUsuario, String dificultad) {
        this.idUsuario = idUsuario;
        this.dificultad = dificultad;
        this.resultado = "En curso"; // Valor por defecto según tu SQL
    }

    // Getters y Setters
    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }

    public Timestamp getInicio() { return inicio; }
    public void setInicio(Timestamp inicio) { this.inicio = inicio; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public Tablero getTableroJuego() { return tableroJuego; }
    public void setTableroJuego(Tablero tableroJuego) { this.tableroJuego = tableroJuego; }
}