import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstructuraRuta {
    protected String nombre;
    protected boolean dirigido;
    protected List<Lugar> nodos;
    protected List<Conexion> conexiones;
    protected LinkedHashSet<String> aristas;

    public EstructuraRuta(String nombre, boolean dirigido) {
        this.nombre = nombre;
        this.dirigido = dirigido;
        this.nodos = new ArrayList<Lugar>();
        this.conexiones = new ArrayList<Conexion>();
        this.aristas = new LinkedHashSet<String>();
    }

    public void limpiar() {
        nodos.clear();
        conexiones.clear();
        aristas.clear();
    }

    public void cargarRutaAcapulcoCdmx(boolean conRutasAlternas) {
        limpiar();

        agregarNodo(0, "Acapulco");
        agregarNodo(1, "Caseta La Venta");
        agregarNodo(2, "Tierra Colorada");
        agregarNodo(3, "Caseta Palo Blanco");
        agregarNodo(4, "Chilpancingo");
        agregarNodo(5, "Caseta Paso Morelos");
        agregarNodo(6, "Puente de Ixtla");
        agregarNodo(7, "Caseta Alpuyeca");
        agregarNodo(8, "Cuernavaca");
        agregarNodo(9, "Caseta Tlalpan");
        agregarNodo(10, "Ciudad de Mexico");

        // Ruta principal Acapulco -> Ciudad de México por la Autopista del Sol.
        // El costo se coloca en el tramo donde se paga la caseta.
        agregarConexion(0, 1, "Acapulco-LaVenta", 20.0, 171.0);
        agregarConexion(1, 2, "LaVenta-TierraColorada", 30.0, 0.0);
        agregarConexion(2, 3, "TierraColorada-PaloBlanco", 20.0, 190.0);
        agregarConexion(3, 4, "PaloBlanco-Chilpancingo", 20.0, 0.0);
        agregarConexion(4, 5, "Chilpancingo-PasoMorelos", 50.0, 209.0);
        agregarConexion(5, 6, "PasoMorelos-PuenteIxtla", 25.0, 0.0);
        agregarConexion(6, 7, "PuenteIxtla-Alpuyeca", 20.0, 100.0);
        agregarConexion(7, 8, "Alpuyeca-Cuernavaca", 25.0, 0.0);
        agregarConexion(8, 9, "Cuernavaca-Tlalpan", 20.0, 156.0);
        agregarConexion(9, 10, "Tlalpan-CDMX", 10.0, 0.0);

        if (conRutasAlternas) {
            agregarConexion(0, 2, "Acapulco-TierraColorada-Libre", 70.0, 0.0);
            agregarConexion(2, 4, "TierraColorada-Chilpancingo-Libre", 70.0, 0.0);
            agregarConexion(4, 6, "Chilpancingo-PuenteIxtla-Libre", 100.0, 0.0);
            agregarConexion(6, 8, "PuenteIxtla-Cuernavaca-Libre", 60.0, 0.0);
            agregarConexion(8, 10, "Cuernavaca-CDMX-Libre", 60.0, 0.0);
        }
    }

    public void agregarNodo(int id, String etiqueta) {
        nodos.add(new Lugar(id, etiqueta));
    }

    public void agregarConexion(int nodoInicial, int nodoFinal, String aristaConexion, double tiempo, double costo) {
        conexiones.add(new Conexion(nodoInicial, nodoFinal, aristaConexion, tiempo, costo));
        aristas.add(aristaConexion);
    }

    public void ejecutarMenu(Scanner scanner) {
        int opcion;
        do {
            System.out.println("\n======================================");
            System.out.println("     " + nombre + " - Ruta Acapulco a CDMX");
            System.out.println("======================================");
            System.out.println("1. Mostrar estructura");
            System.out.println("2. Ruta mas corta con Dijkstra");
            System.out.println("3. Ruta mas larga");
            System.out.println("4. Cargar desde XML");
            System.out.println("5. Cargar desde JSON");
            System.out.println("6. Guardar salidas TXT, CSV, JSON y XML");
            System.out.println("0. Regresar al menu principal");
            opcion = leerEntero(scanner, "Opcion: ");

            switch (opcion) {
                case 1:
                    mostrarEstructura();
                    break;
                case 2:
                    ejecutarDijkstraInteractivo(scanner);
                    break;
                case 3:
                    ejecutarRutaMasLargaInteractiva(scanner);
                    break;
                case 4:
                    cargarXmlInteractivo(scanner);
                    break;
                case 5:
                    cargarJsonInteractivo(scanner);
                    break;
                case 6:
                    guardarSalidas();
                    break;
                case 0:
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    public void mostrarEstructura() {
        System.out.println("\n=== " + nombre.toUpperCase() + " ===");
        System.out.println("Tipo: " + (dirigido ? "Dirigido" : "No dirigido"));
        System.out.println("\nNodos:");
        for (Lugar nodo : nodos) {
            System.out.println("  " + nodo);
        }

        System.out.println("\nAristas:");
        for (String arista : aristas) {
            System.out.println("  " + arista);
        }

        System.out.println("\nConexiones / tramos:");
        for (Conexion c : conexiones) {
            String flecha = dirigido ? " -> " : " -- ";
            System.out.println("  " + c.getNodoInicial() + flecha + c.getNodoFinal()
                    + " [" + c.getAristaConexion() + "]"
                    + " | tiempo: " + formatearNumero(c.getTiempo()) + " min"
                    + " | caseta: $" + formatearNumero(c.getCosto()));
        }
    }

    protected void ejecutarDijkstraInteractivo(Scanner scanner) {
        mostrarNodosParaElegir();
        int origen = leerEntero(scanner, "Nodo origen (0=Acapulco): ");
        int destino = leerEntero(scanner, "Nodo destino (10=Ciudad de Mexico): ");
        mostrarRutaMasCorta(origen, destino);
    }

    protected void mostrarRutaMasCorta(int origen, int destino) {
        int origenIndice = indicePorId(origen);
        int destinoIndice = indicePorId(destino);
        if (origenIndice == -1 || destinoIndice == -1) {
            System.out.println("Origen o destino no encontrado.");
            return;
        }

        ResultadoDijkstra resultado = calcularDijkstra(origen);
        if (resultado.distancias[destinoIndice] >= Double.MAX_VALUE / 8) {
            System.out.println("No existe camino entre " + etiqueta(origen) + " y " + etiqueta(destino) + ".");
            return;
        }

        List<Integer> camino = reconstruirCamino(resultado.anteriores, destinoIndice);
        double costoTotal = calcularCostoCamino(camino);

        System.out.println("\n>>> RUTA MAS CORTA de [" + etiqueta(origen) + "] a [" + etiqueta(destino) + "] <<<");
        System.out.println("Tiempo total: " + formatearNumero(resultado.distancias[destinoIndice]) + " min");
        System.out.println("Costo total de casetas: $" + formatearNumero(costoTotal));
        System.out.println("Camino: " + textoCamino(camino));
        mostrarDetalleTramos(camino);
    }

    protected ResultadoDijkstra calcularDijkstra(int origen) {
        int n = nodos.size();
        double[] dist = new double[n];
        int[] prev = new int[n];
        boolean[] visitado = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Double.MAX_VALUE / 4;
            prev[i] = -1;
            visitado[i] = false;
        }

        int origenIndice = indicePorId(origen);
        dist[origenIndice] = 0;

        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            double mejor = Double.MAX_VALUE / 4;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] < mejor) {
                    mejor = dist[i];
                    u = i;
                }
            }

            if (u == -1) {
                break;
            }

            visitado[u] = true;
            int idActual = nodos.get(u).getId();
            for (Conexion c : conexiones) {
                int vecinoId = -1;
                if (c.getNodoInicial() == idActual) {
                    vecinoId = c.getNodoFinal();
                } else if (!dirigido && c.getNodoFinal() == idActual) {
                    vecinoId = c.getNodoInicial();
                }

                if (vecinoId == -1) {
                    continue;
                }

                int v = indicePorId(vecinoId);
                if (v != -1 && !visitado[v] && dist[u] + c.getTiempo() < dist[v]) {
                    dist[v] = dist[u] + c.getTiempo();
                    prev[v] = u;
                }
            }
        }

        return new ResultadoDijkstra(dist, prev);
    }

    protected void ejecutarRutaMasLargaInteractiva(Scanner scanner) {
        mostrarNodosParaElegir();
        int origen = leerEntero(scanner, "Nodo origen (0=Acapulco): ");
        int destino = leerEntero(scanner, "Nodo destino (10=Ciudad de Mexico): ");
        mostrarRutaMasLarga(origen, destino);
    }

    protected void mostrarRutaMasLarga(int origen, int destino) {
        if (indicePorId(origen) == -1 || indicePorId(destino) == -1) {
            System.out.println("Origen o destino no encontrado.");
            return;
        }

        ResultadoRuta mejor = new ResultadoRuta();
        List<Integer> caminoActual = new ArrayList<Integer>();
        Set<Integer> visitados = new HashSet<Integer>();
        caminoActual.add(origen);
        visitados.add(origen);
        dfsRutaMasLarga(origen, destino, caminoActual, visitados, 0.0, 0.0, mejor);

        System.out.println("\n=== Ruta MAS LARGA de [" + etiqueta(origen) + "] a [" + etiqueta(destino) + "] ===");
        if (mejor.getTiempoTotal() < 0) {
            System.out.println("No existe camino entre ambos nodos.");
            return;
        }

        System.out.println("Tiempo total: " + formatearNumero(mejor.getTiempoTotal()) + " min");
        System.out.println("Costo total de casetas: $" + formatearNumero(mejor.getCostoTotal()));
        System.out.println("Camino: " + textoCamino(mejor.getCamino()));
        mostrarDetalleTramos(mejor.getCamino());
    }

    protected void dfsRutaMasLarga(int actual, int destino, List<Integer> caminoActual,
                                  Set<Integer> visitados, double tiempoActual, double costoActual,
                                  ResultadoRuta mejor) {
        if (actual == destino) {
            if (tiempoActual > mejor.getTiempoTotal()) {
                mejor.setTiempoTotal(tiempoActual);
                mejor.setCostoTotal(costoActual);
                mejor.setCamino(caminoActual);
            }
            return;
        }

        for (Conexion c : conexiones) {
            int vecino = -1;
            double costo = c.getCosto();
            if (c.getNodoInicial() == actual) {
                vecino = c.getNodoFinal();
            } else if (!dirigido && c.getNodoFinal() == actual) {
                vecino = c.getNodoInicial();
            }

            if (vecino == -1 || visitados.contains(vecino)) {
                continue;
            }

            visitados.add(vecino);
            caminoActual.add(vecino);
            dfsRutaMasLarga(vecino, destino, caminoActual, visitados,
                    tiempoActual + c.getTiempo(), costoActual + costo, mejor);
            caminoActual.remove(caminoActual.size() - 1);
            visitados.remove(vecino);
        }
    }

    protected void cargarXmlInteractivo(Scanner scanner) {
        System.out.println("Ejemplo: datos/proyecto01_grafo/datos.xml");
        System.out.print("Archivo XML: ");
        String archivo = scanner.nextLine().trim();
        if (archivo.length() == 0) {
            archivo = rutaDatosDefault("xml");
        }
        try {
            leerDesdeXml(archivo);
            System.out.println("Datos cargados desde XML: " + archivo);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el XML: " + e.getMessage());
        }
    }

    protected void cargarJsonInteractivo(Scanner scanner) {
        System.out.println("Ejemplo: datos/proyecto01_grafo/datos.json");
        System.out.print("Archivo JSON: ");
        String archivo = scanner.nextLine().trim();
        if (archivo.length() == 0) {
            archivo = rutaDatosDefault("json");
        }
        try {
            leerDesdeJson(archivo);
            System.out.println("Datos cargados desde JSON: " + archivo);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el JSON: " + e.getMessage());
        }
    }

    protected String rutaDatosDefault(String extension) {
        return "datos/datos." + extension;
    }

    public void leerDesdeJson(String archivo) throws IOException {
        File file = resolverArchivo(archivo);
        String contenido = leerTexto(file);

        limpiar();

        Pattern nodoPattern = Pattern.compile("\\{\\s*\\\"id\\\"\\s*:\\s*(\\d+)\\s*,\\s*\\\"etiqueta\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*\\}");
        Matcher nodoMatcher = nodoPattern.matcher(contenido);
        while (nodoMatcher.find()) {
            int id = Integer.parseInt(nodoMatcher.group(1));
            String etiqueta = nodoMatcher.group(2);
            agregarNodo(id, etiqueta);
        }

        Pattern conexionPattern = Pattern.compile("\\{\\s*\\\"nodoInicial\\\"\\s*:\\s*(\\d+)\\s*,\\s*\\\"nodoFinal\\\"\\s*:\\s*(\\d+)\\s*,\\s*\\\"aristaConexion\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"\\s*,\\s*\\\"tiempo\\\"\\s*:\\s*([0-9.]+)\\s*,\\s*\\\"costo\\\"\\s*:\\s*([0-9.]+)\\s*\\}");
        Matcher conexionMatcher = conexionPattern.matcher(contenido);
        while (conexionMatcher.find()) {
            int inicio = Integer.parseInt(conexionMatcher.group(1));
            int fin = Integer.parseInt(conexionMatcher.group(2));
            String arista = conexionMatcher.group(3);
            double tiempo = Double.parseDouble(conexionMatcher.group(4));
            double costo = Double.parseDouble(conexionMatcher.group(5));
            agregarConexion(inicio, fin, arista, tiempo, costo);
        }
    }

    public void leerDesdeXml(String archivo) throws Exception {
        File file = resolverArchivo(archivo);
        limpiar();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();

        NodeList listaNodos = document.getElementsByTagName("nodo");
        for (int i = 0; i < listaNodos.getLength(); i++) {
            Element nodo = (Element) listaNodos.item(i);
            int id = Integer.parseInt(texto(nodo, "id"));
            String etiqueta = texto(nodo, "etiqueta");
            agregarNodo(id, etiqueta);
        }

        NodeList listaConexiones = document.getElementsByTagName("conexionNodo2Nodo");
        for (int i = 0; i < listaConexiones.getLength(); i++) {
            Element conexion = (Element) listaConexiones.item(i);
            int inicio = Integer.parseInt(texto(conexion, "nodoInicial"));
            int fin = Integer.parseInt(texto(conexion, "nodoFinal"));
            String arista = texto(conexion, "aristaConexion");
            double tiempo = Double.parseDouble(texto(conexion, "tiempo"));
            double costo = Double.parseDouble(texto(conexion, "costo"));
            agregarConexion(inicio, fin, arista, tiempo, costo);
        }
    }

    protected String texto(Element elemento, String etiqueta) {
        return elemento.getElementsByTagName(etiqueta).item(0).getTextContent();
    }

    public void guardarSalidas() {
        File carpeta = new File("salidas");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = nombre.toLowerCase().replace(" ", "_").replace("-", "_");
        try {
            guardarTxt(new File(carpeta, nombreArchivo + ".txt"));
            guardarCsv(new File(carpeta, nombreArchivo + ".csv"));
            guardarJson(new File(carpeta, nombreArchivo + ".json"));
            guardarXml(new File(carpeta, nombreArchivo + ".xml"));
            System.out.println("Salidas guardadas en la carpeta: salidas");
        } catch (IOException e) {
            System.out.println("No se pudieron guardar las salidas: " + e.getMessage());
        }
    }

    protected void guardarTxt(File archivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(archivo));
        pw.println(nombre + " - Ruta Acapulco a Ciudad de Mexico");
        pw.println("Tipo: " + (dirigido ? "Dirigido" : "No dirigido"));
        pw.println();
        pw.println("NODOS");
        for (Lugar n : nodos) {
            pw.println(n.getId() + " - " + n.getEtiqueta());
        }
        pw.println();
        pw.println("CONEXIONES");
        for (Conexion c : conexiones) {
            pw.println(c.getNodoInicial() + " -> " + c.getNodoFinal()
                    + " | " + c.getAristaConexion()
                    + " | tiempo=" + c.getTiempo()
                    + " | costo=" + c.getCosto());
        }
        pw.close();
    }

    protected void guardarCsv(File archivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(archivo));
        pw.println("nodoInicial,nodoFinal,aristaConexion,tiempo,costo");
        for (Conexion c : conexiones) {
            pw.println(c.getNodoInicial() + "," + c.getNodoFinal() + "," + c.getAristaConexion()
                    + "," + c.getTiempo() + "," + c.getCosto());
        }
        pw.close();
    }

    protected void guardarJson(File archivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(archivo));
        pw.println("{");
        pw.println("  \"grafo\": {");
        pw.println("    \"nodos\": [");
        for (int i = 0; i < nodos.size(); i++) {
            Lugar n = nodos.get(i);
            pw.print("      {\"id\":" + n.getId() + ",\"etiqueta\":\"" + escapar(n.getEtiqueta()) + "\"}");
            pw.println(i < nodos.size() - 1 ? "," : "");
        }
        pw.println("    ],");
        pw.println("    \"conexiones\": [");
        for (int i = 0; i < conexiones.size(); i++) {
            Conexion c = conexiones.get(i);
            pw.print("      {\"nodoInicial\":" + c.getNodoInicial()
                    + ",\"nodoFinal\":" + c.getNodoFinal()
                    + ",\"aristaConexion\":\"" + escapar(c.getAristaConexion()) + "\""
                    + ",\"tiempo\":" + c.getTiempo()
                    + ",\"costo\":" + c.getCosto() + "}");
            pw.println(i < conexiones.size() - 1 ? "," : "");
        }
        pw.println("    ]");
        pw.println("  }");
        pw.println("}");
        pw.close();
    }

    protected void guardarXml(File archivo) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(archivo));
        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<grafo>");
        pw.println("  <nodos>");
        for (Lugar n : nodos) {
            pw.println("    <nodo><id>" + n.getId() + "</id><etiqueta>" + escaparXml(n.getEtiqueta()) + "</etiqueta></nodo>");
        }
        pw.println("  </nodos>");
        pw.println("  <rutas>");
        pw.println("    <conexiones>");
        for (Conexion c : conexiones) {
            pw.println("      <conexionNodo2Nodo>");
            pw.println("        <nodoInicial>" + c.getNodoInicial() + "</nodoInicial><nodoFinal>" + c.getNodoFinal() + "</nodoFinal>");
            pw.println("        <aristaConexion>" + escaparXml(c.getAristaConexion()) + "</aristaConexion>");
            pw.println("        <tiempo>" + c.getTiempo() + "</tiempo><costo>" + c.getCosto() + "</costo>");
            pw.println("      </conexionNodo2Nodo>");
        }
        pw.println("    </conexiones>");
        pw.println("  </rutas>");
        pw.println("</grafo>");
        pw.close();
    }

    protected List<Integer> reconstruirCamino(int[] anteriores, int destinoIndice) {
        List<Integer> camino = new ArrayList<Integer>();
        int actual = destinoIndice;
        while (actual != -1) {
            camino.add(0, nodos.get(actual).getId());
            actual = anteriores[actual];
        }
        return camino;
    }

    protected String textoCamino(List<Integer> camino) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(etiqueta(camino.get(i)));
            if (i < camino.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    protected void mostrarDetalleTramos(List<Integer> camino) {
        System.out.println("\nDetalle por tramos:");
        for (int i = 0; i < camino.size() - 1; i++) {
            int a = camino.get(i);
            int b = camino.get(i + 1);
            Conexion c = buscarConexion(a, b);
            if (c != null) {
                System.out.println("  " + etiqueta(a) + " -> " + etiqueta(b)
                        + " | " + c.getAristaConexion()
                        + " | tiempo: " + formatearNumero(c.getTiempo()) + " min"
                        + " | caseta: $" + formatearNumero(c.getCosto()));
            }
        }
    }

    protected double calcularCostoCamino(List<Integer> camino) {
        double total = 0;
        for (int i = 0; i < camino.size() - 1; i++) {
            Conexion c = buscarConexion(camino.get(i), camino.get(i + 1));
            if (c != null) {
                total += c.getCosto();
            }
        }
        return total;
    }

    protected Conexion buscarConexion(int inicio, int fin) {
        for (Conexion c : conexiones) {
            if (c.getNodoInicial() == inicio && c.getNodoFinal() == fin) {
                return c;
            }
            if (!dirigido && c.getNodoInicial() == fin && c.getNodoFinal() == inicio) {
                return c;
            }
        }
        return null;
    }

    protected int indicePorId(int id) {
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    protected String etiqueta(int id) {
        for (Lugar n : nodos) {
            if (n.getId() == id) {
                return n.getEtiqueta();
            }
        }
        return String.valueOf(id);
    }

    protected void mostrarNodosParaElegir() {
        System.out.println("\nNodos disponibles:");
        for (Lugar n : nodos) {
            System.out.println("  " + n);
        }
    }

    protected int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido.");
            }
        }
    }

    protected File resolverArchivo(String ruta) throws IOException {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            return archivo;
        }

        archivo = new File("datos", ruta);
        if (archivo.exists()) {
            return archivo;
        }

        throw new IOException("No existe el archivo: " + ruta);
    }

    protected String leerTexto(File archivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea).append("\n");
        }
        br.close();
        return sb.toString();
    }

    protected String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    protected String escaparXml(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    protected String formatearNumero(double valor) {
        if (Math.abs(valor - Math.round(valor)) < 0.0001) {
            return String.valueOf((long) Math.round(valor));
        }
        return String.format("%.2f", valor);
    }

    protected Map<Integer, Lugar> mapaNodosOrdenado() {
        Map<Integer, Lugar> mapa = new TreeMap<Integer, Lugar>();
        for (Lugar n : nodos) {
            mapa.put(n.getId(), n);
        }
        return mapa;
    }
}
