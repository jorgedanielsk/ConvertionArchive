import java.io.FileWriter;
import java.io.IOException;

public class EscritorArchivo {
    public boolean guardar(String rutaAbsoluta, String contenido) {
        try (FileWriter escritor = new FileWriter(rutaAbsoluta)) {
            escritor.write(contenido);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}