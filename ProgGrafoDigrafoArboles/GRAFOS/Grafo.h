#ifndef GRAFO_H
#define GRAFO_H

#include <string>
using namespace std;

class Entidad {
public:
    Entidad();
    Entidad(int _id);
    int getId() const;
    virtual void mostrar() const;
    virtual ~Entidad() {}
    int id;
};

class Ciudad : public Entidad {
protected:
    string nombre;
    string estado;

public:
    Ciudad();
    Ciudad(int _id, string _nombre, string _estado);
    string getNombre() const;
    string getEstado() const;
    void mostrar() const;
    bool estaVacia() const;
};

class Conexion {
protected:
    int idOrigen, idDestino;
    bool activa;

public:
    Conexion();
    Conexion(int o, int d);
    int getOrigen() const;
    int getDestino() const;
    virtual void mostrarConexion(string o, string d) const;
    virtual ~Conexion() {}
};

class AristaGrafo : public Conexion {
public:
    AristaGrafo(int o, int d);
    void mostrarConexion(string o, string d) const;
};

class EstructuraGrafo {
protected:
    static const int MAX_N = 50;
    static const int MAX_A = 100;

    Ciudad ciudades[MAX_N];
    Conexion* aristas[MAX_A];
    int totalC, totalA;
    string tipo;

public:
    EstructuraGrafo(string _tipo);
    virtual ~EstructuraGrafo();

    int getTotalC() const;
    int getTotalA() const;
    Ciudad getCiudad(int i) const;
    Conexion* getArista(int i) const;
    int indicePorId(int id) const;

    void agregarCiudad(int id, string nombre, string estado);
    bool eliminarCiudad(int id);
    int buscarCiudad(string nombre) const;

    virtual void agregarArista(int o, int d) = 0;
    bool eliminarArista(int o, int d);

    void mostrarCiudades() const;
    virtual void mostrarEstructura() const = 0;
    void buscarRuta(int idO, int idD) const;
};

class Grafo : public EstructuraGrafo {
public:
    Grafo();
    void agregarArista(int o, int d);
    void mostrarEstructura() const;
};

class Exportador {
public:
    void exportarTXT(EstructuraGrafo& g);
    void exportarCSV(EstructuraGrafo& g);
    void exportarXML(EstructuraGrafo& g);
    void exportarJSON(EstructuraGrafo& g);
    void exportarTodo(EstructuraGrafo& g);
};

#endif
