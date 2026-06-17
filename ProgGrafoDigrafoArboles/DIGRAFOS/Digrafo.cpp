#include "Digrafo.h"
#include <iostream>
#include <fstream>
using namespace std;

Entidad::Entidad()        { id = -1; }
Entidad::Entidad(int _id) { id = _id; }
int  Entidad::getId() const { return id; }
void Entidad::mostrar() const { cout << "  [" << id << "]\n"; }

Ciudad::Ciudad() : Entidad() { nombre=""; estado=""; }
Ciudad::Ciudad(int _id, string _n, string _e) : Entidad(_id) {
    nombre=_n; estado=_e;
}
string Ciudad::getNombre() const { return nombre; }
string Ciudad::getEstado() const { return estado; }
bool   Ciudad::estaVacia() const { return id==-1; }
void   Ciudad::mostrar()   const {
    cout<<"  ["<<id<<"] "<<nombre<<" ("<<estado<<")\n";
}

Conexion::Conexion() { idOrigen=-1; idDestino=-1; activa=false; }
Conexion::Conexion(int o, int d) {
    idOrigen=o; idDestino=d; activa=true;
}
int  Conexion::getOrigen()  const { return idOrigen; }
int  Conexion::getDestino() const { return idDestino; }
void Conexion::mostrarConexion(string o, string d) const {
    cout<<"  "<<o;
}

AristaDigrafo::AristaDigrafo(int o, int d) : Conexion(o,d) {}
void AristaDigrafo::mostrarConexion(string o, string d) const {
    cout<<"  "<<o;
}

EstructuraGrafo::EstructuraGrafo(string _tipo) {
    totalC=0; totalA=0; tipo=_tipo;
    for (int i=0; i<MAX_A; i++) aristas[i]=NULL;
}
EstructuraGrafo::~EstructuraGrafo() {
    for (int i=0; i<totalA; i++) { delete aristas[i]; aristas[i]=NULL; }
}
int       EstructuraGrafo::getTotalC()     const { return totalC; }
int       EstructuraGrafo::getTotalA()     const { return totalA; }
Ciudad    EstructuraGrafo::getCiudad(int i) const { return ciudades[i]; }
Conexion* EstructuraGrafo::getArista(int i) const { return aristas[i]; }

int EstructuraGrafo::indicePorId(int id) const {
    for (int i=0; i<totalC; i++)
        if (ciudades[i].getId()==id) return i;
    return -1;
}
void EstructuraGrafo::agregarCiudad(int id, string n, string e) {
    if (totalC>=MAX_N) { cout<<"Limite alcanzado\n"; return; }
    ciudades[totalC++]=Ciudad(id,n,e);
}
bool EstructuraGrafo::eliminarCiudad(int id) {
    int idx=indicePorId(id);
    if (idx==-1) return false;
    for (int i=0; i<totalA; i++) {
        if (aristas[i]->getOrigen()==id||aristas[i]->getDestino()==id) {
            delete aristas[i];
            for (int j=i; j<totalA-1; j++) aristas[j]=aristas[j+1];
            aristas[--totalA]=NULL; i--;
        }
    }
    for (int i=idx; i<totalC-1; i++) ciudades[i]=ciudades[i+1];
    totalC--;
    return true;
}
int EstructuraGrafo::buscarCiudad(string nombre) const {
    for (int i=0; i<totalC; i++)
        if (ciudades[i].getNombre()==nombre) return i;
    return -1;
}
bool EstructuraGrafo::eliminarArista(int o, int d) {
    for (int i=0; i<totalA; i++) {
        if (aristas[i]->getOrigen()==o && aristas[i]->getDestino()==d) {
            delete aristas[i];
            for (int j=i; j<totalA-1; j++) aristas[j]=aristas[j+1];
            aristas[--totalA]=NULL;
            return true;
        }
    }
    return false;
}
void EstructuraGrafo::mostrarCiudades() const {
    cout<<"\nCiudades("<<totalC<<"):\n";
    for (int i=0; i<totalC; i++) ciudades[i].mostrar();
}
void EstructuraGrafo::buscarRuta(int idO, int idD) const {
    int iO=indicePorId(idO), iD=indicePorId(idD);
    if (iO==-1||iD==-1) { cout<<"Ciudad no encontrada.\n"; return; }
    cout<<"\nRuta:"<<ciudades[iO].getNombre()<<" "<<ciudades[iD].getNombre()<<"\n";

    for (int i=0; i<totalA; i++) {
        if (aristas[i]->getOrigen()==idO && aristas[i]->getDestino()==idD) {
            cout<<"Directo: "<<ciudades[iO].getNombre()
                <<""<<ciudades[iD].getNombre();
            return;
        }
    }
    for (int i=0; i<totalA; i++) {
        if (aristas[i]->getOrigen()!=idO) continue;
        int medio=aristas[i]->getDestino();
        for (int j=0; j<totalA; j++) {
            if (aristas[j]->getOrigen()==medio && aristas[j]->getDestino()==idD) {
                int iM=indicePorId(medio);
                cout<<"Direccion "<<ciudades[iM].getNombre()<<":\n";
                cout<<" "<<ciudades[iO].getNombre();
                cout<<" "<<ciudades[iM].getNombre();
                return;
            }
        }
    }
    cout<<"No existe\n";
}

