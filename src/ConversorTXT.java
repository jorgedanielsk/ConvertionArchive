public class ConversorTXT implements IConversor {
    @Override
    public String convertir(String nombreArchivo, String lenguaje, String contenidoOriginal) {
        return "===================================\n" +
                " REGISTRO DE ENTRADAS DEL USUARIO\n" +
                " Archivo: " + nombreArchivo + "\n" +
                " Lenguaje: " + lenguaje + "\n" +
                "===================================\n\n" +
                contenidoOriginal;
    }
    @Override
    public String getExtension() { return ".txt"; }
}