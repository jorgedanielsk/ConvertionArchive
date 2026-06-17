#include "Grafo.h"
#include <iostream>
#include <fstream>
using namespace std;

// Entidad
Entidad::Entidad()
{
    id = -1;
}

Entidad::Entidad(int _id)
{
    id = _id;
}

int Entidad::getId() const
{
    return id;
}

void Entidad::mostrar() const
{
    cout << "  [" << id << "]\n";
}

Ciudad::Ciudad() : Entidad()
{
    nombre = "";
    estado = "";
}

Ciudad::Ciudad(int _id, string _n, string _e) : Entidad(_id)
{
    nombre = _n;
    estado = _e;
}

string Ciudad::getNombre() const
{
    return nombre;
}

string Ciudad::getEstado() const
{
    return estado;
}

bool Ciudad::estaVacia() const
{
    return id == -1;
}

void Ciudad::mostrar() const
{
    cout << "  [" << id << "] " << nombre << " (" << estado << ")\n";
}

Conexion::Conexion()
{
    idOrigen = -1;
    idDestino = -1;
    activa = false;
}

Conexion::Conexion(int o, int d)
{
    idOrigen = o;
    idDestino = d;
    activa = true;
}

int Conexion::getOrigen() const
{
    return idOrigen;
}

int Conexion::getDestino() const
{
    return idDestino;
}


void Conexion::mostrarConexion(string o, string d) const
{
    cout << "  " << o << " - " << d;
}

AristaGrafo::AristaGrafo(int o, int d) : Conexion(o, d)
{
}

void AristaGrafo::mostrarConexion(string o, string d) const
{
    cout << "  " << o << " <-> " << d;
}

EstructuraGrafo::EstructuraGrafo(string _tipo)
{
    totalC = 0;
    totalA = 0;
    tipo = _tipo;

    for (int i = 0; i < MAX_A; i++)
    {
        aristas[i] = NULL;
    }
}

EstructuraGrafo::~EstructuraGrafo()
{
    for (int i = 0; i < totalA; i++)
    {
        delete aristas[i];
        aristas[i] = NULL;
    }
}

int EstructuraGrafo::getTotalC() const
{
    return totalC;
}

int EstructuraGrafo::getTotalA() const
{
    return totalA;
}

Ciudad EstructuraGrafo::getCiudad(int i) const
{
    return ciudades[i];
}

Conexion* EstructuraGrafo::getArista(int i) const
{
    return aristas[i];
}

int EstructuraGrafo::indicePorId(int id) const
{
    for (int i = 0; i < totalC; i++)
    {
        if (ciudades[i].getId() == id)
        {
            return i;
        }
    }

    return -1;
}

void EstructuraGrafo::agregarCiudad(int id, string n, string e)
{
    if (totalC >= MAX_N)
    {
        cout << "No se pueden agregar mas ciudades.\n";
        return;
    }

    ciudades[totalC] = Ciudad(id, n, e);
    totalC++;
}

bool EstructuraGrafo::eliminarCiudad(int id)
{
    int posicion = indicePorId(id);

    if (posicion == -1)
    {
        return false;
    }

    for (int i = 0; i < totalA; i++)
    {
        if (aristas[i]->getOrigen() == id || aristas[i]->getDestino() == id)
        {
            delete aristas[i];

            for (int j = i; j < totalA - 1; j++)
            {
                aristas[j] = aristas[j + 1];
            }

            totalA--;
            aristas[totalA] = NULL;
            i--;
        }
    }

    for (int i = posicion; i < totalC - 1; i++)
    {
        ciudades[i] = ciudades[i + 1];
    }

    totalC--;
    return true;
}

int EstructuraGrafo::buscarCiudad(string nombre) const
{
    for (int i = 0; i < totalC; i++)
    {
        if (ciudades[i].getNombre() == nombre)
        {
            return i;
        }
    }

    return -1;
}

bool EstructuraGrafo::eliminarArista(int o, int d)
{
    for (int i = 0; i < totalA; i++)
    {
        bool normal = (aristas[i]->getOrigen() == o && aristas[i]->getDestino() == d);
        bool inversa = (aristas[i]->getOrigen() == d && aristas[i]->getDestino() == o);

        if (normal || inversa)
        {
            delete aristas[i];

            for (int j = i; j < totalA - 1; j++)
            {
                aristas[j] = aristas[j + 1];
            }

            totalA--;
            aristas[totalA] = NULL;
            return true;
        }
    }

    return false;
}

void EstructuraGrafo::mostrarCiudades() const
{
    cout << "\nCiudades registradas (" << totalC << "):\n";

    for (int i = 0; i < totalC; i++)
    {
        ciudades[i].mostrar();
    }
}

void EstructuraGrafo::buscarRuta(int idO, int idD) const
{
    int origen = indicePorId(idO);
    int destino = indicePorId(idD);

    if (origen == -1 || destino == -1)
    {
        cout << "No se encontro una de las ciudades.\n";
        return;
    }

    cout << "\nRuta de " << ciudades[origen].getNombre()
         << " a " << ciudades[destino].getNombre() << ":\n";

    for (int i = 0; i < totalA; i++)
    {
        int a = aristas[i]->getOrigen();
        int b = aristas[i]->getDestino();

        if ((a == idO && b == idD) || (a == idD && b == idO))
        {
            cout << "Directa: " << ciudades[origen].getNombre()
                 << " <-> " << ciudades[destino].getNombre();
            return;
        }
    }

    for (int i = 0; i < totalA; i++)
    {
        int a = aristas[i]->getOrigen();
        int b = aristas[i]->getDestino();
        int intermedia = -1;

        if (a == idO)
        {
            intermedia = b;
        }
        else if (b == idO)
        {
            intermedia = a;
        }

        if (intermedia == -1)
        {
            continue;
        }

        for (int j = 0; j < totalA; j++)
        {
            int c = aristas[j]->getOrigen();
            int d = aristas[j]->getDestino();

            if ((c == intermedia && d == idD) || (d == intermedia && c == idD))
            {
                int posIntermedia = indicePorId(intermedia);

                cout << "Con escala en " << ciudades[posIntermedia].getNombre() << ":\n";
                cout << "  " << ciudades[origen].getNombre() << " <-> "
                     << ciudades[posIntermedia].getNombre();
                cout << " " << ciudades[posIntermedia].getNombre() 
				<< " <-> "<< ciudades[destino].getNombre();
                    return;
            }
        }
    }

    cout << "No existe una ruta\n";
}