Digrafo::Digrafo() : EstructuraGrafo("dirigido") {}
void Digrafo::agregarArista(int o, int d) {
    if (totalA>=MAX_A) { cout<<" Limite alcanzado.\n"; return; }
    aristas[totalA++]=new AristaDigrafo(o,d);
}
void Digrafo::mostrarEstructura() const {
    cout<<"\n GRAFO\n";
    if (!totalA) { cout<<" (sin conexion)\n"; return; }
    for (int i=0; i<totalA; i++) {
        int iO=indicePorId(aristas[i]->getOrigen());
        int iD=indicePorId(aristas[i]->getDestino());
        aristas[i]->mostrarConexion(ciudades[iO].getNombre(), ciudades[iD].getNombre());
    }
}

void Exportador::exportarTXT(EstructuraGrafo& g) {
    ofstream f("digrafo_datos.txt");
    f<<"CIUDADES: \n";
    for (int i=0; i<g.getTotalC(); i++) {
        Ciudad c=g.getCiudad(i);
        f<<"  ["<<c.getId()<<"] "<<c.getNombre()<<" ("<<c.getEstado()<<")\n";
    }
    f<<"\nCONEXION:\n";
    for (int i=0; i<g.getTotalA(); i++) {
        Conexion* a=g.getArista(i);
        string o=g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string d=g.getCiudad(g.indicePorId(a->getDestino())).getNombre();
        f<<"  "<<o<<" "<<d;
    }
    f.close();

}
void Exportador::exportarCSV(EstructuraGrafo& g) {
    ofstream fc("digrafo_ciudades.csv");
    fc<<"id,nombre,estado\n";
    for (int i=0; i<g.getTotalC(); i++) {
        Ciudad c=g.getCiudad(i);
        fc<<c.getId()<<","<<c.getNombre()<<","<<c.getEstado()<<"\n";
    }
    fc.close();
    ofstream fa("digrafo_aristas.csv");
    fa<<"origen,destino,tipo\n";
    for (int i=0; i<g.getTotalA(); i++) {
        Conexion* a=g.getArista(i);
        string o=g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string d=g.getCiudad(g.indicePorId(a->getDestino())).getNombre();
        fa<<o<<","<<d<<","<<a->idDestino<<",dirigido\n";
    }
    fa.close();
    cout<<"digrafo_ciudades.csv  digrafo_aristas.csv\n";
}
void Exportador::exportarXML(EstructuraGrafo& g) {
    ofstream f("digrafo_datos.xml");
    f<<"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<digrafo tipo=\"dirigido\">\n";
    f<<"  <ciudades>\n";
    for (int i=0; i<g.getTotalC(); i++) {
        Ciudad c=g.getCiudad(i);
        f<<"    <ciudad><id>"<<c.getId()<<"</id><nombre>"<<c.getNombre()
         <<"</nombre><estado>"<<c.getEstado()<<"</estado></ciudad>\n";
    }
    f<<"  </ciudades>\n  <conexiones>\n";
    for (int i=0; i<g.getTotalA(); i++) {
        Conexion* a=g.getArista(i);
        string o=g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string d=g.getCiudad(g.indicePorId(a->getDestino())).getNombre();
        f<<"    <arista><origen>"<<o<<"</origen><destino>"<<d;
    f<<"  </conexiones>\n</digrafo>\n";
    f.close();
    cout<<"digrafo_datos.xml\n";
}
}
void Exportador::exportarJSON(EstructuraGrafo& g) {
    ofstream f("digrafo_datos.json");
    f<<"{\n  \"tipo\": \"dirigido\",\n  \"ciudades\": [\n";
    for (int i=0; i<g.getTotalC(); i++) {
        Ciudad c=g.getCiudad(i);
        f<<"    {\"id\":"<<c.getId()<<",\"nombre\":\""<<c.getNombre()
         <<"\",\"estado\":\""<<c.getEstado()<<"\"}";
        if (i<g.getTotalC()-1) f<<",";
        f<<"\n";
    }
    f<<"  ],\n  \"conexiones\": [\n";
    for (int i=0; i<g.getTotalA(); i++) {
        Conexion* a=g.getArista(i);
        string o=g.getCiudad(g.indicePorId(a->getOrigen())).getNombre();
        string d=g.getCiudad(g.indicePorId(a->getDestino())).getNombre();
        f<<"    {\"origen\":\""<<o<<"\",\"destino\":\""<<d;
        if (i<g.getTotalA()-1) f<<",";
        f<<"\n";
    }
    f<<"  ]\n}\n";
    f.close();
}
void Exportador::exportarTodo(EstructuraGrafo& g) {
    exportarTXT(g); exportarCSV(g); exportarXML(g); exportarJSON(g);
}
