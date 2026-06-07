public interface IConversor {
    String convertir(String nombreArchivo, String lenguaje, String contenidoOriginal);
    String getExtension();
}