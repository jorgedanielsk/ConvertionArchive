public class Lugar {
    private int id;
    private String etiqueta;

    public Lugar(int id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public int getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + etiqueta;
    }
}
