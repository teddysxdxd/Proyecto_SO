/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinalsistemasoperativos;

/**
 *
 * @author jisaa
 */
public class BloqueMemoria {
    int numeroBloque;
    EstadoBloque estado;
    BloqueMemoria izquierda;
    BloqueMemoria derecha;
    long tamanoBytes;
    Integer procesoAsignado;

    public BloqueMemoria(int numeroBloque, EstadoBloque estado, long tamanoBytes) {
        this.numeroBloque = numeroBloque;
        this.estado = estado;
        this.tamanoBytes = tamanoBytes;
    }

    public boolean estaLibre() {
        return estado == EstadoBloque.LIBRE;
    }

    @Override
    public String toString() {
        String estadoTexto = (estado == EstadoBloque.LIBRE) ? "Libre" : "Ocupado";
        String proc = (procesoAsignado == null) ? "-" : procesoAsignado.toString();
        return String.format("Bloque %d | %s | Tamaño: %d bytes | Proceso: %s",
                numeroBloque, estadoTexto, tamanoBytes, proc);
    }
}