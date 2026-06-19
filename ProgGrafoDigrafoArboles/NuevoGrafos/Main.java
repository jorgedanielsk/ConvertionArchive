import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GrafoApp grafo = new GrafoApp();
        DigrafoApp digrafo = new DigrafoApp();
        ArbolApp arbol = new ArbolApp();

        int opcion;
        do {
            System.out.println("\n======================================");
            System.out.println("        TERCER PARCIAL - INTELLIJ");
            System.out.println("        Ruta Acapulco -> CDMX");
            System.out.println("======================================");
            System.out.println("1. Ejecutar Proyecto 01 - Grafo");
            System.out.println("2. Ejecutar Proyecto 02 - Digrafo");
            System.out.println("3. Ejecutar Proyecto 03 - Arbol");
            System.out.println("0. Salir");
            opcion = leerEntero(scanner, "Opcion: ");

            switch (opcion) {
                case 1:
                    grafo.ejecutarMenu(scanner);
                    break;
                case 2:
                    digrafo.ejecutarMenu(scanner);
                    break;
                case 3:
                    arbol.ejecutarMenu(scanner);
                    break;
                case 0:
                    System.out.println("Saliendo del programa. Trabajo listo para ejecutar en IntelliJ IDEA.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido.");
            }
        }
    }
}
