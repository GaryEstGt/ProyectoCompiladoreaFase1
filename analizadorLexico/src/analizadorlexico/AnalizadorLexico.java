/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico;

import java.io.File;

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
        File archivo=new File(path);
        generarAnalizador(archivo);
         
        if(archivo.exists()){
         Analizador frame=new Analizador();
          frame.setVisible(true);
        }
        
    }
    public static void generarAnalizador(File archivo){
       
        JFlex.Main.generate(archivo);
                
    }
    
}
