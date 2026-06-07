import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;

public class VentanaPrincipal extends JFrame implements ActionListener {

    private JButton btnAbrirCarpeta, btnTXT, btnCSV, btnJSON, btnXML;
    private JButton btnLeerArchivo;

    private JTextArea areaTextoTerminal;
    private JTextArea areaTextoVisor;
    private JTextField campoEntrada;

    private EscritorArchivo escritor;
    private File carpetaActual = null;
    private String lenguajeActual = "Desconocido";
    private String nombreArchivoFuente = "Desconocido";

    private Process procesoActual;
    private BufferedWriter escritorProceso;

    private StringBuilder historialConsola;
    private StringBuilder historialEntradasUsuario;

    private final String CARPETA_RAIZ_CONVERSIONES = "Archivos_Convertidos";

    public VentanaPrincipal() {
        this.escritor = new EscritorArchivo();
        this.historialConsola = new StringBuilder();
        this.historialEntradasUsuario = new StringBuilder();

        this.setTitle("Herramienta de Testing y Conversión");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JTabbedPane sistemaPestanas = new JTabbedPane();
        sistemaPestanas.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel panelPestana1 = construirPestanaTerminal();
        JPanel panelPestana2 = construirPestanaVisor();

        sistemaPestanas.addTab("Ejecutar Programas", panelPestana1);
        sistemaPestanas.addTab("Lector de Archivos", panelPestana2);

        this.add(sistemaPestanas, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // CONSTRUCTOR DE LA PESTAÑA 1
    private JPanel construirPestanaTerminal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        JPanel panelArriba = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAbrirCarpeta = new JButton("1. Seleccionar Carpeta y Ejecutar");
        btnAbrirCarpeta.setPreferredSize(new Dimension(300, 40));
        btnAbrirCarpeta.addActionListener(this);
        panelArriba.add(btnAbrirCarpeta);
        panelPrincipal.add(panelArriba, BorderLayout.NORTH);

        // Panel Central (Terminal)
        JPanel panelTerminal = new JPanel(new BorderLayout());

        areaTextoTerminal = new JTextArea(20, 60);
        areaTextoTerminal.setEditable(false);
        areaTextoTerminal.setBackground(Color.BLACK);
        areaTextoTerminal.setForeground(Color.GREEN);
        areaTextoTerminal.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaTextoTerminal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(areaTextoTerminal);

        JPanel panelInferiorEntrada = new JPanel(new BorderLayout(10, 0));
        panelInferiorEntrada.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblPrompt = new JLabel("  Escribe tu respuesta  ");
        lblPrompt.setFont(new Font("Consolas", Font.BOLD, 14));

        campoEntrada = new JTextField();
        campoEntrada.setFont(new Font("Consolas", Font.BOLD, 16));
        campoEntrada.setEnabled(false);
        campoEntrada.setBackground(new Color(30, 34, 40));
        campoEntrada.setForeground(Color.CYAN);
        campoEntrada.setCaretColor(Color.WHITE);
        campoEntrada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        campoEntrada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarDatoAlPrograma();
            }
        });

        panelInferiorEntrada.add(lblPrompt, BorderLayout.WEST);
        panelInferiorEntrada.add(campoEntrada, BorderLayout.CENTER);

        panelTerminal.add(scroll, BorderLayout.CENTER);
        panelTerminal.add(panelInferiorEntrada, BorderLayout.SOUTH);
        panelPrincipal.add(panelTerminal, BorderLayout.CENTER);

        // Panel Inferior (Conversores)
        JPanel panelConversiones = new JPanel(new FlowLayout());
        panelConversiones.setBorder(BorderFactory.createTitledBorder("2. Exportar DATOS DEL USUARIO a:"));

        btnTXT = new JButton("TXT");
        btnCSV = new JButton("CSV");
        btnJSON = new JButton("JSON");
        btnXML = new JButton("XML");

        activarBotonesConversor(false);

        btnTXT.addActionListener(this);
        btnCSV.addActionListener(this);
        btnJSON.addActionListener(this);
        btnXML.addActionListener(this);

        panelConversiones.add(btnTXT);
        panelConversiones.add(btnCSV);
        panelConversiones.add(btnJSON);
        panelConversiones.add(btnXML);

        panelPrincipal.add(panelConversiones, BorderLayout.SOUTH);
        return panelPrincipal;
    }

    // CONSTRUCTOR DE LA PESTAÑA 2
    private JPanel construirPestanaVisor() {
        JPanel panelVisor = new JPanel(new BorderLayout(10, 10));
        panelVisor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelArriba = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLeerArchivo = new JButton("Abrir Archivo convertido (.json, .xml, .csv, .txt)");
        btnLeerArchivo.setPreferredSize(new Dimension(400, 40));
        btnLeerArchivo.addActionListener(this);
        panelArriba.add(btnLeerArchivo);

        areaTextoVisor = new JTextArea();
        areaTextoVisor.setEditable(false);
        areaTextoVisor.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaTextoVisor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        areaTextoVisor.setBackground(new Color(245, 245, 245));
        JScrollPane scrollVisor = new JScrollPane(areaTextoVisor);

        panelVisor.add(panelArriba, BorderLayout.NORTH);
        panelVisor.add(scrollVisor, BorderLayout.CENTER);

        return panelVisor;
    }

    // LÓGICA DE BOTONES
    private void activarBotonesConversor(boolean estado) {
        btnTXT.setEnabled(estado);
        btnCSV.setEnabled(estado);
        btnJSON.setEnabled(estado);
        btnXML.setEnabled(estado);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ABRIR CARPETA (PESTAÑA 1)
        if (e.getSource() == btnAbrirCarpeta) {
            JFileChooser seleccionar = new JFileChooser();
            seleccionar.setCurrentDirectory(new File("."));
            seleccionar.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (seleccionar.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                carpetaActual = seleccionar.getSelectedFile();
                iniciarProcesoInteractivos(carpetaActual);
            }
        }
        // LEER ARCHIVO (PESTAÑA 2)
        else if (e.getSource() == btnLeerArchivo) {
            JFileChooser seleccionar = new JFileChooser();
            File dirConversiones = new File(CARPETA_RAIZ_CONVERSIONES);
            if (dirConversiones.exists()) {
                seleccionar.setCurrentDirectory(dirConversiones);
            } else {
                seleccionar.setCurrentDirectory(new File("."));
            }

            seleccionar.setFileSelectionMode(JFileChooser.FILES_ONLY);

            if (seleccionar.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = seleccionar.getSelectedFile();
                try {
                    String contenido = Files.readString(archivoSeleccionado.toPath());
                    areaTextoVisor.setText(contenido);
                    areaTextoVisor.setCaretPosition(0);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        // BOTONES DE CONVERSIÓN
        else if (e.getSource() == btnTXT || e.getSource() == btnCSV || e.getSource() == btnJSON || e.getSource() == btnXML) {
            IConversor conversor = null;
            if (e.getSource() == btnTXT) conversor = new ConversorTXT();
            else if (e.getSource() == btnCSV) conversor = new ConversorCSV();
            else if (e.getSource() == btnJSON) conversor = new ConversorJSON();
            else if (e.getSource() == btnXML) conversor = new ConversorXML();

            if (conversor != null) {
                // Usamos el nombre del código fuente + la hora exacta (Hora, Minuto, Segundo)
                String horaExacta = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
                String nombreBase = nombreArchivoFuente + "_prueba_" + horaExacta;

                String textoFinal = historialEntradasUsuario.toString();

                if (textoFinal.trim().isEmpty()) {
                    textoFinal = "Sin entradas del usuario.";
                }

                String textoConvertido = conversor.convertir(nombreBase, lenguajeActual, textoFinal);
                String subcarpeta = conversor.getExtension().replace(".", "").toUpperCase();

                File dirDestino = new File(CARPETA_RAIZ_CONVERSIONES + File.separator + subcarpeta);
                if (!dirDestino.exists()) dirDestino.mkdirs();

                String rutaFinal = dirDestino.getAbsolutePath() + File.separator + nombreBase + conversor.getExtension();
                if (escritor.guardar(rutaFinal, textoConvertido)) {
                    JOptionPane.showMessageDialog(this,
                            "¡Datos exportados exitosamente!\nGuardado como: " + nombreBase + conversor.getExtension(),
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    // LÓGICA DE EJECUCIÓN BIDIRECCIONAL Y COMANDOS
    private void iniciarProcesoInteractivos(File directorio) {
        btnAbrirCarpeta.setEnabled(false);
        activarBotonesConversor(false);
        areaTextoTerminal.setText("");

        historialConsola.setLength(0);
        historialEntradasUsuario.setLength(0);

        String comando = detectarComando(directorio);
        if (comando == null) {
            areaTextoTerminal.setText("Error: No se encontró un .exe o main.java en esta carpeta.");
            btnAbrirCarpeta.setEnabled(true);
            return;
        }

        areaTextoTerminal.setText("Ejecutando [" + lenguajeActual + "]: " + comando + "\n====================================\n");
        campoEntrada.setEnabled(true);
        campoEntrada.requestFocus();

        Thread hiloEjecucion = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", comando);
                    pb.directory(directorio);
                    pb.redirectErrorStream(true);
                    procesoActual = pb.start();

                    escritorProceso = new BufferedWriter(new OutputStreamWriter(procesoActual.getOutputStream()));
                    InputStreamReader lector = new InputStreamReader(procesoActual.getInputStream());

                    int caracter;
                    while ((caracter = lector.read()) != -1) {
                        char c = (char) caracter;
                        historialConsola.append(c);

                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                areaTextoTerminal.append(String.valueOf(c));
                                areaTextoTerminal.setCaretPosition(areaTextoTerminal.getDocument().getLength());
                            }
                        });
                    }

                    procesoActual.waitFor();
                } catch (Exception ex) {
                    areaTextoTerminal.append("\nError en la ejecucion: " + ex.getMessage());
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            areaTextoTerminal.append("\n====================================\n[Proceso Terminado]");
                            campoEntrada.setEnabled(false);

                            if (lenguajeActual.equals("Java")) {
                                limpiarArchivosClass(directorio);
                            }
                            btnAbrirCarpeta.setEnabled(true);
                            activarBotonesConversor(true);
                        }
                    });
                }
            }
        });
        hiloEjecucion.start();
    }

    private void enviarDatoAlPrograma() {
        try {
            String texto = campoEntrada.getText();
            campoEntrada.setText("");

            if (escritorProceso != null) {
                escritorProceso.write(texto + "\n");
                escritorProceso.flush();

                areaTextoTerminal.append(texto + "\n");
                historialConsola.append(texto).append("\n");
                historialEntradasUsuario.append(texto).append("\n");
            }
        } catch (IOException ex) {
            areaTextoTerminal.append("\nError al enviar dato: " + ex.getMessage());
        }
    }

    private String detectarComando(File directorio) {
        File[] archivos = directorio.listFiles();
        nombreArchivoFuente = "Desconocido";

        if (archivos != null) {
            for (File a : archivos) {
                if (a.getName().toLowerCase().endsWith(".cpp") || a.getName().toLowerCase().endsWith(".java")) {
                    nombreArchivoFuente = a.getName();
                    break;
                }
            }

            for (File a : archivos) {
                if (a.getName().toLowerCase().endsWith(".exe")) {
                    lenguajeActual = "C++";
                    if (nombreArchivoFuente.equals("Desconocido")) nombreArchivoFuente = a.getName();
                    return a.getName();
                }
            }
            for (File a : archivos) {
                if (a.getName().equalsIgnoreCase("main.java") || a.getName().equalsIgnoreCase("Main.java")) {
                    lenguajeActual = "Java";
                    nombreArchivoFuente = a.getName();
                    return "javac *.java && java " + a.getName().replace(".java", "");
                }
            }
        }
        lenguajeActual = "Desconocido";
        return null;
    }
    private void limpiarArchivosClass(File directorio) {
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().endsWith(".class")) {
                    archivo.delete();
                }
            }
        }
    }
}