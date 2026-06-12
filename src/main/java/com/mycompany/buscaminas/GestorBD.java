package com.mycompany.buscaminas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestorBD {
    private Connection conexion;
    // URL adaptada a tu nueva base de datos "dammines"
    private final String url = "jdbc:mysql://localhost:3306/dammines";
    private final String user = "root";
    private final String pass = "";

    public boolean conectar() {
        try {
            conexion = DriverManager.getConnection(url, user, pass);
            return true;
        } catch (SQLException e) {
            System.out.println("Error de conexión a la BD: " + e.getMessage());
            return false;
        }
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    public int registrarUsuario(String nombreUsuario) {
        String sql = "INSERT INTO usuario (nombre) VALUES (?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nombreUsuario);  
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
        return -1; 
    }

    public int guardarPartida(Partida p) {
        String sql = "INSERT INTO partida (id_usuario, dificultad, resultado) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, p.getIdUsuario()); 
            pstmt.setString(2, p.getDificultad());
            pstmt.setString(3, p.getResultado());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1); 
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
        return -1; 
    }

    public void guardarCelda(Celda c, int idPartida) {
        // 1. Inserción en la tabla padre
        String sqlPadre = "INSERT INTO celda (id_partida, fila, columna, estado_visibilidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmtPadre = conexion.prepareStatement(sqlPadre)) {
            pstmtPadre.setInt(1, idPartida);
            pstmtPadre.setInt(2, c.getFila());
            pstmtPadre.setInt(3, c.getColumna());
            pstmtPadre.setString(4, c.getEstadoVisibilidad());
            pstmtPadre.executeUpdate();

            // 2. Inserción en la tabla hija correspondiente
            if (c instanceof Mina) {
                String sqlMina = "INSERT INTO mina (id_partida, fila, columna) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtMina = conexion.prepareStatement(sqlMina)) {
                    pstmtMina.setInt(1, idPartida);
                    pstmtMina.setInt(2, c.getFila());
                    pstmtMina.setInt(3, c.getColumna());
                    pstmtMina.executeUpdate();
                }
            } else if (c instanceof Numero) {
                Numero num = (Numero) c;
                String sqlNumero = "INSERT INTO numero (id_partida, fila, columna, numero) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtNumero = conexion.prepareStatement(sqlNumero)) {
                    pstmtNumero.setInt(1, idPartida);
                    pstmtNumero.setInt(2, c.getFila());
                    pstmtNumero.setInt(3, c.getColumna());
                    pstmtNumero.setInt(4, num.getNumero());
                    pstmtNumero.executeUpdate();
                }
            } else if (c instanceof Vacio) {
                String sqlVacio = "INSERT INTO vacio (id_partida, fila, columna) VALUES (?, ?, ?)";
                try (PreparedStatement pstmtVacio = conexion.prepareStatement(sqlVacio)) {
                    pstmtVacio.setInt(1, idPartida);
                    pstmtVacio.setInt(2, c.getFila());
                    pstmtVacio.setInt(3, c.getColumna());
                    pstmtVacio.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar la celda: " + e.getMessage());
        }
    }
}