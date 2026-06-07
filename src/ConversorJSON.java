public class ConversorJSON implements IConversor {
    @Override
    public String convertir(String nombreArchivo, String lenguaje, String contenidoOriginal) {
        String[] lineas = contenidoOriginal.split("\n");
        StringBuilder arregloJSON = new StringBuilder();

        int msjIndex = 1;
        boolean primero = true;

        for (String linea : lineas) {
            String limpia = linea.trim();
            if (!limpia.isEmpty() && !limpia.equals("Sin entradas del usuario.")) {
                if (!primero) arregloJSON.append(",\n");

                arregloJSON.append("    {\n")
                        .append("      \"msj\": ").append(msjIndex).append(",\n")
                        .append("      \"consola\": \"").append(limpia.replace("\"", "\\\"")).append("\"\n")
                        .append("    }");
                msjIndex++;
                primero = false;
            }
        }

        return "{\n" +
                "  \"archivo\": \"" + nombreArchivo + "\",\n" +
                "  \"lenguaje\": \"" + lenguaje + "\",\n" +
                "  \"Resultado de usuario\": [\n" +
                arregloJSON.toString() + "\n" +
                "  ]\n" +
                "}";
    }

    @Override
    public String getExtension() { return ".json"; }
}