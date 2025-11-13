/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinalsistemasoperativos;

/**
 *
 * @author jisaa
 */

public class Proceso {
    int id;
    long tamanoBytes;
    int tiempoSegundos;
    EstadoProceso estado;

    public Proceso(int id, long tamanoBytes, int tiempoSegundos) {
        this.id = id;
        this.tamanoBytes = tamanoBytes;
        this.tiempoSegundos = tiempoSegundos;
        this.estado = EstadoProceso.ESPERANDO;
    }

    public int getId() {
        return id;
    }

    public long getTamanoBytes() {
        return tamanoBytes;
    }

    public int getTiempoSegundos() {
        return tiempoSegundos;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTamanoBytes(long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public void setTiempoSegundos(int tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }
    
    
}