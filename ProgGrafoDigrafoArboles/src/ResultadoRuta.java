import java.util.ArrayList;
import java.util.List;

public class ResultadoRuta {
    private List<Integer> camino;
    private double tiempoTotal;
    private double costoTotal;

    public ResultadoRuta() {
        this.camino = new ArrayList<Integer>();
        this.tiempoTotal = -1;
        this.costoTotal = 0;
    }

    public List<Integer> getCamino() {
        return camino;
    }

    public void setCamino(List<Integer> camino) {
        this.camino = new ArrayList<Integer>(camino);
    }

    public double getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(double tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }
}
