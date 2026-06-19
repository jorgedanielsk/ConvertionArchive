public class Conexion {
    private int nodoInicial;
    private int nodoFinal;
    private String aristaConexion;
    private double tiempo;
    private double costo;

    public Conexion(int nodoInicial, int nodoFinal, String aristaConexion, double tiempo, double costo) {
        this.nodoInicial = nodoInicial;
        this.nodoFinal = nodoFinal;
        this.aristaConexion = aristaConexion;
        this.tiempo = tiempo;
        this.costo = costo;
    }

    public int getNodoInicial() {
        return nodoInicial;
    }

    public int getNodoFinal() {
        return nodoFinal;
    }

    public String getAristaConexion() {
        return aristaConexion;
    }

    public double getTiempo() {
        return tiempo;
    }

    public double getCosto() {
        return costo;
    }
}
