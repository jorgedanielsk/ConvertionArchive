#include <stdio.h>
#include <stdlib.h>
#include "arbol.h"
#include <stdio.h>

NodoArbol* crearNodo(int dato){

    NodoArbol *nuevo;

    nuevo = (NodoArbol*)malloc(
        sizeof(NodoArbol)
    );

    nuevo->dato = dato;
    nuevo->izq = NULL;
    nuevo->der = NULL;

    return nuevo;
}

NodoArbol* insertar(
    NodoArbol *raiz,
    int dato
){

    if(raiz == NULL){
        return crearNodo(dato);
    }

    if(dato < raiz->dato){

        raiz->izq = insertar(
            raiz->izq,
            dato
        );

    }else{

        raiz->der = insertar(
            raiz->der,
            dato
        );
    }

    return raiz;
}

void preorden(NodoArbol *raiz){

    if(raiz != NULL){

        printf("%d ", raiz->dato);

        preorden(raiz->izq);
        preorden(raiz->der);
    }
}

void inorden(NodoArbol *raiz){

    if(raiz != NULL){

        inorden(raiz->izq);

        printf("%d ", raiz->dato);

        inorden(raiz->der);
    }
}

void postorden(NodoArbol *raiz){

    if(raiz != NULL){

        postorden(raiz->izq);
        postorden(raiz->der);

        printf("%d ", raiz->dato);
    }
}
void mostrarArbol(
    NodoArbol *raiz,
    int espacio
){

    if(raiz == NULL)
        return;

    espacio += 5;

    mostrarArbol(
        raiz->der,
        espacio
    );

    printf("\n");

    for(int i = 5; i < espacio; i++){
        printf(" ");
    }

    printf("%d\n",
           raiz->dato);

    mostrarArbol(
        raiz->izq,
        espacio
    );
}
    void escribirTXT(NodoArbol *raiz, FILE *archivo){

    if(raiz == NULL)
        return;

    fprintf(archivo, "%d\n", raiz->dato);

    escribirTXT(raiz->izq, archivo);
    escribirTXT(raiz->der, archivo);
}

void guardarTXT(NodoArbol *raiz){

    FILE *archivo;

    archivo = fopen("arbol.txt", "w");

    if(archivo == NULL){
        printf("Error al crear archivo\n");
        return;
    }

    fprintf(archivo, "ARBOL BINARIO\n\n");

    escribirTXT(raiz, archivo);

    fclose(archivo);

    printf("\nArchivo arbol.txt generado correctamente\n");
}
void escribirXML(
    NodoArbol *raiz,
    FILE *archivo
){

    if(raiz == NULL)
        return;

    fprintf(
        archivo,
        "    <Numero>%d</Numero>\n",
        raiz->dato
    );

    escribirXML(
        raiz->izq,
        archivo
    );

    escribirXML(
        raiz->der,
        archivo
    );
}

void guardarXML(
    NodoArbol *raiz
){

    FILE *archivo;

    archivo = fopen(
        "arbol.xml",
        "w"
    );

    if(archivo == NULL){
        printf("Error al crear XML\n");
        return;
    }

    fprintf(
        archivo,
        "<?xml version=\"1.0\"?>\n"
    );

    fprintf(
        archivo,
        "<Arbol>\n"
    );

    escribirXML(
        raiz,
        archivo
    );

    fprintf(
        archivo,
        "</Arbol>\n"
    );

    fclose(archivo);

    printf(
        "Archivo arbol.xml generado correctamente\n"
    );
}
