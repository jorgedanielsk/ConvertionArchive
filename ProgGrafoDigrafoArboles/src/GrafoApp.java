public class GrafoApp extends EstructuraRuta {
    public GrafoApp() {
        super("Proyecto 01 - Grafo", false);
        cargarRutaAcapulcoCdmx(true);
    }

    @Override
    protected String rutaDatosDefault(String extension) {
        return "datos/proyecto01_grafo/datos." + extension;
    }
}
