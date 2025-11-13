/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinalsistemasoperativos;

/**
 *
 * @author jisaa
 */

import javax.swing.JTextArea;

public class TareaProceso implements Runnable {

    private final Proceso proceso;
    private final GestorMemoria gestor;
    private final Object monitor;
    private final JTextArea areaSalida;
    private final PanelMemoria panelMemoria;

    public TareaProceso(Proceso proceso, GestorMemoria gestor, Object monitor, JTextArea areaSalida, PanelMemoria panelMemoria) {
        this.proceso = proceso;
        this.gestor = gestor;
        this.monitor = monitor;
        this.areaSalida = areaSalida;
        this.panelMemoria = panelMemoria;
    }

    @Override
    public void run() {
        try {
            proceso.estado = EstadoProceso.EJECUTANDO;
            areaSalida.append("🟡 Proceso " + proceso.id + " ejecutándose (" + proceso.tiempoSegundos + " seg)...\n");
            panelMemoria.actualizarVista();

            // Simula la ejecución del proceso
            for (int i = 1; i <= proceso.tiempoSegundos; i++) {
                Thread.sleep(1000);
//                areaSalida.append("⏱ Proceso " + proceso.id + " lleva " + i + " segundos...\n");
            }

            // Libera la memoria del proceso
            gestor.liberarMemoria(proceso);
            proceso.estado = EstadoProceso.FINALIZADO;

            areaSalida.append("✅ Proceso " + proceso.id + " finalizó y liberó memoria.\n");
            areaSalida.append(gestor.obtenerEstadoMemoria());
            panelMemoria.actualizarVista();

            // Notificar a otros hilos que esperaban memoria
            synchronized (monitor) {
                monitor.notifyAll();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
