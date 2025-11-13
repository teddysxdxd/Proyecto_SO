package com.mycompany.proyectofinalsistemasoperativos;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

public class InterfazSimulador extends JFrame {

    private final GestorMemoria gestor;
    private final Object monitor = new Object();
    private final JTextArea areaSalida;
    private final JTextField campoId, campoTamano, campoTiempo;
    private final List<Thread> procesosActivos = Collections.synchronizedList(new ArrayList<>());
    private final Queue<Proceso> colaEspera = new LinkedList<>();
    private final PanelMemoria panelMemoria;

    public InterfazSimulador() {
        // === CONFIGURACIÓN GENERAL ===
        setTitle("Simulador de Asignación de Memoria - Proyecto Final Sistemas Operativos I");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // === SOLICITAR MEMORIA TOTAL ===
        double totalKB = Double.parseDouble(
            JOptionPane.showInputDialog("Ingrese el tamaño total de memoria (KB):")
        );
        gestor = new GestorMemoria((long) (totalKB * 1024));

        // === ENCABEZADO SUPERIOR ===
        // === ENCABEZADO SUPERIOR (versión corregida con espacio adecuado) ===
            JPanel header = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(
                            0, 0, new Color(5, 10, 30),
                            getWidth(), getHeight(), new Color(25, 25, 60));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            header.setPreferredSize(new Dimension(getWidth(), 180)); // AUMENTAMOS altura
            header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            header.setLayout(new BorderLayout());

            // --- LOGO ---
            JLabel logo = new JLabel(new ImageIcon("logo_universidad.png"));
            logo.setPreferredSize(new Dimension(110, 110));
            logo.setHorizontalAlignment(SwingConstants.CENTER);

            // --- TEXTOS ---
            JLabel titulo = new JLabel("PROYECTO FINAL - SISTEMAS OPERATIVOS I", SwingConstants.CENTER);
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titulo.setForeground(Color.WHITE);

            JLabel subtitulo = new JLabel("Centro Universitario Boca del Monte | Ingeniería en Sistemas de la Información", SwingConstants.CENTER);
            subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            subtitulo.setForeground(new Color(210, 210, 210));

            JLabel integrantes = new JLabel("<html><div style='text-align:center;'>"
                    + "<span style='color:#E0E0E0;'>Fidian Bianchi Morales Pastran 7690-22-22593</span><br>"
                    + "<span style='color:#D0D0D0;'>Javier Sandoval 7690-23-5643</span><br>"
                    + "<span style='color:#D0D0D0;'>Tedy Adolfo Castellanos 7690-23-3016</span><br>"
                    + "</div></html>", SwingConstants.CENTER);
            integrantes.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            // --- PANEL CENTRAL DE TEXTOS ---
            JPanel infoPanel = new JPanel(new GridLayout(3, 1, 2, 5));
            infoPanel.setOpaque(false);
            infoPanel.add(titulo);
            infoPanel.add(subtitulo);
            infoPanel.add(integrantes);

            // --- LÍNEA SEPARADORA INFERIOR (opcional, visualmente bonito) ---
            //JPanel linea = new JPanel();
            //linea.setBackground(new Color(70, 90, 150));
            //linea.setPreferredSize(new Dimension(getWidth(), 2));

            header.add(logo, BorderLayout.WEST);
            header.add(infoPanel, BorderLayout.CENTER);
            //header.add(linea, BorderLayout.SOUTH);

            add(header, BorderLayout.NORTH);


        // === PANEL DE ENTRADA DE PROCESOS ===
        JPanel panelSuperior = new JPanel(new GridBagLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Nuevo Proceso"));
        panelSuperior.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoId = new JTextField(8);
        campoTamano = new JTextField(8);
        campoTiempo = new JTextField(8);
        JButton botonAgregar = new JButton("Agregar Proceso");

        botonAgregar.setBackground(new Color(33, 97, 140));
        botonAgregar.setForeground(Color.WHITE);
        botonAgregar.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0;
        panelSuperior.add(new JLabel("ID Proceso:"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(campoId, gbc);

        gbc.gridx = 2;
        panelSuperior.add(new JLabel("Tamaño (B):"), gbc);
        gbc.gridx = 3;
        panelSuperior.add(campoTamano, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelSuperior.add(new JLabel("Tiempo (segundos):"), gbc);
        gbc.gridx = 1;
        panelSuperior.add(campoTiempo, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelSuperior.add(botonAgregar, gbc);

        add(panelSuperior, BorderLayout.WEST);

        // === PANEL DE MEMORIA ===
        panelMemoria = new PanelMemoria(gestor);
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder("Vista de Memoria Principal"));
        panelCentro.add(panelMemoria, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // === ÁREA DE SALIDA ===
        areaSalida = new JTextArea(10, 50);
        areaSalida.setEditable(false);
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaSalida);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro de eventos"));
        add(scroll, BorderLayout.SOUTH);

        // === EVENTOS ===
        botonAgregar.addActionListener(e -> agregarProceso());

        // === HILO DE VERIFICACIÓN ===
        new Thread(this::verificarCola).start();

        setVisible(true);
    }

    private void agregarProceso() {
        try {
            int id = Integer.parseInt(campoId.getText().trim());
            double tamanoKB = Double.parseDouble(campoTamano.getText().trim());
            int tiempo = Integer.parseInt(campoTiempo.getText().trim());
            long tamanoBytes = (long) (tamanoKB);

            Proceso p = new Proceso(id, tamanoBytes, tiempo);

            if (tamanoBytes > gestor.getTotalMemoria()) {
                JOptionPane.showMessageDialog(
                    this,
                    "El proceso " + id + " requiere más memoria (" + tamanoKB + " B) que la disponible (" +
                    gestor.getTotalMemoria() + " B).",
                    "Advertencia: Memoria insuficiente",
                    JOptionPane.WARNING_MESSAGE
                );
                areaSalida.append("Proceso " + id + " rechazado: supera la memoria total.\n");
                limpiarCampos();
                return;
            }

            synchronized (monitor) {
                while (!gestor.asignarMemoria(p)) {
                    areaSalida.append(" No hay memoria para el proceso " + id + ". Esperando...\n");
                    monitor.wait(2000);
                }
            }

            areaSalida.append("Proceso " + id + " asignado correctamente.\n");
            areaSalida.append(gestor.obtenerEstadoMemoria());
            panelMemoria.actualizarVista();

            Thread hilo = new Thread(new TareaProceso(p, gestor, monitor, areaSalida, panelMemoria));
            procesosActivos.add(hilo);
            hilo.start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            limpiarCampos();
        }
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoTamano.setText("");
        campoTiempo.setText("");
    }

    private void iniciarProceso(Proceso p) {
        areaSalida.append("Proceso " + p.getId() + " asignado correctamente.\n");
        areaSalida.append(gestor.obtenerEstadoMemoria());
        panelMemoria.actualizarVista();

        Thread hilo = new Thread(new TareaProceso(p, gestor, monitor, areaSalida, panelMemoria));
        procesosActivos.add(hilo);
        hilo.start();
    }

    private void verificarCola() {
        while (true) {
            synchronized (monitor) {
                if (!colaEspera.isEmpty()) {
                    Proceso siguiente = colaEspera.peek();
                    if (gestor.asignarMemoria(siguiente)) {
                        colaEspera.poll();
                        SwingUtilities.invokeLater(() -> iniciarProceso(siguiente));
                    }
                }
            }
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfazSimulador::new);
    }
}
