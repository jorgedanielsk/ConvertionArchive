#include <iostream>
#include <string>
#include "Grafo.h"
using namespace std;

Grafo grafo;
Exportador exportador;

void cargarDatos()
{
    grafo.agregarCiudad(1, "Cancun", "Quintana Roo");
    grafo.agregarCiudad(2, "Ciudad de Mexico", "CDMX");
    grafo.agregarCiudad(3, "Guadalajara", "Jalisco");
    grafo.agregarCiudad(4, "Baja California", "Baja California");
    grafo.agregarCiudad(5, "Chiapas", "Chiapas");

    grafo.agregarArista(1, 2);
    grafo.agregarArista(2, 3);
    grafo.agregarArista(3, 4);
    grafo.agregarArista(4, 2);
    grafo.agregarArista(2, 5);
    grafo.agregarArista(1, 5);

    exportador.exportarTodo(grafo);
}

void menu()
{
    cout << "\nMENU\n" << endl;
    cout << "1. Ver ciudades" << endl;
    cout << "2. Ver conexiones" << endl;
    cout << "3. Buscar ruta" << endl;
    cout << "4. Agregar ciudad o conexion" << endl;
    cout << "5. Eliminar ciudad o conexion" << endl;
    cout << "6. Buscar ciudad" << endl;
    cout << "7. Exportar TXT" << endl;
    cout << "8. Exportar CSV" << endl;
    cout << "9. Exportar XML" << endl;
    cout << "10. Exportar JSON" << endl;
    cout << "11. Exportar todo" << endl;
    cout << "0. Salir" << endl;
    cout << "Opcion: ";
}

void opBuscarRuta()
{
    grafo.mostrarCiudades();

    int origen, destino;

    cout << "Origen: ";
    cin >> origen;

    cout << "Destino: ";
    cin >> destino;

    grafo.buscarRuta(origen, destino);
}

void opAgregar()
{
    int opcion;

    cout << "\n1. Ciudad" << endl;
    cout << "2. Conexion" << endl;
    cout << "3. Ambos" << endl;
    cout << "Opcion: ";
    cin >> opcion;
    cin.ignore();

    if (opcion == 1 || opcion == 3)
    {
        string nombre, estado;

        cout << "Nombre: ";
        getline(cin, nombre);

        cout << "Estado: ";
        getline(cin, estado);

        int nuevoId = 1;

        for (int i = 0; i < grafo.getTotalC(); i++)
        {
            if (grafo.getCiudad(i).getId() >= nuevoId)
            {
                nuevoId = grafo.getCiudad(i).getId() + 1;
            }
        }

        grafo.agregarCiudad(nuevoId, nombre, estado);
        cout << "Ciudad agregada con " << nuevoId;
    }

    if (opcion == 2 || opcion == 3)
    {
        grafo.mostrarCiudades();

        int origen, destino;

        cout << "Origen: ";
        cin >> origen;

        cout << "Destino: ";
        cin >> destino;


        if (grafo.indicePorId(origen) == -1 || grafo.indicePorId(destino) == -1)
        {
            cout << "Alguno no existe.\n";
        }
        else
        {
            grafo.agregarArista(origen, destino);
            cout << "Conexion agregada.\n";
        }
    }

    exportador.exportarTodo(grafo);
}

void opEliminar()
{
    int opcion;

    cout << "\n1. Ciudad" << endl;
    cout << "2. Conexion" << endl;
    cout << "Opcion: ";
    cin >> opcion;

    if (opcion == 1)
    {
        grafo.mostrarCiudades();

        int id;
        cout << "Eliminar: ";
        cin >> id;

        if (grafo.eliminarCiudad(id))
        {
            cout << "Ciudad eliminada.\n";
        }
        else
        {
            cout << "No se encontro la ciudad.\n";
        }
    }
    else if (opcion == 2)
    {
        grafo.mostrarEstructura();

        int origen, destino;

        cout << "Origen: ";
        cin >> origen;

        cout << "Destino: ";
        cin >> destino;

        if (grafo.eliminarArista(origen, destino))
        {
            cout << "Conexion eliminada.\n";
        }
        else
        {
            cout << "No se encontro la conexion.\n";
        }
    }

    exportador.exportarTodo(grafo);
}

void opBuscar()
{
    string nombre;

    cin.ignore();
    cout << "\nNombre a buscar: ";
    getline(cin, nombre);

    int posicion = grafo.buscarCiudad(nombre);

    if (posicion != -1)
    {
        cout << "\nCiudad encontrada:\n";
        grafo.getCiudad(posicion).mostrar();

        cout << "Conexiones:\n";

        bool hayConexiones = false;
        int idCiudad = grafo.getCiudad(posicion).getId();

        for (int i = 0; i < grafo.getTotalA(); i++)
        {
            Conexion* conexion = grafo.getArista(i);

            if (conexion->getOrigen() == idCiudad || conexion->getDestino() == idCiudad)
            {
                int otraCiudad;

                if (conexion->getOrigen() == idCiudad)
                {
                    otraCiudad = grafo.indicePorId(conexion->getDestino());
                }
                else
                {
                    otraCiudad = grafo.indicePorId(conexion->getOrigen());
                }

                hayConexiones = true;
            }
        }

        if (!hayConexiones)
        {
            cout << "No tiene conexiones\n";
        }
    }
    else
    {
        cout << "No existe \"" << nombre;

        char respuesta;
        cout << "Deseas crearla? (s/n): ";
        cin >> respuesta;

        if (respuesta == 's' || respuesta == 'S')
        {
            cin.ignore();

            string estado;
            cout << "Estado: ";
            getline(cin, estado);

            int nuevoId = 1;

            for (int i = 0; i < grafo.getTotalC(); i++)
            {
                if (grafo.getCiudad(i).getId() >= nuevoId)
                {
                    nuevoId = grafo.getCiudad(i).getId() + 1;
                }
            }

            grafo.agregarCiudad(nuevoId, nombre, estado);
            cout << "Ciudad creada " << nuevoId;

            char respuestaConexion;
            cout << "Deseas agregar una conexion? (s/n): ";
            cin >> respuestaConexion;

            if (respuestaConexion == 's' || respuestaConexion == 'S')
            {
                grafo.mostrarCiudades();

                int destino;
                cout << "Destino: ";
                cin >> destino;

                if (grafo.indicePorId(destino) != -1)
                {
                    grafo.agregarArista(nuevoId, destino);
                    cout << "Conexion agregada\n";
                }
                else
                {
                    cout << "No existe\n";
                }
            }

            exportador.exportarTodo(grafo);
        }
    }
}

int main()
{
    cout << "\nGRAFO\n";

    cargarDatos();

    int opcion;

    do
    {
        menu();
        cin >> opcion;

        switch (opcion)
        {
            case 1:
                grafo.mostrarCiudades();
                break;

            case 2:
                grafo.mostrarEstructura();
                break;

            case 3:
                opBuscarRuta();
                break;

            case 4:
                opAgregar();
                break;

            case 5:
                opEliminar();
                break;

            case 6:
                opBuscar();
                break;

            case 7:
                exportador.exportarTXT(grafo);
                break;

            case 8:
                exportador.exportarCSV(grafo);
                break;

            case 9:
                exportador.exportarXML(grafo);
                break;

            case 10:
                exportador.exportarJSON(grafo);
                break;

            case 11:
                exportador.exportarTodo(grafo);
                break;

            case 0:
                cout << "\nSalida...\n";
                break;

            default:
                cout << "\nOpcion no valida.\n";
                break;
        }

    } while (opcion != 0);

    return 0;
}
