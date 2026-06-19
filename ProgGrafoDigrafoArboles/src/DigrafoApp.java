public class DigrafoApp extends EstructuraRuta {
    public DigrafoApp() {
        super("Proyecto 02 - Digrafo", true);
        cargarRutaAcapulcoCdmx(true);
    }

    @Override
    protected String rutaDatosDefault(String extension) {
        return "datos/proyecto02_digrafo/datos." + extension;
    }
}
