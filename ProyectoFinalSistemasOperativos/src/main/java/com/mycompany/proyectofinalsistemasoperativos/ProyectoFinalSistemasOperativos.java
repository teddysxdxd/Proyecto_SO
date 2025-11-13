
 /* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

/**
 *
 * @author jisaa
 */

package com.mycompany.proyectofinalsistemasoperativos;

import javax.swing.SwingUtilities;

/**
 * Punto de inicio del Proyecto Final de Sistemas Operativos I.
 * Muestra el SplashScreen y luego carga el simulador de gestión de memoria.
 */
public class ProyectoFinalSistemasOperativos {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Mostrar pantalla de carga con efecto visual
            SplashScreen splash = new SplashScreen();
            splash.mostrarYCerrar(() -> {
                // Una vez terminada la animación, iniciar el simulador principal
                new InterfazSimulador().setVisible(true);
            });
        });
    }
}



