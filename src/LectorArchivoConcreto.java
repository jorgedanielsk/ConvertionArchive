import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class LectorArchivoConcreto implements ILectorArchivo {

    @Override
    public String leerContenido(String rutaAbsoluta) {
        try {
            return Files.readString(Path.of(rutaAbsoluta));
        } catch (IOException e) {
            return "Error al leer el archivo: " + e.getMessage();
        }
    }
}