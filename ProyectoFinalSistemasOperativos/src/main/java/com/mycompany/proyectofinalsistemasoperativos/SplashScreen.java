/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinalsistemasoperativos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Splash Screen con fondo degradado, estrellas y barra de carga animada.
 */
public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel lblTexto;
    private float alpha = 1f;

    public SplashScreen() {
        setSize(600, 450); // Aumentamos la altura para dar más espacio vertical
        setLocationRelativeTo(null);

        // Panel principal con fondo degradado + estrellas
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint grad = new GradientPaint(0, 0,
                        new Color(10, 20, 40), getWidth(), getHeight(), new Color(30, 30, 70));
                g2d.setPaint(grad);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Estrellas sutiles
                g2d.setColor(new Color(255, 255, 255, 60));
                for (int i = 0; i < 40; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    int s = (int) (Math.random() * 2) + 1;
                    g2d.fillOval(x, y, s, s);
                }
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Logo institucional
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setIcon(new ImageIcon("logo_universidad.png")); // logo UMG

        // Títulos
        JLabel titulo = new JLabel("PROYECTO FINAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Simulador de Gestión de Memoria", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitulo.setForeground(Color.LIGHT_GRAY);

        // ** CAMBIO 1: Texto de la universidad actualizado para coincidir con la imagen **
        JLabel universidad = new JLabel("Centro Universitario Boca del Monte | Ingeniería en Sistemas de la Información", SwingConstants.CENTER);
        universidad.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        universidad.setForeground(new Color(200, 200, 200));

        // Nombres del equipo - Se revisa el HTML para asegurar los saltos de línea
        JLabel integrantes = new JLabel("<html><div style='text-align:center;'>"
            + "Fidian Bianchi Morales Pastran 7690-22-22593<br>" 
            + "Javier Isaac Sandoval Perez 7690-23-5643<br>" 
            + "Tedi Adolfo Castellanos 7690-23-3016<br>" 
            + "ahor<br>"
            + "</div></html>", SwingConstants.CENTER);

        integrantes.setForeground(new Color(176, 176, 176));
        integrantes.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // ** CAMBIO 2: GridLayout ajustado a 4 filas para las 4 etiquetas **
        JPanel textoPanel = new JPanel(new GridLayout(4, 1));
        textoPanel.setOpaque(false);
        textoPanel.add(titulo);
        textoPanel.add(subtitulo);
        textoPanel.add(universidad);
        textoPanel.add(integrantes);

        // Texto inferior
        lblTexto = new JLabel("Cargando simulador...", SwingConstants.CENTER);
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Barra de progreso
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 204, 255));
        progressBar.setBackground(Color.WHITE);

        panel.add(logo, BorderLayout.NORTH);
        panel.add(textoPanel, BorderLayout.CENTER);
        panel.add(lblTexto, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);
    }

    public void mostrarYCerrar(Runnable abrirSimulador) {
        setVisible(true);

        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(50); // velocidad = duración total aprox. 5 seg
                    final int progreso = i;
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progreso);
                        lblTexto.setText("Cargando simulador... " + progreso + "%");
                    });
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                dispose();
                abrirSimulador.run();
            });
        }).start();
    }
}