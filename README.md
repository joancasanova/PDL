# Procesador de Lenguaje JS-PdL

Este proyecto ha sido desarrollado para la asignatura **Procesadores de Lenguajes** en la **Universidad Politécnica de Madrid** durante el curso 2023-24.

- **Autor**: Juan Francisco Casanova Ferrer
- **Email**: juancasanovaferrer@gmail.com

Para cualquier duda, no dudes en contactar conmigo, estaré encantado de ayudarte.

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

1. **Tabla de Símbolos**
2. **Definir Tokens**
3. **Analizador Léxico**
4. **Analizador Sintáctico**
5. **Analizador Semántico**
6. **Pruebas**
7. **Presentación**

### 0. Tabla de Símbolos

La tabla de símbolos es una estructura esencial que guarda los identificadores (variables y funciones) presentes en el código fuente de JS-PdL.

- Implementación: `src/main/java/tablaSimbolos`

Pasos a seguir:
- Este módulo y sus clases son generales y pueden ser referenciados directamente para el desarrollo de la práctica.

### 1. Definir Tokens

Un token es la unidad léxica más pequeña e indivisible con significado propio. Cada token pertenece a una categoría léxica diferente. Por ejemplo: PALABRARESERVADA, SUMA, o COMPARADOR.

- Implementación: `src/main/java/token`

Pasos a seguir:

0. **Definir los Tokens:** Se deben definir los tokens de la práctica, que dependen de las opciones elegidas.

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

    - `PalabraReservada.java`: Escibir aquí las palabras reservadas.
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

Tan solo se tiene que crear la gramática sintáctica correspondiente a la práctica, y procesarla con bison (se explica a continuación cómo). Para desarrollar el analizador sintáctico, sigue estos pasos:

0. **Crear la gramática del analizador sintáctico**.

1. **Instalar Bison**:

    - En Linux:
        ```bash
        sudo apt update
        sudo apt install bison
        ```
    - En Windows: descarga WSL y ejecuta los comandos desde ahí.

2. **Crear el archivo `gramatica.y`**:

    - Este archivo sirve como input para Bison.
    - **IMPORTANTE**: Los nombres entre paréntesis en `TipoToken` se usarán en `%token`. Incluir también las palabras reservadas. No incluir `$end`.
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
        3. Usar el programa **VASt** para representar el árbol sintáctico (se explicará más adelante cómo usar VASt).

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

La presentación consiste en procesar un fichero de input delante de uno de los profesores. Durante la presentación no se realizan preguntas acerca del proceso de desarrollo, ni se pide que se explique el diseño del procesador, ni conceptos relativos a la asignatura. Esta presentación se reduce exclusivamente a comprobar que los archivos de output son correctos, se genera el árbol sintáctico con VASt, y se han identificado los errores en el input.

En otras palabras, se trata de superar una prueba, el método para superarla es indiferente. A este tipo de procedimiento se le llama pruebas de caja negra.

Una consecuencia directa de este tipo de evaluación es que el código que se ejecute durante esta presentación puede ser el desarrollado por el alumno, o no, ya que no se realiza ningún tipo de comprobación. Y, aunque el código sea realmente desarrollado por el alumno, este puede no seguir ninguno de los principios impartidos en la asignatura y tener un diseño completamente alternativo, o deficiente.

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

## Motivación para el Desarrollo de esta Guía

### Problemática de la Asignatura

**Ratio elevado de suspensos: cuello de botella:**
La asignatura de Procesadores de Lenguajes tiene un alto índice de suspensos, convirtiéndose en un verdadero cuello de botella para los estudiantes de Ingeniería Informática. Este elevado ratio de suspensos puede desmotivar a los alumnos, retrasar su progreso académico, e incluso motivar al avandono del grado.

