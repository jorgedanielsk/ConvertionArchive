<div align="center">

<img src="https://bs-uploads.toptal.io/blackfish-uploads/components/blog_post_page/5675635/cover_image/regular_1708x683/COVER-de364d79bd4f5604990a2c9f0438c50b.png" width="350" height="200" alt="Logo del Equipo">

# 📚 Estructuras de Datos, Algoritmos y Entorno de Testing

*Una suite completa de estructuras de datos lineales, algoritmos de ordenamiento y una potente herramienta gráfica para su compilación, ejecución y exportación de historiales a formatos universales.*

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![C++](https://img.shields.io/badge/c++-%2300599C.svg?style=for-the-badge&logo=c%2B%2B&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05032.svg?style=for-the-badge&logo=git&logoColor=white)
![Data](https://img.shields.io/badge/JSON_%7C_XML_%7C_CSV-5E5C5C?style=for-the-badge)

</div>

---

## 👨‍💻 Sobre el Proyecto

Este repositorio contiene un ecosistema modularizado diseñado en dos frentes principales:
1. **Estructuras de Datos y Algoritmos:** Gestión de tipos de datos primitivos y objetos complejos (TDA) bajo una arquitectura limpia (Interfaz, Abstracta, Concreta).
2. **Entorno de Testing y Conversión:** Una aplicación gráfica orquestadora que automatiza las pruebas del código fuente y formatea los resultados.

---

## 🚀 Entorno Gráfico de Testing y Conversión de Archivos (NUEVO)

Como núcleo de evaluación del proyecto, se desarrolló una herramienta en **Java Swing** que actúa como una terminal inteligente. Permite evaluar los programas de C++ y Java sin salir de la interfaz, aplicando principios SOLID, concurrencia de hilos (Threads) y manejo avanzado de flujos de E/S (I/O).

### Características Principales:
* 🖥️ **Terminal Interactiva Bidireccional:** A través de un sistema de pestañas (`JTabbedPane`), el usuario puede compilar y ejecutar códigos `.cpp` y `.java`. El subproceso corre en segundo plano y se comunica con la interfaz visual, permitiendo enviar comandos de consola en tiempo real.
* 🧹 **Clean Execution ("No dejar rastro"):** Cuenta con un recolector de basura automatizado que detecta el fin de los procesos e inmediatamente limpia los archivos bytecode (`.class`) generados temporalmente, manteniendo intacto el directorio original del usuario.
* 💾 **Motor de Exportación Universal:** Guarda el historial completo de la interacción del usuario. Genera nombres de archivo dinámicos utilizando Marcas de Tiempo (*Timestamps*) para evitar sobrescrituras y exporta la información estructurada mediante interfaces polimórficas hacia formatos **JSON, XML, CSV y TXT**.
* 📖 **Visor Integrado de Archivos:** Implementa lectura de alto rendimiento utilizando `java.nio.file.Files` para cargar archivos alojados en el disco duro y mostrarlos en un entorno claro de lectura dentro de la misma aplicación.

---

## 🛠️ Estructuras de Datos Implementadas

### 📦 1. Colas (Queues)
Divididas en diferentes enfoques de implementación según la gestión de memoria y el lenguaje:
* 🌱 **ArregloNuevoDato (Java):** Utiliza el tipo de dato personalizado `Producto.java` aplicando la arquitectura Abstracta/Concreta.
* ⚙️ **ArreglosDatoBasico (Java):** Implementación clásica utilizando tipos de datos primitivos (`int`, `double`, etc.).
* 🔗 **Punteros (C++):** Implementación de colas dinámicas manipulando directamente la memoria mediante aritmética de punteros y nodos.

### 📝 2. Listas (Lists)
Incluyen implementaciones de gestión dinámica de elementos en memoria:
* ☕ **Implementación en Java:** Manejo de listas apoyado en la recolección de basura nativa del lenguaje, abarcando arreglos y uso de librerías.
* ⚙️ **Implementación en C++:** Gestión manual de la memoria utilizando punteros para enlazar los elementos y recorrer la estructura, garantizando una administración eficiente de los recursos.

### 🥞 3. Pilas (Stacks)
Implementaciones basadas en el principio **LIFO** (Last In, First Out) para la gestión de estados:
* ⚙️ **Implementación en C++:** Desarrollo enfocado en el alto rendimiento y manejo de referencias en memoria para apilar y desapilar datos de forma controlada y segura mediante el uso de arreglos, librerías y punteros.

---

## ⚡ Algoritmos de Ordenamiento

Ubicados en la carpeta `Ordenamientos` (Java), se incluyen los tres pilares del ordenamiento algorítmico manejando TDAs (`Persona.java`):

1. 🫧 **Bubble Sort (Burbuja):** Método por comparación adyacente simple.
2. 🔀 **Merge Sort:** Algoritmo eficiente de tipo "Divide y Vencerás" con complejidad matemática de $O(n \log n)$.
3. ⏱️ **Quick Sort:** Ordenamiento por partición, optimizado para alto rendimiento en grandes conjuntos de datos.

---

## 🚀 Guías de Ejecución

### 🛠️ Para la Herramienta de Testing y Conversión
1. Dirígete a la carpeta raíz de la herramienta interactiva.
2. Ejecuta la clase `VentanaPrincipal.java` (o `Main.java` si aplica).
3. Selecciona la carpeta que contiene el código que deseas probar (por ejemplo, las carpetas descargables de este repositorio). La herramienta detectará automáticamente si es C++ o Java y lanzará el entorno correspondiente.

### ☕ Para los módulos individuales en Java
1. **Importar en el IDE:** Abre IntelliJ, selecciona `File > Open` y elige la carpeta raíz del algoritmo.
2. **Configuración de Módulos:** Asegúrate de que la carpeta `src` de cada submódulo esté marcada como **Sources Root**.
3. **Correr el programa:** Busca el archivo principal correspondiente al tema, haz clic derecho y selecciona **Run 'Main.java'**.

### ⚙️ Para los módulos en C++ (Punteros)
1. Abre tu terminal o consola de comandos y navega hasta la carpeta del proyecto.
2. Compila los archivos uniendo la implementación y el main:
   ```bash
   g++ main.cpp Estructura.cpp -o programa
