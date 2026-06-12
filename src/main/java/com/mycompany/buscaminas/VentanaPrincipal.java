/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VentanaPrincipal {
    
    private Stage stage;
    private PartidaController controlador;
    
    // Componentes del Juego
    private GridPane panelTablero;
    private Button[][] botonesFisicos;
    private Label lblMinas;
    private Label lblBanderas;
    private Label lblTiempo;
    
    // Variables temporales para la configuración
    private String dificultadSeleccionada = "Fácil";

    public VentanaPrincipal(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("DAMmines App");
    }

    public void setControlador(PartidaController controlador) {
        this.controlador = controlador;
    }

    // ====================================================================
    // PANTALLA 1: INICIO
    // ====================================================================
    public void mostrarPantallaInicio() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #f8fafc;");

        Label titulo = new Label("DAMmmines");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));

        Label lblNombre = new Label("Nombre de usuario");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Introduce tu nombre");
        txtNombre.setMaxWidth(200);

        HBox cajaDificultad = new HBox(10);
        cajaDificultad.setAlignment(Pos.CENTER);
        Button btnFacil = new Button("Fácil");
        Button btnMedio = new Button("Medio");
        Button btnDificil = new Button("Difícil");
        
        String estiloNormal = "-fx-background-color: #e2e8f0; -fx-text-fill: black;";
        String estiloSeleccionado = "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold;";
        
        btnFacil.setStyle(estiloSeleccionado);
        btnMedio.setStyle(estiloNormal);
        btnDificil.setStyle(estiloNormal);

        btnFacil.setOnAction(e -> { dificultadSeleccionada = "Fácil"; btnFacil.setStyle(estiloSeleccionado); btnMedio.setStyle(estiloNormal); btnDificil.setStyle(estiloNormal); });
        btnMedio.setOnAction(e -> { dificultadSeleccionada = "Medio"; btnMedio.setStyle(estiloSeleccionado); btnFacil.setStyle(estiloNormal); btnDificil.setStyle(estiloNormal); });
        btnDificil.setOnAction(e -> { dificultadSeleccionada = "Difícil"; btnDificil.setStyle(estiloSeleccionado); btnFacil.setStyle(estiloNormal); btnMedio.setStyle(estiloNormal); });

        cajaDificultad.getChildren().addAll(btnFacil, btnMedio, btnDificil);

        Button btnJugar = new Button("Jugar");
        btnJugar.setStyle("-fx-background-color: #cbd5e1; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 30 10 30;");
        btnJugar.setOnAction(e -> {
            String nombre = txtNombre.getText().isEmpty() ? "Jugador" : txtNombre.getText();
            controlador.configurarYEmpezarPartida(dificultadSeleccionada, nombre);
        });

        layout.getChildren().addAll(titulo, lblNombre, txtNombre, cajaDificultad, btnJugar);
        
        Scene escena = new Scene(layout, 500, 400);
        stage.setScene(escena);
        stage.show();
    }

    // ====================================================================
    // PANTALLA 2: JUEGO (HUD y Cuadrícula)
    // ====================================================================
    public void mostrarPantallaJuego(int filas, int columnas) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8fafc;");
        layout.setPadding(new Insets(20));

        // HUD Superior
        HBox hud = new HBox(40);
        hud.setAlignment(Pos.CENTER);
        hud.setPadding(new Insets(0, 0, 20, 0));

        VBox minasBox = new VBox(5, new Label("Minas"), lblMinas = new Label("0"));
        minasBox.setAlignment(Pos.CENTER);
        lblMinas.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox banderasBox = new VBox(5, new Label("🚩"), lblBanderas = new Label("0"));
        banderasBox.setAlignment(Pos.CENTER);
        lblBanderas.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox tiempoBox = new VBox(5, new Label("⏱"), lblTiempo = new Label("00:00"));
        tiempoBox.setAlignment(Pos.CENTER);
        lblTiempo.setFont(Font.font("System", FontWeight.BOLD, 18));

        hud.getChildren().addAll(minasBox, banderasBox, tiempoBox);
        layout.setTop(hud);

        // Cuadrícula Central
        panelTablero = new GridPane();
        panelTablero.setAlignment(Pos.CENTER);
        panelTablero.setHgap(2);
        panelTablero.setVgap(2);

        botonesFisicos = new Button[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                Button btn = new Button();
                btn.setPrefSize(35, 35);
                btn.setStyle("-fx-background-color: #cbd5e1; -fx-border-color: #94a3b8;");
                
                final int filaSeleccionada = f;
                final int colSeleccionada = c;

                btn.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        controlador.procesarClicIzquierdo(filaSeleccionada, colSeleccionada);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        controlador.procesarClicDerecho(filaSeleccionada, colSeleccionada);
                    }
                });
                
                botonesFisicos[f][c] = btn;
                panelTablero.add(btn, c, f);
            }
        }
        
        layout.setCenter(panelTablero);

        Scene escena = new Scene(layout);
        stage.setScene(escena);
        stage.sizeToScene(); 
        stage.centerOnScreen();
    }

    // --- ESTE ES EL MÉTODO QUE TE FALTABA PARA EL HUD ---
    public void actualizarHUD(int minasRestantes, int banderasRestantes, String tiempoStr) {
        lblMinas.setText(String.valueOf(minasRestantes));
        lblBanderas.setText(String.valueOf(banderasRestantes));
        lblTiempo.setText(tiempoStr);
    }

    public void actualizarBotonCelda(int fila, int columna, String texto, String colorFondo) {
        Button btn = botonesFisicos[fila][columna];
        btn.setText(texto);
        btn.setStyle("-fx-background-color: " + colorFondo + "; -fx-border-color: #94a3b8; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
    }

    // ====================================================================
    // PANTALLA 3: RESULTADO (MÉTODO QUE TE FALTABA)
    // ====================================================================
    public void mostrarPantallaResultado(boolean victoria, String tiempoFinal) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: white;");

        Label titulo = new Label(victoria ? "¡Victoria!" : "¡Derrota!");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titulo.setStyle(victoria ? "-fx-text-fill: #16a34a;" : "-fx-text-fill: #dc2626;");

        Label lblResultadoTiempo = new Label("Tiempo final: " + tiempoFinal);
        lblResultadoTiempo.setFont(Font.font("Arial", 18));

        Button btnVolver = new Button("Volver al inicio");
        btnVolver.setStyle("-fx-background-color: #e2e8f0; -fx-padding: 10 20 10 20;");
        btnVolver.setOnAction(e -> mostrarPantallaInicio());

        layout.getChildren().addAll(titulo, lblResultadoTiempo, btnVolver);
        
        Scene escena = new Scene(layout, 400, 300);
        stage.setScene(escena);
        stage.centerOnScreen();
    }
}