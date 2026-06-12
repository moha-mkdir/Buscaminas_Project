/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminas;
import java.sql.Date;
/**
 *
 * @author jonelvper
 */


public class Usuario {
    private int idUsuario;
    private String nombre;
    private Date fechaIni;

    public Usuario(int idUsuario, String nombre, Date fechaIni) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.fechaIni = fechaIni;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFechaIni() { return fechaIni; }
    public void setFechaIni(Date fechaIni) { this.fechaIni = fechaIni; }
}