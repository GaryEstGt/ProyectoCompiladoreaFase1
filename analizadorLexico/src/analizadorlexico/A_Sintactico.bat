SET JAVA_HOME="C:\Program Files\Java\jdk1.8.0_231\bin"
SET PATH=%JAVA_HOME%;%PATH%
SET CLASSPATH=%JAVA_HOME%;
cd C:\Users\garya\ProyectoCompiladoreaFase1\analizadorLexico\src\analizadorlexico
java -jar C:\Users\garya\Desktop\java-cup-11.jar -parser analisis_sintactico -symbols sym sintaxis.cup
pause