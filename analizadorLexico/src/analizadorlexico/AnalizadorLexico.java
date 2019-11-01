/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author garya
 */
public class AnalizadorLexico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String path="C:/Users/garya/ProyectoCompiladoreaFase1/analizadorLexico/src/analizadorLexico/Lexer.flex";
        String path2="C:/Users/garya/ProyectoCompiladoreaFase1/analizadorLexico/src/analizadorLexico/lexer2.flex";
        File archivo2=new File(path2);
        File archivo=new File(path);
       // generarSintactico();
            generarAnalizador(archivo2);
          generarAnalizador(archivo);
         
        if(archivo.exists()){
         Analizador frame=new Analizador();
          frame.setVisible(true);
        }
        
    }
    public static void generarAnalizador(File archivo){
       
        JFlex.Main.generate(archivo);
                
    }
    public static void generarSintactico(){
        try {
            String archSintactico = "C:/Users/garya/ProyectoCompiladoreaFase1/analizadorLexico/src/analizadorLexico/sintaxis.cup";
            String[]asintactico={"-parser","sintaxis",archSintactico};
            java_cup.Main.main(asintactico);
        } catch (IOException ex) {
            Logger.getLogger(AnalizadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AnalizadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
