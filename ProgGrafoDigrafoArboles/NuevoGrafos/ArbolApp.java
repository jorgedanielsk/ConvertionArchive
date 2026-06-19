public class ArbolApp extends EstructuraRuta {
    private NodoArbol raiz;

    public ArbolApp() {
        super("Proyecto 03 - Arbol", false);
        cargarRutaAcapulcoCdmx(false);
        reconstruirArbol();
    }

    @Override
    public void agregarNodo(int id, String etiqueta) {
        super.agregarNodo(id, etiqueta);
    }

    @Override
    public void cargarRutaAcapulcoCdmx(boolean conRutasAlternas) {
        super.cargarRutaAcapulcoCdmx(false);
        reconstruirArbol();
    }

    @Override
    public void mostrarEstructura() {
        super.mostrarEstructura();
        reconstruirArbol();
        System.out.println("\nRecorrido preorden:");
        preorden(raiz);
        System.out.println("\nRecorrido inorden:");
        inorden(raiz);
        System.out.println("\nRecorrido posorden:");
        posorden(raiz);
        System.out.println();
    }

    @Override
    public void leerDesdeJson(String archivo) throws java.io.IOException {
        super.leerDesdeJson(archivo);
        reconstruirArbol();
    }

    @Override
    public void leerDesdeXml(String archivo) throws Exception {
        super.leerDesdeXml(archivo);
        reconstruirArbol();
    }

    @Override
    protected String rutaDatosDefault(String extension) {
        return "datos/proyecto03_arbol/datos." + extension;
    }

    private void reconstruirArbol() {
        raiz = null;
        for (Lugar lugar : nodos) {
            raiz = insertar(raiz, lugar);
        }
    }

    private NodoArbol insertar(NodoArbol actual, Lugar lugar) {
        if (actual == null) {
            return new NodoArbol(lugar);
        }
        if (lugar.getId() < actual.dato.getId()) {
            actual.izquierdo = insertar(actual.izquierdo, lugar);
        } else if (lugar.getId() > actual.dato.getId()) {
            actual.derecho = insertar(actual.derecho, lugar);
        }
        return actual;
    }

    private void preorden(NodoArbol nodo) {
        if (nodo == null) {
            return;
        }
        System.out.print("  " + nodo.dato);
        preorden(nodo.izquierdo);
        preorden(nodo.derecho);
    }

    private void inorden(NodoArbol nodo) {
        if (nodo == null) {
            return;
        }
        inorden(nodo.izquierdo);
        System.out.print("  " + nodo.dato);
        inorden(nodo.derecho);
    }

    private void posorden(NodoArbol nodo) {
        if (nodo == null) {
            return;
        }
        posorden(nodo.izquierdo);
        posorden(nodo.derecho);
        System.out.print("  " + nodo.dato);
    }
}