**Cantidad de trabajo desproporcionada para 3 ECTS:**
La carga de trabajo que se exige a los estudiantes para completar esta asignatura es considerablemente elevada en comparación con los 3 ECTS asignados. Esto genera una desproporción entre el esfuerzo requerido y los créditos obtenidos, profundizando en la sensación de desmotivación y agotamiento. La percepción general, y reflejada en las encuestas, es que el esfuerzo necesario para aprobar esta asignatura no se ve reflejado en los créditos otorgados.

**Profesores que no colaboran:**
La falta de colaboración y apoyo por parte de algunos profesores agrava la situación. En ocasiones, los estudiantes no reciben la orientación y el feedback necesarios para avanzar en sus proyectos, lo que aumenta la frustración y la dificultad para superar la asignatura. En sesiones de tutorías es común recibir un trato poco predispuesto a ayudar, con respuestas vagas a las dudas planteadas y, en algunos casos, incluso con actitud desagradable.

**Falso reparto de trabajo entre compañeros:**
A menudo, los trabajos en grupo presentan un falso reparto de tareas, donde no todos los miembros del grupo contribuyen equitativamente. Esto puede derivar en situaciones no deseadas donde el esfuerzo de unos pocos se diluye en el grupo, afectando negativamente a la evaluación individual. Este problema es exacerbado por la falta de mecanismos efectivos para evaluar la contribución individual dentro de los grupos.

**Corrección en caja negra:**
La corrección de los trabajos se realiza bajo un enfoque de caja negra, es decir, se evalúa únicamente el resultado de la ejecución de una serie de pruebas sin considerar el diseño, los conocimientos reales adquiridos por los estudiantes, ni el esfuerzo invertido en el desarrollo del proyecto. Este tipo de evaluación no refleja adecuadamente las competencias adquiridas en la asignatura y puede resultar negligente para los alumnos que han trabajado diligentemente en su proyecto.

**Falsa concepción de que el examen demuestra conocimientos sobre la asignatura:**
Existe una percepción errónea de que el examen final es un verdadero reflejo del conocimiento y las habilidades adquiridas en la asignatura. Sin embargo, la forma más eficiente de superar el examen y maximizar la nota es aprendiendo los ejercicios tipo y repitiendo las respuestas de memoria, sin una comprensión profunda de los conceptos. Esto no fomenta un aprendizaje significativo ni el desarrollo de competencias reales.

**Problemática alargada en el tiempo:**
Esta situación se ha dilatado en el tiempo y no se ha hecho nada para cambiarlo. La persistencia de estos problemas a lo largo de años sin una intervención adecuada ha perpetuado un entorno académico hostil y a menudo desalentador para los estudiantes.

### Solución Propuesta
Con esta guía se pretende abordar los problemas mencionados, en particular la escasa colaboración de los profesores y la "corrección en caja negra", de modo que si algún alumno la sigue, el profesorado deba asegurarse de que se han comprendido bien los conceptos de la asignatura durante la sesión de presentación. Se espera una reacción constructiva por parte del profesorado de la asignatura en la siguiente dirección:

- **Evaluación del diseño:**

    1. **Realizar preguntas en la sesión de presentación a TODOS los miembros del grupo:**
        Durante la presentación del proyecto, se propone formular preguntas relativas al desarrollo de la práctica a todos los miembros del grupo para asegurar que todos han participado activamente y poseen un entendimiento del trabajo realizado.

    2. **Exposición de los módulos implementados:**
        Los estudiantes deben ser capaces de presentar y explicar los módulos que han implementado, detallando su funcionalidad y su integración en el proyecto global.

    3. **Inspección del código desarrollado:**
        Se propone inspeccionar directamente el código y evaluar en función de si aplica correctamente los conceptos de la asignatura. Esto incluye la implementación de diferentes módulos utilizando la lógica correspondiente y siguiendo los algoritmos presentados en la asignatura.

    4. **Comprobación de que el código utilizado en la exposición está realmente dsarrollado por los alumnos:**
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