Grafo::Grafo() : EstructuraGrafo("no_dirigido")
{
}

void Grafo::agregarArista(int o, int d)
{
    if (totalA >= MAX_A)
    {
        cout << "No se pueden agregar mas conexiones\n";
        return;
    }

    aristas[totalA] = new AristaGrafo(o, d);
    totalA++;
}

void Grafo::mostrarEstructura() const
{
    cout << "\nConexiones del grafo:\n";

    if (totalA == 0)
    {
        cout << "No hay conexiones registradas\n";
        return;
    }

    for (int i = 0; i < totalA; i++)
    {
        int origen = indicePorId(aristas[i]->getOrigen());
        int destino = indicePorId(aristas[i]->getDestino());

        aristas[i]->mostrarConexion(ciudades[origen].getNombre(), ciudades[destino].getNombre());
    }
}

void Exportador::exportarTXT(EstructuraGrafo& g)
{
    ofstream f("grafo_datos.txt");

    f << "GRAFO\n\n";
    f << "CIUDADES:\n";

    for (int i = 0; i < g.getTotalC(); i++)
    {
        Ciudad c = g.getCiudad(i);
        f << "  [" << c.getId() << "] " << c.getNombre() << " (" << c.getEstado() << ")\n";
    }

    f << "\nCONEXIONES:\n";

    for (int i = 0; i < g.getTotalA(); i++)
    {
        Conexion* a = g.getArista(i);
        string origen = g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string destino = g.getCiudad(g.indicePorId(a->getDestino())).getNombre();

        f << "  " << origen << " <-> " << destino;
    }

    f.close();
    cout << "Archivo TXT generado.\n";
}

void Exportador::exportarCSV(EstructuraGrafo& g)
{
    ofstream fc("grafo_ciudades.csv");

    fc << "id,nombre,estado\n";

    for (int i = 0; i < g.getTotalC(); i++)
    {
        Ciudad c = g.getCiudad(i);
        fc << c.getId() << "," << c.getNombre() << "," << c.getEstado() << "\n";
    }

    fc.close();

    ofstream fa("grafo_aristas.csv");

    fa << "origen,destino,tipo\n";

    for (int i = 0; i < g.getTotalA(); i++)
    {
        Conexion* a = g.getArista(i);
        string origen = g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string destino = g.getCiudad(g.indicePorId(a->getDestino())).getNombre();

        fa << origen << "," << destino;
    }

    fa.close();
    cout << "Archivos CSV generados.\n";
}

void Exportador::exportarXML(EstructuraGrafo& g)
{
    ofstream f("grafo_datos.xml");

    f << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    f << "<grafo tipo=\"no_dirigido\">\n";
    f << "  <ciudades>\n";

    for (int i = 0; i < g.getTotalC(); i++)
    {
        Ciudad c = g.getCiudad(i);
        f << "    <ciudad><id>" << c.getId() << "</id><nombre>" << c.getNombre()
          << "</nombre><estado>" << c.getEstado() << "</estado></ciudad>\n";
    }

    f << "  </ciudades>\n";
    f << "  <conexiones>\n";

    for (int i = 0; i < g.getTotalA(); i++)
    {
        Conexion* a = g.getArista(i);
        string origen = g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string destino = g.getCiudad(g.indicePorId(a->getDestino())).getNombre();

        f << "    <arista><origen>" << origen << "</origen><destino>" << destino;
    }

    f << "  </conexiones>\n";
    f << "</grafo>\n";

    f.close();
    cout << "Archivo XML generado.\n";
}

void Exportador::exportarJSON(EstructuraGrafo& g)
{
    ofstream f("grafo_datos.json");

    f << "{\n  \"tipo\": \"no_dirigido\",\n  \"ciudades\": [\n";

    for (int i = 0; i < g.getTotalC(); i++)
    {
        Ciudad c = g.getCiudad(i);

        f << "    {\"id\":" << c.getId() << ",\"nombre\":\"" << c.getNombre()
          << "\",\"estado\":\"" << c.getEstado() << "\"}";

        if (i < g.getTotalC() - 1)
        {
            f << ",";
        }

        f << "\n";
    }

    f << "  ],\n  \"conexiones\": [\n";

    for (int i = 0; i < g.getTotalA(); i++)
    {
        Conexion* a = g.getArista(i);
        string origen = g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string destino = g.getCiudad(g.indicePorId(a->getDestino())).getNombre();

        f << "    {\"origen\":\"" << origen << "\",\"destino\":\""
          << destino;

        if (i < g.getTotalA() - 1)
        {
            f << ",";
        }

        f << "\n";
    }

    f << "  ]\n}\n";

    f.close();
    cout << "Archivo JSON generado.\n";
}

void Exportador::exportarTodo(EstructuraGrafo& g)
{
    exportarTXT(g);
    exportarCSV(g);
    exportarXML(g);
    exportarJSON(g);

    cout << "Archivos actualizados\n";
}
