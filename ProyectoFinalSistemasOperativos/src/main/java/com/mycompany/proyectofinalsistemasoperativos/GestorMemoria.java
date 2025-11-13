/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinalsistemasoperativos;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jisaa
 */

public class GestorMemoria {

    enum EstadoBloque { LIBRE, OCUPADO }

    static class BloqueMemoria {
        int numeroBloque;
        EstadoBloque estado;
        BloqueMemoria izquierda;
        BloqueMemoria derecha;
        long tamanoBytes;
        Integer procesoAsignado;

        BloqueMemoria(int numeroBloque, EstadoBloque estado, long tamanoBytes) {
            this.numeroBloque = numeroBloque;
            this.estado = estado;
            this.tamanoBytes = tamanoBytes;
        }

        boolean estaLibre() {
            return estado == EstadoBloque.LIBRE;
        }

        @Override
        public String toString() {
            String estadoStr = (estado == EstadoBloque.LIBRE) ? "Libre" : "Ocupado";
            String proc = (procesoAsignado == null) ? "-" : procesoAsignado.toString();
            return String.format("Bloque %d | %s | Tamaño: %d bytes | Proceso: %s",
                    numeroBloque, estadoStr, tamanoBytes, proc);
        }
    }

    private BloqueMemoria cabeza;
    private AtomicInteger siguienteNumeroBloque = new AtomicInteger(1);
    private long totalBytes;
    private long usadosBytes = 0;

    public GestorMemoria(long totalBytes) {
        this.totalBytes = totalBytes;
        this.usadosBytes = 0;
        this.cabeza = new BloqueMemoria(siguienteNumeroBloque.getAndIncrement(), EstadoBloque.LIBRE, totalBytes);
    }

    public synchronized boolean asignarMemoria(Proceso p) {
        BloqueMemoria actual = cabeza;
        while (actual != null) {
            if (actual.estaLibre() && actual.tamanoBytes >= p.tamanoBytes) {
                if (actual.tamanoBytes > p.tamanoBytes) {
                    long restante = actual.tamanoBytes - p.tamanoBytes;
                    actual.tamanoBytes = p.tamanoBytes;

                    BloqueMemoria nuevoLibre = new BloqueMemoria(
                        siguienteNumeroBloque.getAndIncrement(),
                        EstadoBloque.LIBRE,
                        restante
                    );
                    nuevoLibre.derecha = actual.derecha;
                    nuevoLibre.izquierda = actual;
                    if (actual.derecha != null) actual.derecha.izquierda = nuevoLibre;
                    actual.derecha = nuevoLibre;
                }
                actual.estado = EstadoBloque.OCUPADO;
                actual.procesoAsignado = p.id;
                usadosBytes += actual.tamanoBytes;
                return true;
            }
            actual = actual.derecha;
        }
        return false;
    }

    public synchronized boolean liberarMemoria(Proceso p) {
        BloqueMemoria actual = cabeza;
        boolean liberado = false;
        while (actual != null) {
            if (!actual.estaLibre() && actual.procesoAsignado != null && actual.procesoAsignado.equals(p.id)) {
                actual.estado = EstadoBloque.LIBRE;
                usadosBytes -= actual.tamanoBytes;
                actual.procesoAsignado = null;
                liberado = true;

                // Fusionar bloques libres adyacentes
                if (actual.izquierda != null && actual.izquierda.estaLibre()) {
                    BloqueMemoria izq = actual.izquierda;
                    izq.tamanoBytes += actual.tamanoBytes;
                    izq.derecha = actual.derecha;
                    if (actual.derecha != null) actual.derecha.izquierda = izq;
                    actual = izq;
                }
                if (actual.derecha != null && actual.derecha.estaLibre()) {
                    BloqueMemoria der = actual.derecha;
                    actual.tamanoBytes += der.tamanoBytes;
                    actual.derecha = der.derecha;
                    if (der.derecha != null) der.derecha.izquierda = actual;
                }
            }
            actual = actual.derecha;
        }
        return liberado;
    }

    public synchronized String obtenerEstadoMemoria() {
        StringBuilder sb = new StringBuilder("\n--- ESTADO DE MEMORIA ---\n");
        BloqueMemoria actual = cabeza;
        while (actual != null) {
            sb.append(actual.toString()).append("\n");
            actual = actual.derecha;
        }
        sb.append(String.format(
            "Total: %d bytes | Usados: %d bytes | Libres: %d bytes\n",
            totalBytes, usadosBytes, totalBytes - usadosBytes
        ));
        sb.append("---------------------------\n");
        return sb.toString();
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public BloqueMemoria getCabeza() {
        return cabeza;
    }

    public long getTotalMemoria() {
        return totalBytes;
    }
}
