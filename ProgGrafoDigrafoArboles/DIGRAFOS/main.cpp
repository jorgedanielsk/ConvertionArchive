// Version con estilo mas estudiantil

#include <iostream>
#include <string>
#include "Digrafo.h"
using namespace std;

Digrafo    digrafo;
Exportador exportador;

void cargarDatos() {
    digrafo.agregarCiudad(1, "Cancun", "Quintana Roo");
    digrafo.agregarCiudad(2, "Ciudad de Mexico", "CDMX");
    digrafo.agregarCiudad(3, "Guadalajara", "Jalisco");
    digrafo.agregarCiudad(4, "Baja California", "Baja California");
    digrafo.agregarCiudad(5, "Chiapas", "Chiapas");

    digrafo.agregarArista(1, 2);  
    digrafo.agregarArista(2, 3);  
    digrafo.agregarArista(3, 4);  
    digrafo.agregarArista(4, 2);  
    digrafo.agregarArista(2, 5);  
    digrafo.agregarArista(5, 1);  
    exportador.exportarTodo(digrafo);
}

void menu() {
   
    cout<< " DIGRAFO \n";
    cout<< " 1. Ver ciudades\n";
    cout<< " 2. Ver conexion\n";
    cout<< " 3. Buscar ruta\n";
    cout<< " 4. Agregar ciudad/conexion\n";
    cout<< " 5. Eliminar ciudad/conexion     \n";
    cout<< " 6. Buscar ciudad\n";
    cout<< " 7. Exportar TXT   8. Exportar CSV\n";
    cout<< " 9. Exportar XML  10. Exportar JSON\n";
    cout<< " 11. Exportar\n";
    cout<< " 0. Salir\n";
    cout<< " Opcion: ";
}

void opBuscarRuta() {
    digrafo.mostrarCiudades();
    int o, d;
    cout<<"Origen:"; cin>>o;
    cout<<"Destino:"; cin>>d;
    digrafo.buscarRuta(o, d);
}

void opAgregar() {
    int op;
    cout<<"\n 1.Ciudad  2.Conexion  3.Ambos” Opcion: "; cin>>op; cin.ignore();

    if (op==1||op==3) {
        string n, e;
        cout<<"Nombre: "; getline(cin,n);
        cout<<"Estado: "; getline(cin,e);
        int nid=1;
        for (int i=0; i<digrafo.getTotalC(); i++)
            if (digrafo.getCiudad(i).getId()>=nid) nid=digrafo.getCiudad(i).getId()+1;
        digrafo.agregarCiudad(nid,n,e);
        cout<<" Ciudad agregada "<<nid<<".\n";
    }
    if (op==2||op==3) {
        digrafo.mostrarCiudades();
        int o,d,p;
        cout<<"Origen  : "; cin>>o;
        cout<<"Destino : "; cin>>d;
        if (digrafo.indicePorId(o)==-1||digrafo.indicePorId(d)==-1)
            cout<<" No existe\n";
        else { digrafo.agregarArista(o,d); cout<<" Conexion agregada.\n"; }
    }
    exportador.exportarTodo(digrafo);
}

void opEliminar() {
    int op;
    cout<<"\n 1.Ciudad  2.Conexion Opcion: "; cin>>op;
    if (op==1) {
        digrafo.mostrarCiudades();
        int id; cout<<" Eliminar: "; cin>>id;
        cout<<(digrafo.eliminarCiudad(id)?"  Eliminada.\n":" No encontrada.\n");
    } else if (op==2) {
        digrafo.mostrarEstructura();
        int o,d;
        cout<<"Origen: "; cin>>o;
        cout<<"Destino: "; cin>>d;
        cout<<(digrafo.eliminarArista(o,d)
              ?"Conexion eliminada.\n"
              :"No encontrada\n");
    }
    exportador.exportarTodo(digrafo);
}

void opBuscar() {
    string n; cin.ignore();
    cout<<"\nNombre: "; getline(cin,n);
    int idx=digrafo.buscarCiudad(n);
    if (idx!=-1) {
        cout<<" \n"; digrafo.getCiudad(idx).mostrar();
        int idC=digrafo.getCiudad(idx).getId();

        bool hay=false;
        for (int i=0; i<digrafo.getTotalA(); i++) {
            Conexion* a=digrafo.getArista(i);
            if (a->getOrigen()==idC) {
                int iD=digrafo.indicePorId(a->getDestino());
                cout<<" "<<digrafo.getCiudad(iD).getNombre();
                hay=true;
            }
        }
        if (!hay) cout<<"(ninguna)\n";

        cout<<"Entran:\n";
        hay=false;
        for (int i=0; i<digrafo.getTotalA(); i++) {
            Conexion* a=digrafo.getArista(i);
            if (a->getDestino()==idC) {
                int iO=digrafo.indicePorId(a->getOrigen());
                cout<<" "<<digrafo.getCiudad(iO).getNombre();
                hay=true;
            }
        }
        if (!hay) cout<<"(ninguna)\n";

    } else {
        cout<<"No existe\n";
        char r; cout<<" Crear? (s/n): "; cin>>r;
        if (r=='s'||r=='S') {
            cin.ignore();
            string e; cout<<"Estado: "; getline(cin,e);
            int nid=1;
            for (int i=0; i<digrafo.getTotalC(); i++)
                if (digrafo.getCiudad(i).getId()>=nid) nid=digrafo.getCiudad(i).getId()+1;
            digrafo.agregarCiudad(nid,n,e);
            cout<<" Creada ";
            char r2; cout<<"Agregar conexion? (s/n): "; cin>>r2;
            if (r2=='s'||r2=='S') {
                digrafo.mostrarCiudades();
                int d,p; cout<<"Destino: "; cin>>d;
                if (digrafo.indicePorId(d)!=-1) { digrafo.agregarArista(nid,d); }
                else cout<<"No existe.\n";
            }
            exportador.exportarTodo(digrafo);
        }
    }
}

int main()
{
    int op;
    cargarDatos();

    do
    {
        menu();
        cin >> op;

        switch(op)
        {
            case 1:
                digrafo.mostrarCiudades();
                break;

            case 2:
                digrafo.mostrarEstructura();
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
                exportador.exportarTXT(digrafo);
                break;

            case 8:
                exportador.exportarCSV(digrafo);
                break;

            case 9:
                exportador.exportarXML(digrafo);
                break;

            case 10:
                exportador.exportarJSON(digrafo);
                break;

            case 11:
                exportador.exportarTodo(digrafo);
                break;

            case 0:
                cout << "\nSalida...\n";
                break;

            default:
                cout << "\nOpcion no valida.\n";
                break;
        }

    } while(op != 0);

    return 0;
}
