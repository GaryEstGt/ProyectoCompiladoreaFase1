# ProyectoCompiladoreaFase1
Fase 1: Analizador Lexico
## Instrucciones
--En la primera ejecución al iniciar el form creará el analizador
--Posteriormente le pedirá que abra un archivo con la extensión ".SQL"
--Al seleccionar el archivo sera analizado, y en la misma ventana obtendrá el resultado.
--Tambien tendrá un archivo de extensión .out con el resultado que se guardara en la carpeta del programa.

## Características
El analizador lexico de SQL "MiniSql" es capaz de analizar el lenguaje de sql, devolviendo los token , y su ubicación en el archivo de entrada (linea, columna inicio, columna final). Esto se hace por medio de la herramiento JFlex en el lenguaje de Java. JFlex obtiene como entrada un archivo .flex que contiene la definición de las expresiones regulares del lenguaje a analizar. En este archivo se definió todos los token válidos y tambien de validó ciertos errores que podría contener el archivo .sql. Posteriormente JFLex genera una clase .java encargada de recibir un archivo como entrada y analizarlo, posterior al análisis se interpreta la salida y se genera un archivo .out  con el resultado del análisis, indicando token . linea y columna del token reconocido y errores si los hay.

