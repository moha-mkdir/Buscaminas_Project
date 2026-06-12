package com.mycompany.buscaminas;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VentanaPrincipal vista = new VentanaPrincipal(primaryStage);
        PartidaController controlador = new PartidaController(vista);
        vista.setControlador(controlador);
        
        // Arrancamos directamente en la pantalla de inicio del juego
        vista.mostrarPantallaInicio(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}

