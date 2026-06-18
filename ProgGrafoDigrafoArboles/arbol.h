#ifndef ARBOL_H
#define ARBOL_H

typedef struct NodoArbol{

    int dato;
    struct NodoArbol *izq;
    struct NodoArbol *der;

}NodoArbol;

NodoArbol* crearNodo(int dato);

NodoArbol* insertar(
    NodoArbol *raiz,
    int dato
);

void preorden(NodoArbol *raiz);

void inorden(NodoArbol *raiz);

void postorden(NodoArbol *raiz);

void mostrarArbol(
    NodoArbol *raiz,
    int espacio
);

void guardarTXT(NodoArbol *raiz);
void escribirTXT(NodoArbol *raiz, FILE *archivo);
void guardarXML(NodoArbol *raiz);
void escribirXML(
    NodoArbol *raiz,
    FILE *archivo
);

#endif
