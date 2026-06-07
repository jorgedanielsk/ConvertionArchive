public class ConversorCSV implements IConversor {
    @Override
    public String convertir(String nombreArchivo, String lenguaje, String contenidoOriginal) {
        String[] lineas = contenidoOriginal.split("\n");
        StringBuilder csv = new StringBuilder();
        csv.append("Archivo,Lenguaje,Msj,Consola\n");
        int orden = 1;
        for (String linea : lineas) {
            String limpia = linea.trim();
            if (!limpia.isEmpty() && !limpia.equals("Sin entradas del usuario.")) {
                String escapada = limpia.replace("\"", "\"\"");
                csv.append("\"").append(nombreArchivo).append("\",\"")
                        .append(lenguaje).append("\",")
                        .append(orden).append(",")
                        .append("\"").append(escapada).append("\"\n");
                orden++;
            }
        }
        return csv.toString();
    }
    @Override
    public String getExtension() { return ".csv"; }
}