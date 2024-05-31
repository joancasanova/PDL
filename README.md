# Procesador de Lenguaje JS-PdL

Este proyecto ha sido desarrollado para la asignatura **Procesadores de Lenguajes** en la **Universidad Politécnica de Madrid** durante el curso 2023-24.

- **Autor**: Juan Francisco Casanova Ferrer
- **Email**: juancasanovaferrer@gmail.com

Para cualquier duda, no dudes en contactar conmigo, estaré encantado de ayudarte.

## Contenidos

0. [Descripción del Proyecto](#descripción-del-proyecto)
1. [Funcionalidades Implementadas](#funcionalidades-implementadas)
2. [Guía Completa para el Desarrollo de la Práctica - JS-PdL](#guía-completa-para-el-desarrollo-de-la-práctica---js-pdl)
    - [0. Tabla de Símbolos](#0-tabla-de-símbolos)
    - [1. Definir Tokens](#1-definir-tokens)
    - [2. Analizador Léxico](#2-analizador-léxico)
    - [3. Analizador Sintáctico](#3-analizador-sintáctico)
    - [4. Analizador Semántico](#4-analizador-semántico)
    - [5. Pruebas](#5-pruebas)
    - [6. Presentación](#6-presentación)
    - [Visualización de Árboles Sintácticos con VASt](#visualización-de-árboles-sintácticos-con-vast)
3. [Cómo Ejecutar el Proyecto](#cómo-ejecutar-el-proyecto)
4. [Motivación para el Desarrollo de esta Guía](#motivación-para-el-desarrollo-de-esta-guía)
    - [Problemática de la Asignatura](#problemática-de-la-asignatura)
    - [Solución Propuesta](#solución-propuesta)

## Descripción del Proyecto

Este programa implementa un procesador de lenguaje para el lenguaje **JS-PdL**, una variante de JavaScript diseñada específicamente para la práctica de esta asignatura. Cabe destacar que las características de JS-PdL no coinciden al 100% con el estándar de JavaScript, ya que es una versión simplificada de éste.

## Funcionalidades Implementadas

Para esta práctica, se requiere desarrollar una serie de funcionalidades obligatorias y opcionales. Las funcionalidades completas pueden consultarse en la siguiente página: [Funciones JS-PdL](https://dlsiis.fi.upm.es/procesadores/IntroJavaScript.html).

Las funcionalidades opcionales implementadas en este proyecto son:

- **Sentencias**: Sentencia repetitiva (`while`)
- **Operadores especiales**: Asignación con suma (`+=`)
- **Técnicas de Análisis Sintáctico**: Ascendente LR
- **Comentarios**: Comentario de línea (`//`)
- **Cadenas**: Con comillas dobles (`" "`)

## Guía Completa para el Desarrollo de la Práctica - JS-PdL

A continuación se presenta una guía detallada basada en el código de este repositorio para el desarrollo de la práctica.

### Fases del Desarrollo

El desarrollo de la práctica se divide en las siguientes fases:

0. **Tabla de Símbolos**
1. **Definir Tokens**
2. **Analizador Léxico**
3. **Analizador Sintáctico**
4. **Analizador Semántico**
5. **Pruebas**
6. **Presentación**

### 0. Tabla de Símbolos

La tabla de símbolos es una estructura esencial que guarda los identificadores (variables y funciones) presentes en el código fuente de JS-PdL.

- Implementación: `src/main/java/tablaSimbolos`

Pasos a seguir:
- Este módulo y sus clases son generales y pueden ser una referencia directa para el desarrollo de la práctica.

### 1. Definir Tokens

Un token es la unidad léxica más pequeña e indivisible con significado propio. Cada token pertenece a una categoría léxica diferente. Por ejemplo: PALABRARESERVADA, SUMA, o COMPARADOR.

- Implementación: `src/main/java/token`

Pasos a seguir:

0. **Definir los Tokens:** Se deben definir los tokens de la práctica, que dependen de las opciones de que se deben implementar.

1. **Adaptar `TipoToken.java`:**
    - Incluir todos los tokens, incluyendo `FINDEFICHERO`.
    - Especificar, entre paréntesis, al lado del token, el carácter que representa cada token:
        ```java
        SUMA("'+'"),
        NEGACION("'!'")
        ```
    - Si el token está representado por varios caracteres, escribir una palabra que lo represente:
        ```java
        ENTERO("ENTERO"),
        CADENA("CADENA")
        ```
    - Para FINDEFICHERO y PALABRARESERVADA especificarlo así:
        ```java
        FINDEFICHERO("$end"),
        PALABRARESERVADA("")
        ```
    - **NOTA**: Los nombres especificados entre paréntesis se utilizarán posteriormente para generar el archivo `gramatica.y` empleado en el analizador sintáctico y deben coincidir con éste.

### 2. Analizador Léxico

El analizador léxico recibe caracter a caracter hasta generar un token.

- Implementación: `src/main/java/lexico`

Pasos a seguir:

0. **Crear el autómata finito determinista, las acciones semánticas y errores:** Puedes ver un ejemplo en la memoria adjunta en `docs/Memoria.pdf`.

1. **Adaptar Enums** (`src/main/java/lexico/enums`):

    - `PalabraReservada.java`: Escribir aquí las palabras reservadas.
    - `EstadoTransito.java`: Definir los estados de tránsito del autómata. Ejemplos:
        - `SIMBOLOIGUAL`: Estado intermedio entre los tokens `=` ASIGNACION y `==` COMPARADOR.
        - `LEXEMA`: Estado intermedio entre los tokens IDENTIFICADOR y PALABRARESERVADA.
        - **Atención**: Dejar el estado `INICIO`.

    - `EstadoFinal.java`: Definir los estados finales del autómata. Ejemplos:
        - `ABREPARENTESIS`: Corresponde al token `ABREPARENTESIS`.
        - **Atención**: Dejar los estados `PENDIENTE` y `FINDEFICHERO`.

2. **Adaptar `AnalizadorLexico.java`:**

    - Función `debeConsumirCaracter(EstadoFinal estado)`:
        - Incluir todos los estados finales que implican haber leído un carácter extra que al final no se ha utilizado: 
        - Por ejemplo `SUMA (+)`, `NEGACION (!)`, o `ASIGNACION (=)` si existen `+=`, `!=`, y `==`.

3. **Adaptar `GestorEstados.java`:**

    - Función `procesarEstadoInicial(Character charActual)`:
        - Dependiendo del carácter recibido, pasar a un estado final o intermedio.
        - **Atención**: Aquí se gestiona si un identificador puede empezar por barra baja `_`.

    - Función `actualizarEstado(Character charActual, String lexema)`:
        - Adaptar el switch para manejar cada estado de tránsito en el autómata.

4. **Adaptar `GeneradorToken.java`:**

    - Especificar el valor adecuado para los atributos `MAX_CARACTERES_CADENA` y `MAX_VALOR_ENTERO`.
    - Función `generarToken(EstadoFinal estadoFinal, Character charActual, String lexema)`:
        - Adaptar los casos para cada estado final, dejando `PENDIENTE` y `FINDEFICHERO`.

5. **Probar el Analizador Léxico:**

    - En `GestorAnalisis.java` (`src/main/java/main/GestorAnalisis.java`), comentar todo lo relativo a `analizadorSintactico` y `analizadorSemantico`.
    - Comprobar que se generan los tokens adecuadamente:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Comprobar que los tokens en `output/archivoTokens.txt` son correctos.

### 3. Analizador Sintáctico

El analizador sintáctico recibe token a token y genera como output las reglas una a una.

Se ha implementado un analizador sintáctico **ASCENDENTE LR**. Es decir, se aconseja elegir esta opción en la práctica si se desea seguir esta guía.

La tarea principal es crear la gramática sintáctica correspondiente a la práctica y procesarla con bison (se explica a continuación cómo).

0. **Crear la gramática del analizador sintáctico**.

1. **Instalar Bison**:

    - En Linux:
        ```bash
        sudo apt update
        sudo apt install bison
        ```
    - En Windows: descarga WSL y ejecuta los comandos desde ahí, o utiliza una máquina virtual de Linux.

2. **Crear el archivo `gramatica.y`**:

    - Este archivo sirve como input para Bison.
    - **IMPORTANTE**: Los nombres entre paréntesis de `TipoToken` se usarán en `%token`. Incluir también las palabras reservadas. No incluir `$end`.
    - Ejemplo de archivo `.y`:

        ```yacc
        %{
        /* Código C necesario para la integración con el analizador léxico (lexer) */
        #include <stdio.h>
        #include <stdlib.h>

        extern int yylex(void);
        void yyerror(const char *s) {
            fprintf(stderr, "Error: %s\n", s);
        }
        %}

        %token ID ENTERO CADENA PUT GET RETURN IF WHILE LET FUNCTION INT BOOLEAN STRING VOID EQ_OP ADD_OP
        %left '+' EQ_OP ADD_OP
        %right '!'

        %start P

        %%

        P: B P
         | F P
         | /* empty */
         ;

        B: IF '(' E ')' S
         | WHILE '(' E ')' '{' C '}'
         | LET ID T ';'
         | LET ID T '=' E ';'
         | S
         ;

        T: INT
         | BOOLEAN
         | STRING
         ;

        F: F1 '{' C '}'
         ;

        F1: F2 '(' A ')'
         ;

        F2: FUNCTION ID H
         ;

        H: T
         | VOID
         ;

        A: T ID K
         | VOID
         ;

        K: ',' T ID K
         | /* empty */
         ;

        C: B C
         | /* empty */
         ;

        E: E EQ_OP U
         | U
         ;

        U: U '+' V
         | V
         ;

        V: '!' W
         | W
         ;

        W: ID
         | '(' E ')'
         | ID '(' L ')'
         | ENTERO
         | CADENA
         ;

        S: ID '=' E ';'
         | ID ADD_OP E ';'
         | ID '(' L ')' ';'
         | PUT E ';'
         | GET ID ';'
         | RETURN Z ';'
         ;

        L: E Q
         | /* empty */
         ;

        Q: ',' E Q
         | /* empty */
         ;

        Z: E
         | /* empty */
         ;

        %%

        int main(void) {
            return yyparse();
        }
        ```

3. **Generar el archivo `gramatica.output`**:

    - Ejecuta en línea de comandos en el mismo directorio que `gramatica.y`:
        ```bash
        bison -d --verbose gramatica.y -Wcounterexamples
        ```
    - Si se ha generado correctamente, no aparecerán mensajes de error y se creará `gramatica.output`.

4. **Mover `gramatica.output` al directorio `resources`**.

5. **Probar el Analizador Sintáctico**:

    - En `GestorAnalisis.java` (`src/main/java/main/GestorAnalisis.java`), comentar todo lo relativo a `analizadorSemantico`.
    - Comprobar que se generan correctamente las reglas:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Comprobar que las reglas en `output/reglasAplicadas.txt` son correctas.
        3. Usar el programa **VASt** para representar el árbol sintáctico (se explica más adelante cómo usar VASt).

### 4. Analizador Semántico

El analizador semántico procesa las reglas una a una generadas por el analizador sintáctico y comprueba que son correctas.

La lógica del analizador semántico se encuentra encapsulada en la clase `ProcesadorReglas.java`.

Para implementar el analizador semántico, se debe seguir estos pasos:

0. **Adaptar `ProcesadorReglas.java`:**

    - Función `procesarRegla(Integer numeroRegla)`: Crear un caso por cada regla correspondiente a la gramática del analizador sintáctico hecha en el paso anterior.

1. **Adaptar la funcionalidad de cada función** a las reglas correspondientes a las opciones de vuestra práctica.

2. **Adaptar `calcularAncho(Tipo tipo)`** según las especificaciones del enunciado.

3. **Probar el Analizador Semántico**:

    - En `GestorAnalisis.java` (`src/main/java/main/GestorAnalisis.java`), descomentar todo.
    - Comprobar que se genera correctamente la tabla de símbolos:
        1. Crear un archivo `input.txt` y guardarlo en el directorio `input`.
        2. Comprobar que las reglas en `output/archivoTablaSimbolos.txt` son correctas.

### 5. Pruebas

En el directorio `src/test/archivosTest` se han creado una serie de pruebas. Se deben adaptar estas pruebas a las opciones correspondientes a cada práctica. Al ejecutar las pruebas de `AnalizadorTest.java`, se deberán superar todas.

### 6. Presentación

La presentación consiste en procesar un fichero de input. Durante la presentación no se realizan preguntas acerca del proceso de desarrollo, ni se pide que se explique el diseño del procesador, ni se pregunta sobre conceptos relativos a la asignatura. Esta presentación se reduce exclusivamente a comprobar que los archivos de output son correctos, se genera el árbol sintáctico con VASt, y se han identificado los errores en el input.

En otras palabras, se trata de superar una prueba, el método para superarla es indiferente. A este tipo de testing se le llama pruebas de caja negra, sirve para evaluar que se cumplen los requisitos funcionales de un producto sin tener en cuenta su diseño interno.

Una consecuencia directa de este tipo de evaluación es que el código que se ejecute durante esta presentación puede ser el desarrollado por el alumno, o no, ya que no se realiza ningún tipo de comprobación. Y, aunque el código sea realmente desarrollado por el alumno, este puede no seguir ninguno de los principios impartidos en la asignatura y tener un diseño completamente alternativo o deficiente.

### Visualización de Árboles Sintácticos con VASt

0. **Descargar VASt** de la sección de herramientas de la página web de la asignatura: [Herramientas Procesadores](https://dlsiis.fi.upm.es/procesadores/Herramientas.html)

1. **Crear el archivo `gramatica.txt`** que sirve de input para el programa. 

    - Este archivo representa la gramática utilizada para el analizador sintáctico. A continuación se muestra el utilizado para esta práctica:
        
        ```
        NoTerminales = { P1 P E U V W X S L Q Z B T F F1 F2 H A K C }

        Terminales = { == + ! , id ( ) ent cad = ; put get return += if while let int boolean string { } function void $ }

        Axioma = P1

        Producciones = {
            P1 -> P $
            P -> B P
            P -> F P
            P -> lambda
            B -> if ( E ) S
            B -> while ( E ) { C }
            B -> let id T ;
            B -> let id T = E ;
            B -> S
            T -> int
            T -> boolean
            T -> string
            F -> F1 { C }
            F1 -> F2 ( A )
            F2 -> function id H
            H -> T
            H -> void
            A -> T id K
            A -> void
            K -> , T id K
            K -> lambda
            C -> B C
            C -> lambda
            E -> E == U
            E -> U
            U -> U + V
            U -> V
            V -> ! W
            V -> W
            W -> id
            W -> ( E )
            W -> id ( L )
            W -> ent
            W -> cad
            S -> id = E ;	
            S -> id += E ;
            S -> id ( L ) ;
            S -> put E ;
            S -> get id ;
            S -> return Z ;
            L -> E Q
            L -> lambda
            Q -> , E Q
            Q -> lambda
            Z -> E
            Z -> lambda
        }
        ```

2. **Ejecutar el programa (visualizador)**.

3. **Archivo > Abrir archivo Gramática...** > seleccionar el archivo de gramática creado.

4. En la pestaña de **Parse**, poner las reglas generadas por el analizador sintáctico.

5. **Archivo > Generar Árbol**.

## 3. Cómo Ejecutar el Proyecto

Para ejecutar el proyecto usando Gradle, sigue estos pasos:

0. Navega al directorio del proyecto:
    ```sh
    cd /ruta/al/directorio/del/proyecto
    ```

1. Asegúrate de que tienes Gradle instalado. Puedes verificarlo con:
    ```sh
    gradle -v
    ```

2. Compila el proyecto:
    ```sh
    gradle build
    ```

3. Ejecuta el proyecto:
    ```sh
    gradle run
    ```

Asegúrate de que el archivo `input.txt` está en el directorio `input` antes de ejecutar el programa.

Si necesitas más detalles, no dudes en preguntar.

## Motivación para el Desarrollo de esta Guía

### Problemática de la Asignatura

**Ratio elevado de suspensos:**
La asignatura de Procesadores de Lenguajes tiene un alto índice de suspensos, convirtiéndose en un cuello de botella para los estudiantes de Ingeniería Informática. Este elevado ratio de suspensos tiene como consecuencia la desmotivación de los alumnos, el retrasar su progreso académico, e incluso alentar al abandono del grado. En la [página web de la asignatura](https://dlsiis.fi.upm.es/procesadores/FAQ.html) se revela que tan solo el 68% de los "estudiantes que se han esforzado" aprueban la práctica. Esto es una estadística reveladora, ya que implica que más del 30% de los alumnos que se esfuerzan suspenden, es decir, existe un problema si un tercio de los alumnos trabajadores no son capaces de superar la práctica. Esta situación se lleva arrastrando desde hace años y refleja una problemática estructural.

**Cantidad de trabajo desproporcionada para 3 ECTS:**
La carga de trabajo que se exige a los estudiantes para completar esta asignatura es considerablemente elevada en comparación con los 3 ECTS asignados. Esto genera una desproporción entre el esfuerzo requerido y los créditos obtenidos, profundizando en la sensación de desmotivación y agotamiento. La percepción general, y manifestada en las encuestas, es que el esfuerzo necesario para aprobar esta asignatura no se ve reflejado en los créditos otorgados.

**Colaboración deficiente del equipo docente:**
Mientras que es evidente que el equipo docente pone extensas herramientas y recursos a disposición del alumno, como las clases presenciales, clases para las prácticas, o la página web de Draco, estos recursos no atajan la problemática real a la que se enfrentan los alumnos con esta práctica. Debido a las grandes dimensiones de ésta, el reto no solo recae en comprender los conceptos teóricos de la materia, sino también en la organización y el desarrollo de un software no trivial. Respecto a esto último, los alumnos se ven solos. La falta de colaboración y apoyo por parte de algunos profesores agrava la situación. En ocasiones, los estudiantes no reciben la orientación y el feedback necesarios para avanzar en sus proyectos, lo que aumenta la frustración y la dificultad para superar la asignatura. Es también de conocimiento general que en sesiones de tutorías es común recibir un trato poco predispuesto a ayudar, con respuestas vagas a las dudas planteadas y, en algunos casos, incluso se dan comportamientos desagradables.

**Reparto desequilibrado de trabajo entre compañeros:**
A menudo, los trabajos en grupo presentan un reparto desequilibrado de tareas, donde no todos los miembros del grupo contribuyen equitativamente. Esto puede derivar en situaciones no deseadas donde el esfuerzo de unos pocos se diluye en el grupo, afectando negativamente a la evaluación individual. Este problema es exacerbado por la falta de mecanismos efectivos para evaluar la contribución individual dentro de los grupos en esta asignatura. En el caso concreto de esta práctica, este problema se ve acentuado ya que, debido a las dimensiones del proyecto, si no se consigue un reparto equitativo de las tareas, la carga de trabajo pasa a ser desmesurada para el alumno. Este problema es muy común, ya que no es sencillo encontrar compañeros que tengan las habilidades y la predisposición necesarias para abordar la práctica, y es mera cuestión de suerte encontrarlos.

**Corrección que no tiene en cuenta el trabajo realizado:**
La corrección de los trabajos se realiza bajo un enfoque de caja negra, es decir, se evalúa únicamente el resultado de la ejecución de una serie de pruebas sin considerar el diseño, los conocimientos reales adquiridos por los estudiantes, ni el esfuerzo invertido en el desarrollo del proyecto. Este tipo de evaluación no refleja adecuadamente las competencias adquiridas en la asignatura y puede resultar negligente para los alumnos que han trabajado diligentemente en su proyecto.

**Concepción errónea de que el examen demuestra conocimientos sobre la asignatura:**
Existe una percepción errónea de que el examen final demuestra el conocimiento y las habilidades adquiridas en la asignatura. Es cierto que muchos de los alumnos que aprueban el examen sí han adquirido las competencias, pero la forma más eficiente de superar el examen y maximizar la nota es aprendiendo los ejercicios tipo y repitiendo las respuestas de memoria, sin necesariamente tener una comprensión profunda de los conceptos. Esto no fomenta un aprendizaje significativo ni el desarrollo de competencias reales.

**Problemática alargada en el tiempo:**
Esta es una situación que se dilata en el tiempo y que no muestra signos de cambio. La persistencia de estos problemas a lo largo de años sin la intervención adecuada está originando un entorno académico hostil y a menudo desalentador para los estudiantes.

### Solución Propuesta
Con esta guía se pretende abordar y paliar los problemas mencionados. De este modo, los alumnos que acudan a esta guía podrán tener una asistencia extra en el desarrollo de su práctica. Se espera una reacción constructiva por parte del equipo docente de la asignatura, y se proponen las siguientes medidas:

- **Evaluación del diseño:**

    1. **Realizar preguntas en la sesión de presentación a todos los miembros del grupo:**
        Durante la presentación del proyecto, se propone formular preguntas relativas al desarrollo de la práctica a todos los miembros del grupo para asegurar que todos han participado activamente y poseen un entendimiento del trabajo realizado.

    2. **Exposición de los módulos implementados:**
        Los estudiantes deben ser capaces de presentar y explicar los módulos que han implementado, detallando su funcionalidad y su integración en el proyecto global.

    3. **Inspección del código desarrollado:**
        Se propone inspeccionar directamente el código y evaluar en función de si aplica correctamente los conceptos impartidos. Esto incluye la implementación de diferentes módulos utilizando la lógica correspondiente y siguiendo los algoritmos que se enseñan en la asignatura.

    4. **Comprobación de que el código utilizado en la exposición está realmente desarrollado por los alumnos:**
        Es fundamental verificar que el código presentado ha sido desarrollado por los alumnos y no ha sido plagiado, garantizando la integridad académica.

- **Valoración del trabajo acorde a los créditos de la asignatura**
La evaluación del trabajo debe ser proporcional a los créditos asignados a la asignatura (3 ECTS). Esto implica ajustar la carga de trabajo y la complejidad de los proyectos a un nivel razonable, que permita a los estudiantes alcanzar los objetivos de aprendizaje sin una carga excesiva de trabajo. Para valorar el trabajo de los alumnos se puede recurrir a las siguientes herramientas:

    1. **Exposición de retos encontrados en el desarrollo y soluciones aplicadas:**
        Los alumnos deben ser capaces de describir los retos que enfrentaron durante el desarrollo del proyecto y las soluciones que aplicaron, demostrando la dedicación, persistencia, la capacidad para resolver problemas y aplicar los conocimientos teóricos en la práctica.

    2. **Valoración completa y en su conjunto del trabajo realizado:**
        La evaluación debería considerar el trabajo en su totalidad, independientemente de la superación o no de casos de prueba concretos, reconociendo el esfuerzo y el conocimiento aplicados en el desarrollo del proyecto.

---

Esta guía busca no solo facilitar la superación de la asignatura, sino también promover una evaluación más justa y representativa de los conocimientos y habilidades adquiridos por los estudiantes.

---

¡Gracias por tu atención y buena suerte con el desarrollo de tu práctica!

Si necesitas más detalles o quieres sugerir alguna modificación o mejora, no dudes en enviar un correo electrónico a juancasanovaferrer@gmail.com