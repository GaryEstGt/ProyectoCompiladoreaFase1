# ProyectoCompiladoreaFase1
Fase 1: Analizador Lexico
## Instrucciones
--En la primera ejecución al iniciar el form creará el analizador
--Posteriormente le pedirá que abra un archivo con la extensión ".SQL"
--Al seleccionar el archivo sera analizado, y en la misma ventana obtendrá el resultado.
--Tambien tendrá un archivo de extensión .out con el resultado que se guardara en la carpeta del programa.

## Características
El analizador lexico de SQL "MiniSql" es capaz de analizar el lenguaje de sql, devolviendo los token , y su ubicación en el archivo de entrada (linea, columna inicio, columna final). Esto se hace por medio de la herramiento JFlex en el lenguaje de Java. JFlex obtiene como entrada un archivo .flex que contiene la definición de las expresiones regulares del lenguaje a analizar. En este archivo se definió todos los token válidos y tambien de validó ciertos errores que podría contener el archivo .sql. Posteriormente JFLex genera una clase .java encargada de recibir un archivo como entrada y analizarlo, posterior al análisis se interpreta la salida y se genera un archivo .out  con el resultado del análisis, indicando token . linea y columna del token reconocido y errores si los hay.

Fase 2: Analizador Sintactico

## Características
El analizador lexico de SQL "MiniSql" es capaz de analizar el lenguaje de sql tanto de forma léxica como de forma sintáctica, devolviendo los token , y su ubicación en el archivo de entrada (linea, columna inicio, columna final) y si hay errores los devuelve en la ventana del programa. El análisis sintáctica se hace por medio del análisis Descendente de grámaticas, para este proyecto se definieron las gramáticas del lenguaje de SQL desde su documentación oficial, posteriormente se realizó un método por cada producción de la grámatica, para de esta forma poder realizar el análisis, la entrada para el análisis sintáctica es la lista de tokens reconocida anteriormente por el analizador léxico.

El análizador síntactico tiene la capacidad de reconocer expressiones de SELECT, UPDATE, DELETE E INSERT como parte de instrucciones DML de sql y CREATE, ALTER, DROP Y TRUNCATE como parte de instrucciones DDL.


