public class ConversorXML implements IConversor {
    @Override
    public String convertir(String nombreArchivo, String lenguaje, String contenidoOriginal) {
        String[] lineas = contenidoOriginal.split("\n");
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ejecucion>\n");
        xml.append("  <archivo>").append(nombreArchivo).append("</archivo>\n");
        xml.append("  <lenguaje>").append(lenguaje).append("</lenguaje>\n");
        xml.append("  <entradas>\n");
        for (String linea : lineas) {
            String limpia = linea.trim();
            if (!limpia.isEmpty() && !limpia.equals("Sin entradas del usuario.")) {
                limpia = limpia.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
                xml.append("    <dato msj=\"").append(lineas.length).append("\">").append(limpia).append("</dato>\n");
            }
        }
        xml.append("  </entradas>\n</ejecucion>");
        return xml.toString();
    }
    @Override
    public String getExtension() { return ".xml"; }
}