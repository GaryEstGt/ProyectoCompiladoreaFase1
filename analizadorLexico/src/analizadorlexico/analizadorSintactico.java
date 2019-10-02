/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico;

import java.util.LinkedList;

/**
 *
 * @author garya
 */
public class analizadorSintactico {
    public LinkedList<Token> instrucciones=new LinkedList();
    public String errores="";
    int posAnalizador=0;
    boolean finAnalisis=false;
    analizadorSintactico(LinkedList<Token> instrucciones){
        this.instrucciones=instrucciones;
        sintacticoGeneral();
    }
    public final void sintacticoGeneral(){
       while(posAnalizador<instrucciones.size()-1){
          
        switch(instrucciones.get(posAnalizador).getToken()){
            case "SELECT":
                selectStatement();
            break;
            case "INSERT":
                insertStatement();
            break;
            case "UPDATE":
                updateStatement();
            break;
            case "DELETE":
                deleteStatement();
            break;
            case "CREATE":
                createStatement();
            break;
            case "ALTER":
                alterStatement();
            break;
            case "DROP":
                dropStatement();
            break;
            case "TRUNCATE":
                truncateStatement();
            break;
            default:
                errores+="error de sintaxis, se esperaba sentencia";
                recorrerFinal();
                break;
        }
    }
    }
    public void recorrerFinal(){
        boolean findFinal=false;
        while(!findFinal){
           if(posAnalizador<(instrucciones.size()-1)){
            if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa") ||instrucciones.get(posAnalizador).getToken().equals("GO") ){
                posAnalizador++;
                findFinal=true;
            }else{
                posAnalizador++;
            }
           }else{
               findFinal=true;
           }
        }
    }
    public boolean MatchToken(Token tokenIns, String tokenEsperado){
        boolean error=false;
        if(tokenIns.getToken().equals(tokenEsperado)){
            if(posAnalizador<(instrucciones.size()-1)){
                posAnalizador++;
            }       
        }else{
            errores+="Error de sintaxis se esperaba: " +tokenEsperado+" en la linea: "+tokenIns.getLinea()+"\n";
            recorrerFinal();
            error=true;
        }
        return error;
    }
    public boolean selectStatement(){ //evalua SELECT
        boolean salirMetodo=false;
       if(instrucciones.get(posAnalizador).getToken().equals("SELECT")){
        salirMetodo= MatchToken(instrucciones.get(posAnalizador),"SELECT"); //viene SELECT
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=selState();                                             //empieza argumento de select
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=selAction();                                            //empieza el from
        if(salirMetodo){ return salirMetodo;}       
         if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
       }
       else if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
          salirMetodo= MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto"); //primer parentesis
          if(salirMetodo){ return salirMetodo;} 
          salirMetodo=selectStatement();                                //entrar al Select otra vez
          if(salirMetodo){ return salirMetodo;} 
          salirMetodo= MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");// cerrar parentesis
          if(salirMetodo){ return salirMetodo;}
       }else{
           errores+="Error de sintaxis se esperaba SELECT o ( en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
           recorrerFinal();
           salirMetodo=true;
           return salirMetodo;
       }
       return salirMetodo;
    }
    public boolean selState(){ //evalua argumento de SELECT
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("ALL") || instrucciones.get(posAnalizador).getToken().equals("DISTINCT")){
            salirMetodo=Option(); //si viene ALL O DISTINCT
            if(salirMetodo){ return salirMetodo;} 
            if(instrucciones.get(posAnalizador).getToken().equals("TOP")){ //evalue si hay TOP
                 salirMetodo=TOP();//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
            }
            salirMetodo=selectExpression(); //ejecuta select expresion
            if(salirMetodo){ return salirMetodo;} 
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("TOP")){ //Si no viene ALL O DISTINCT y viene TOP
            salirMetodo=TOP();//ejecuta TOP
            if(salirMetodo){ return salirMetodo;}
            salirMetodo=selectExpression();//ejecuta Select expresion
            if(salirMetodo){ return salirMetodo;} 
        }else{
            salirMetodo=selectExpression(); //ejecuta Select expresion si no viene ni option ni top
            if(salirMetodo){ return salirMetodo;} 
        }
        return salirMetodo;
    }
    public boolean Option(){
         boolean salirMetodo=false;
         if(instrucciones.get(posAnalizador).getToken().equals("ALL")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ALL");    //si viene ALL
              if(salirMetodo){ return salirMetodo;} 
         }else if(instrucciones.get(posAnalizador).getToken().equals("DISTINCT")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DISTINCT");//si viene DISTINCT
              if(salirMetodo){ return salirMetodo;}
         }
        return salirMetodo;
    }
    public boolean TOP(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TOP");   
        if(salirMetodo){ return salirMetodo;} 
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");    
        if(salirMetodo){ return salirMetodo;} 
        salirMetodo=expression();   
        if(salirMetodo){ return salirMetodo;} 
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");    
        if(salirMetodo){ return salirMetodo;} 
        
        if(instrucciones.get(posAnalizador).getToken().equals("PERCENT")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PERCENT");   
        }
        return salirMetodo;
    }
    public boolean selAction(){
        boolean salirMetodo=false;
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FROM");;
            if(salirMetodo){ return salirMetodo;}
            salirMetodo=table_exp();
            if(salirMetodo){ return salirMetodo;}
           if(instrucciones.get(posAnalizador).getToken().equals("WHERE")){
                salirMetodo=sigExp();
               if(salirMetodo){ return salirMetodo;}              
           }
            if(instrucciones.get(posAnalizador).getToken().equals("GROUP")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GROUP");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BY");    
                if(salirMetodo){ return salirMetodo;}
                salirMetodo=group_state();
                if(salirMetodo){ return salirMetodo;}
                
           }
         if(instrucciones.get(posAnalizador).getToken().equals("HAVING")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"HAVING");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=search_condition_having();
                if(salirMetodo){ return salirMetodo;}
           }
         if(instrucciones.get(posAnalizador).getToken().equals("ORDER")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ORDER");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BY");    
                if(salirMetodo){ return salirMetodo;}
                salirMetodo=order_exp();
                if(salirMetodo){ return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("ASC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ASC");    
                    if(salirMetodo){ return salirMetodo;} 
                }
                else if(instrucciones.get(posAnalizador).getToken().equals("DESC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DESC");    
                    if(salirMetodo){ return salirMetodo;} 
                }
           }       
           
        return salirMetodo; 
    }
    public boolean sigExp(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WHERE");    
        if(salirMetodo){ return salirMetodo;}
         salirMetodo=search_condition();
         if(salirMetodo){ return salirMetodo;}           
        return salirMetodo;
    }
    public boolean group_state(){
        boolean salirMetodo=false;
        
                    if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                        salirMetodo=identExp();
                        if(salirMetodo){ return salirMetodo;}
                    }
                    else if(instrucciones.get(posAnalizador).getToken().equals("CASE")){
                        salirMetodo=caseState();
                        if(salirMetodo){ return salirMetodo;}
                    }
                    else if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");    
                        if(salirMetodo){ return salirMetodo;}
                        salirMetodo=group_state();
                        if(salirMetodo){ return salirMetodo;}
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");    
                        if(salirMetodo){ return salirMetodo;}
                    }
                    else{
                      errores+="Error de sintaxis se esperaba groupby expression en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
                    }
                    if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");        
                            if(salirMetodo){ return salirMetodo;}
                            salirMetodo=group_state();
                            if(salirMetodo){ return salirMetodo;}
                   }
        return salirMetodo;
    }
    public boolean order_exp(){
        boolean salirMetodo=false;
            
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "ParentesisAbierto":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){ return salirMetodo;}
                salirMetodo=order_exp();
                if(salirMetodo){ return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){ return salirMetodo;}
                break;
            case "Identificador":
                salirMetodo=identExp();
                if(salirMetodo){ return salirMetodo;}
                break;
            case "COUNT":
            case "SUM":
            case "AVG":
            case "MAX":
            case "MIN":
                 if(instrucciones.get(posAnalizador).getToken().equals("COUNT")){
                    MatchToken(instrucciones.get(posAnalizador),"COUNT");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MAX")){
                    MatchToken(instrucciones.get(posAnalizador),"MAX");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MIN")){
                    MatchToken(instrucciones.get(posAnalizador),"MIN");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("AVG")){
                    MatchToken(instrucciones.get(posAnalizador),"AVG");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("SUM")){
                    MatchToken(instrucciones.get(posAnalizador),"SUM");
                }
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=count_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
             case "CASE":
                salirMetodo=caseState();
                if(salirMetodo){ return salirMetodo;}
                break;
            default:
                errores+="Error de sintaxis se esperaba orderby expression en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
            if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");        
                            if(salirMetodo){ return salirMetodo;}
                            salirMetodo=order_exp();
                            if(salirMetodo){ return salirMetodo;}
                   }
        return salirMetodo;
    }
    public boolean table_exp(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=table_exp();
            if(salirMetodo){ return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
            if(salirMetodo){return salirMetodo;}
        }else{
            salirMetodo=identExp();
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            
        }
          salirMetodo=join_table();
         if(salirMetodo){return salirMetodo;}                             
        return salirMetodo;
    }
    public boolean join_table(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=join_table();
            if(salirMetodo){ return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
            if(salirMetodo){return salirMetodo;}
        }else{
        if(instrucciones.get(posAnalizador).getToken().equals("FULL")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FULL");
            if(salirMetodo){return salirMetodo;}  
            if(instrucciones.get(posAnalizador).getToken().equals("OUTER")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OUTER");
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"JOIN");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=identExp();
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=on_join();
             if(salirMetodo){return salirMetodo;}
             if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")||instrucciones.get(posAnalizador).getToken().equals("FULL")||instrucciones.get(posAnalizador).getToken().equals("RIGHT")||instrucciones.get(posAnalizador).getToken().equals("LEFT")||instrucciones.get(posAnalizador).getToken().equals("INNER")){
                salirMetodo=join_table();
                if(salirMetodo){return salirMetodo;}
              } 
               
        }else if(instrucciones.get(posAnalizador).getToken().equals("LEFT")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"LEFT");
            if(salirMetodo){return salirMetodo;}  
            if(instrucciones.get(posAnalizador).getToken().equals("OUTER")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OUTER");
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"JOIN");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=identExp();
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=on_join();
             if(salirMetodo){return salirMetodo;}    
               if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")||instrucciones.get(posAnalizador).getToken().equals("FULL")||instrucciones.get(posAnalizador).getToken().equals("RIGHT")||instrucciones.get(posAnalizador).getToken().equals("LEFT")||instrucciones.get(posAnalizador).getToken().equals("INNER")){
                salirMetodo=join_table();
                if(salirMetodo){return salirMetodo;}
              }  
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("RIGHT")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"RIGHT");
            if(salirMetodo){return salirMetodo;}  
            if(instrucciones.get(posAnalizador).getToken().equals("OUTER")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OUTER");
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"JOIN");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=identExp();
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=on_join();
             if(salirMetodo){return salirMetodo;}    
                if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")||instrucciones.get(posAnalizador).getToken().equals("FULL")||instrucciones.get(posAnalizador).getToken().equals("RIGHT")||instrucciones.get(posAnalizador).getToken().equals("LEFT")||instrucciones.get(posAnalizador).getToken().equals("INNER")){
                salirMetodo=join_table();
                if(salirMetodo){return salirMetodo;}
              }  
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("INNER")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INNER");
            if(salirMetodo){return salirMetodo;}  
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"JOIN");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=identExp();
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=on_join();
             if(salirMetodo){return salirMetodo;}    
             if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")||instrucciones.get(posAnalizador).getToken().equals("FULL")||instrucciones.get(posAnalizador).getToken().equals("RIGHT")||instrucciones.get(posAnalizador).getToken().equals("LEFT")||instrucciones.get(posAnalizador).getToken().equals("INNER")){
                salirMetodo=join_table();
                if(salirMetodo){return salirMetodo;}
              } 
        }
        }
        return salirMetodo;
    }
    public boolean on_join(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
              if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");
               if(salirMetodo){ return salirMetodo;}
               salirMetodo=identExp();
                if(salirMetodo){ return salirMetodo;}
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                 if(salirMetodo){ return salirMetodo;}
                 salirMetodo=identExp();
                 if(salirMetodo){ return salirMetodo;}
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
        }else if(instrucciones.get(posAnalizador).getToken().equals("ON")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");
               if(salirMetodo){ return salirMetodo;}
               salirMetodo=identExp();
                if(salirMetodo){ return salirMetodo;}
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                 if(salirMetodo){ return salirMetodo;}
                 salirMetodo=identExp();
                if(salirMetodo){return salirMetodo;}
        }
        else{
                errores+="Error se esperaba ON o ( en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo; 
        }
        return salirMetodo;
    }
    public boolean selectExpression(){
        boolean salirMetodo=false;
        boolean vieneAsterisco=false;
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "Multiplicacion":
                MatchToken(instrucciones.get(posAnalizador),"Multiplicacion");
                vieneAsterisco=true;
                break;
            case "CASE":
                salirMetodo=caseState();
                if(salirMetodo){ return salirMetodo;}
                break;
            case "String":
                MatchToken(instrucciones.get(posAnalizador),"String");
                break;
            case "CAST":
                MatchToken(instrucciones.get(posAnalizador),"CAST");
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=cast_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "CONVERT":
                MatchToken(instrucciones.get(posAnalizador),"CONVERT");
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=convert_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "COUNT":
            case "MAX":
            case "MIN":
            case "SUM":
            case "AVG":
                if(instrucciones.get(posAnalizador).getToken().equals("COUNT")){
                    MatchToken(instrucciones.get(posAnalizador),"COUNT");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MAX")){
                    MatchToken(instrucciones.get(posAnalizador),"MAX");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MIN")){
                    MatchToken(instrucciones.get(posAnalizador),"MIN");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("AVG")){
                    MatchToken(instrucciones.get(posAnalizador),"AVG");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("SUM")){
                    MatchToken(instrucciones.get(posAnalizador),"SUM");
                }
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=count_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "Identificador":
            case "Numero":
            case "Float":
            case "Bit":
            case "ParentesisAbierto":
                salirMetodo=expression();
                if(salirMetodo){return salirMetodo;}
                break;
            default:
                errores+="Error se esperaba columna, funcion o expresion en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
            if(!vieneAsterisco){
                if(instrucciones.get(posAnalizador).getToken().equals("AS")){
                        MatchToken(instrucciones.get(posAnalizador),"AS");    
                    if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                        MatchToken(instrucciones.get(posAnalizador),"Identificador");   
                    }
                    else if(instrucciones.get(posAnalizador).getToken().equals("String")){
                        MatchToken(instrucciones.get(posAnalizador),"String");   
                    }
                } 
            }

                if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    MatchToken(instrucciones.get(posAnalizador),"Coma");   
                     salirMetodo=selectExpression();
                     if(salirMetodo){ return salirMetodo;}
            }
           
        return salirMetodo;
    }
    public boolean identExp(){
        boolean salirMetodo=false;
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");  
            if(salirMetodo){return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
                    MatchToken(instrucciones.get(posAnalizador),"Punto");   
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");  
                    if(salirMetodo){return salirMetodo;}
                        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
                            MatchToken(instrucciones.get(posAnalizador),"Punto");   
                             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");  
                        if(salirMetodo){return salirMetodo;}
            }
            }
        
        return salirMetodo;
    }
    public boolean caseState(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("CASE")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CASE");  
             if(salirMetodo){return salirMetodo;}
             salirMetodo=when_list();
             if(salirMetodo){return salirMetodo;}
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"END");  
             if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
            MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
            salirMetodo=caseState();
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
            if(salirMetodo){return salirMetodo;}
        }

        return salirMetodo;
    }
    public boolean cast_exp(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("CAST")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CAST");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=cast_exp();
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
            if(salirMetodo){return salirMetodo;}
        }else{
               salirMetodo=expression();
              if(salirMetodo){return salirMetodo;}
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");
              if(salirMetodo){return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("INT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INT");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("FLOAT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FLOAT");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("BIT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BIT");
                 if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("STRING")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"STRING");
                if(salirMetodo){return salirMetodo;}
            }else{
                errores+="Error se esperaba tipo de dato en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
            }
        }
        
        return salirMetodo;
    }
    public boolean state_logic(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("NOT") || instrucciones.get(posAnalizador).getToken().equals("BETWEEN")){
            if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BETWEEN");
            if(salirMetodo){return salirMetodo;}
             salirMetodo=selectExpression();
             if(salirMetodo){ return salirMetodo;}
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AND");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=selectExpression();
             if(salirMetodo){ return salirMetodo;}
            
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("IS")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IS");
            if(salirMetodo){ return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NULL");
            if(salirMetodo){return salirMetodo;}
        }
        else{
            if(instrucciones.get(posAnalizador).getToken().equals("Igual")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("NoIgual")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NoIgual");
                if(salirMetodo){return salirMetodo;}
            }           
            else if(instrucciones.get(posAnalizador).getToken().equals("Mayor")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Mayor");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("MayorIgual")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"MayorIgual");
                if(salirMetodo){return salirMetodo;}

            }
            else if(instrucciones.get(posAnalizador).getToken().equals("Menor")){
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Menor");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("MenorIgual")){
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"MenorIgual");
                if(salirMetodo){return salirMetodo;}
            }else{
                errores+="Error se esperaba operador logico en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
            }
            salirMetodo=expression();
            if(salirMetodo){return salirMetodo;}
        }
        
           
        return salirMetodo;
    }
    public boolean cont_exp(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
          salirMetodo=identExp();
           if(salirMetodo){return salirMetodo;} 
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
            if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Multiplicacion")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Multiplicacion");
           if(salirMetodo){return salirMetodo;} 
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
            if(salirMetodo){return salirMetodo;}
        }else{
              errores+="Error se esperaba columna o * en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
        return salirMetodo;
    }
    public boolean convert_exp(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("CONVERT")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CONVERT");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=convert_exp();
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
            if(salirMetodo){return salirMetodo;}
        }else{
            if(instrucciones.get(posAnalizador).getToken().equals("INT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INT");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("FLOAT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FLOAT");
                if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("BIT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BIT");
                 if(salirMetodo){return salirMetodo;}
            }
            else if(instrucciones.get(posAnalizador).getToken().equals("STRING")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"STRING");
                if(salirMetodo){return salirMetodo;}
            }else{
                errores+="Error se esperaba tipo de dato en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
            }
            if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Numero");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
             if(salirMetodo){return salirMetodo;}
             salirMetodo=expression();
              if(salirMetodo){return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean count_exp(){
        boolean salirMetodo=false;
         if(instrucciones.get(posAnalizador).getToken().equals("Multiplicacion")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Multiplicacion");
           if(salirMetodo){return salirMetodo;}
         }else if(instrucciones.get(posAnalizador).getToken().equals("ALL") || instrucciones.get(posAnalizador).getToken().equals("DISTINCT")||instrucciones.get(posAnalizador).getToken().equals("Identificador")||instrucciones.get(posAnalizador).getToken().equals("Numero")||instrucciones.get(posAnalizador).getToken().equals("Float")||instrucciones.get(posAnalizador).getToken().equals("Bit")){
             if(instrucciones.get(posAnalizador).getToken().equals("ALL")){
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ALL");
                  if(salirMetodo){return salirMetodo;}
             }else if(instrucciones.get(posAnalizador).getToken().equals("DISTINCT")){
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DISTINCT");
                 if(salirMetodo){return salirMetodo;}
             }
             salirMetodo=expression();
             if(salirMetodo){return salirMetodo;}
         }else{
             if(instrucciones.get(posAnalizador).getToken().equals("COUNT")||instrucciones.get(posAnalizador).getToken().equals("MAX")||instrucciones.get(posAnalizador).getToken().equals("MIN")||instrucciones.get(posAnalizador).getToken().equals("SUM")||instrucciones.get(posAnalizador).getToken().equals("AVG")){
                  if(instrucciones.get(posAnalizador).getToken().equals("COUNT")){
                    MatchToken(instrucciones.get(posAnalizador),"COUNT");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MAX")){
                    MatchToken(instrucciones.get(posAnalizador),"MAX");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MIN")){
                    MatchToken(instrucciones.get(posAnalizador),"MIN");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("AVG")){
                    MatchToken(instrucciones.get(posAnalizador),"AVG");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("SUM")){
                    MatchToken(instrucciones.get(posAnalizador),"SUM");
                }
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=count_exp();
                    if(salirMetodo){return salirMetodo;}
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                    if(salirMetodo){return salirMetodo;}  
             }
         }
        return salirMetodo;
    }
    public boolean expression(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
            salirMetodo=identExp();
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Numero")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Numero");
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Float")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Float");
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Bit")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Bit");
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
            if(salirMetodo){return salirMetodo;}
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
           if(salirMetodo){return salirMetodo;}
        }
        else{
              errores+="Error se esperaba constante o identificador en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Suma")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Suma");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Resta")){
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Resta");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
           if(salirMetodo){return salirMetodo;} 
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Multiplicacion")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Multiplicacion");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
           if(salirMetodo){return salirMetodo;}
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("Division")){
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Division");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
           if(salirMetodo){return salirMetodo;} 
        }else if(instrucciones.get(posAnalizador).getToken().equals("Porcentaje")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Porcentaje");
           if(salirMetodo){return salirMetodo;}
           salirMetodo=expression();
           if(salirMetodo){return salirMetodo;}
        }
        
        
        return salirMetodo;
    }
    public boolean predicado(){
        boolean salirMetodo=false;
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "Identificador":
            case "Numero":
            case "Float":
            case "Bit":
            case "ParentesisAbierto":
                 if(instrucciones.get(posAnalizador).getToken().equals("Identificador") && (instrucciones.get(posAnalizador+1).getToken().equals("NOT")||instrucciones.get(posAnalizador+1).getToken().equals("LIKE")||(instrucciones.get(posAnalizador+1).getToken().equals("Igual")&&instrucciones.get(posAnalizador+2).getToken().equals("String")))){
                      salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                    if(salirMetodo){return salirMetodo;}
                    if(instrucciones.get(posAnalizador).getToken().equals("NOT")||instrucciones.get(posAnalizador).getToken().equals("LIKE")){
                        if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                        if(salirMetodo){return salirMetodo;}
                        }   
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"LIKE");
                        if(salirMetodo){return salirMetodo;}
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}
                        if(instrucciones.get(posAnalizador).getToken().equals("ESCAPE")){
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ESCAPE");
                        if(salirMetodo){return salirMetodo;}
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}
                    }                   
                 }else if(instrucciones.get(posAnalizador).getToken().equals("Igual")){
                         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                        if(salirMetodo){return salirMetodo;}
                         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}  
                         
                    }
                 }else{
                        salirMetodo=expression();
                        if(salirMetodo){return salirMetodo;}
                        if(instrucciones.get(posAnalizador).getToken().equals("IN")){
                            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IN");
                            if(salirMetodo){return salirMetodo;}
                            salirMetodo=expression();
                            if(salirMetodo){return salirMetodo;}
                        }else{
                            salirMetodo=state_logic();
                            if(salirMetodo){return salirMetodo;}
                        } 
                    }
               
                break;
            case "String":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                if(salirMetodo){return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                    if(salirMetodo){return salirMetodo;}
                }   
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"LIKE");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                if(salirMetodo){return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("ESCAPE")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ESCAPE");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                    if(salirMetodo){return salirMetodo;}
                }   break;
            case "CONTAINS":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CONTAINS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=cont_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "FREETEXT":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FREETEXT");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=cont_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            default:
                errores+="Error se esperaba expresion logica en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
        
        return salirMetodo;
    }
    public boolean when_list(){
        boolean salirMetodo=false;
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WHEN");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=search_condition();
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"THEN");
            if(salirMetodo){return salirMetodo;}
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "String":
                MatchToken(instrucciones.get(posAnalizador),"String");
                if(salirMetodo){return salirMetodo;}
                break;
            case "Identificador":
            case "Numero":
            case "Float":
            case "Bit":
            case "ParentesisAbierto":
                salirMetodo=expression();
                if(salirMetodo){return salirMetodo;}
                break;
            default:
                errores+="Error se esperaba String o expresion en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
            if(instrucciones.get(posAnalizador).getToken().equals("ELSE")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ELSE");
                if(salirMetodo){return salirMetodo;}
            switch (instrucciones.get(posAnalizador).getToken()) {
                case "String":
                   salirMetodo= MatchToken(instrucciones.get(posAnalizador),"String");
                    if(salirMetodo){return salirMetodo;}
                    break;
                case "Identificador":
                case "Numero":
                case "Float":
                    case "Bit":
                 case "ParentesisAbierto":
                    salirMetodo=expression();
                    if(salirMetodo){return salirMetodo;}
                    break;
                default:
                    errores+="Error se esperaba String o expresion en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
            }
            }
            if(instrucciones.get(posAnalizador).getToken().equals("WHEN")){
                salirMetodo=when_list();
                if(salirMetodo){return salirMetodo;}
            }
        
        
        return salirMetodo;
    }

    public boolean search_condition(){
        boolean salirMetodo=false;
         if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=search_condition();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
            }
         else{
         if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=predicado();
            if(salirMetodo){return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("OR")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OR");
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=search_condition();
                if(salirMetodo){return salirMetodo;}
            }
            if(instrucciones.get(posAnalizador).getToken().equals("AND")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AND");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=search_condition();
                   if(salirMetodo){return salirMetodo;}
             }
         }
      return salirMetodo;
    }   
    public boolean search_condition_having(){
        boolean salirMetodo=false;
         if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=search_condition_having();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
            }
         else{
         if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=predicado_having();
            if(salirMetodo){return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("OR")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OR");
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=search_condition_having();
                if(salirMetodo){return salirMetodo;}
            }
            if(instrucciones.get(posAnalizador).getToken().equals("AND")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AND");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=search_condition_having();
                   if(salirMetodo){return salirMetodo;}
             }
         }
      return salirMetodo;
    }   
    public boolean predicado_having(){
        boolean salirMetodo=false;
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "Identificador":
            case "Numero":
            case "Float":
            case "Bit":
               if(instrucciones.get(posAnalizador).getToken().equals("Identificador") && (instrucciones.get(posAnalizador+1).getToken().equals("NOT")||instrucciones.get(posAnalizador+1).getToken().equals("LIKE")||instrucciones.get(posAnalizador+1).getToken().equals("Igual"))){
                      salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                    if(salirMetodo){return salirMetodo;}
                    if(instrucciones.get(posAnalizador).getToken().equals("NOT")||instrucciones.get(posAnalizador).getToken().equals("LIKE")){
                        if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                        if(salirMetodo){return salirMetodo;}
                        }   
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"LIKE");
                        if(salirMetodo){return salirMetodo;}
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}
                        if(instrucciones.get(posAnalizador).getToken().equals("ESCAPE")){
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ESCAPE");
                        if(salirMetodo){return salirMetodo;}
                        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}
                    }                   
                 }else if(instrucciones.get(posAnalizador).getToken().equals("Igual")){
                         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                        if(salirMetodo){return salirMetodo;}
                         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                        if(salirMetodo){return salirMetodo;}
                    }
                 }else{
                        salirMetodo=expression();
                        if(salirMetodo){return salirMetodo;}
                        if(instrucciones.get(posAnalizador).getToken().equals("IN")){
                            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IN");
                            if(salirMetodo){return salirMetodo;}
                            salirMetodo=expression();
                            if(salirMetodo){return salirMetodo;}
                        }else{
                            salirMetodo=state_logic();
                            if(salirMetodo){return salirMetodo;}
                        } 
                    }
                break;
            case "String":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                if(salirMetodo){return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                    if(salirMetodo){return salirMetodo;}
                }   salirMetodo=MatchToken(instrucciones.get(posAnalizador),"LIKE");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                if(salirMetodo){return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("ESCAPE")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ESCAPE");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"String");
                    if(salirMetodo){return salirMetodo;}
                }   break;
            case "CONTAINS":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CONTAINS");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=cont_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "FREETEXT":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FREETEXT");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=cont_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "MIN":
            case "SUM":
            case "AVG":
            case "COUNT":
                case"MAX":
                if(instrucciones.get(posAnalizador).getToken().equals("COUNT")){
                    MatchToken(instrucciones.get(posAnalizador),"COUNT");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MAX")){
                    MatchToken(instrucciones.get(posAnalizador),"MAX");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MIN")){
                    MatchToken(instrucciones.get(posAnalizador),"MIN");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("AVG")){
                    MatchToken(instrucciones.get(posAnalizador),"AVG");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("SUM")){
                    MatchToken(instrucciones.get(posAnalizador),"SUM");
                }
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=count_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                 salirMetodo=state_logic();
                    if(salirMetodo){return salirMetodo;}
                break; 
            default:
                errores+="Error se esperaba expresion logica en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
        
        return salirMetodo;
    }
    public boolean createStatement(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CREATE");
        if(salirMetodo){return salirMetodo;}
        switch(instrucciones.get(posAnalizador).getToken()){
            case "TABLE":
                    salirMetodo=createTable();
                    if(salirMetodo){return salirMetodo;}
                break;
             case "DATABASE":
                 salirMetodo=createDatabase();
                    if(salirMetodo){return salirMetodo;}
                break; 
                case "USER":
                    salirMetodo=createUser();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "INDEX":
                case "UNIQUE":
                case "CLUSTERED":
                case "NONCLUSTERED":
                    salirMetodo=createIndex();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "VIEW":
                case "OR":
                    salirMetodo=createView();
                    if(salirMetodo){return salirMetodo;}
                break;
                default:
                    errores+="Error se esperaba CREATE en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
                   
        }
         if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
    public boolean createTable(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TABLE");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
        }
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=columnDef();
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("CONSTRAINT")){
            salirMetodo=constraint();
            if(salirMetodo){return salirMetodo;}
        }
        
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
        if(salirMetodo){return salirMetodo;}
        
        
        return salirMetodo;
    }
    public boolean columnDef(){
        boolean salirMetodo=false;
         salirMetodo=identExp();        
        if(salirMetodo){return salirMetodo;}
        salirMetodo=data_type();        
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("NOT")||instrucciones.get(posAnalizador).getToken().equals("NULL")){
            if(instrucciones.get(posAnalizador).getToken().equals("NOT")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                if(salirMetodo){return salirMetodo;}
            }
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NULL");
            if(salirMetodo){return salirMetodo;}
        }
        if(instrucciones.get(posAnalizador).getToken().equals("PRIMARY")){
                
           salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PRIMARY");
           if(salirMetodo){return salirMetodo;}
            
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"KEY");
            if(salirMetodo){return salirMetodo;}
            if(instrucciones.get(posAnalizador).getToken().equals("ASC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ASC");    
                    if(salirMetodo){ return salirMetodo;} 
                }
                else if(instrucciones.get(posAnalizador).getToken().equals("DESC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DESC");    
                    if(salirMetodo){ return salirMetodo;} 
                }
            
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
             if(salirMetodo){return salirMetodo;}
             salirMetodo=columnDef();
             if(salirMetodo){return salirMetodo;}
         }
        
        return salirMetodo;
    }
     public boolean data_type(){
        boolean salirMetodo=false;
         switch (instrucciones.get(posAnalizador).getToken()) {
            case "INT":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INT");
                if(salirMetodo){return salirMetodo;}
                break;
            case "FLOAT":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FLOAT");
                if(salirMetodo){return salirMetodo;}
                break;
            case "BIT":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"BIT");
                if(salirMetodo){return salirMetodo;}
                break;
            case "VARCHAR":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"VARCHAR");
                if(salirMetodo){return salirMetodo;}
                if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Numero");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");    
                    if(salirMetodo){ return salirMetodo;} 
                }
                break;
            default:
                errores+="Error se esperaba tipo de dato en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
        return salirMetodo;
    }
     public boolean constraint(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CONSTRAINT");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "PRIMARY":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PRIMARY");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"KEY");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=constraint2();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "FOREIGN":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FOREIGN");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"KEY");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=constraint2();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"REFERENCES");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=constraint2();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                break;
            case "CHECK":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CHECK");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=predicado();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "UNIQUE":
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"UNIQUE");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=constraint2();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            default:
                errores+="Error se esperaba constraint en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
       
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
            salirMetodo= MatchToken(instrucciones.get(posAnalizador),"Coma");
             if(salirMetodo){return salirMetodo;}
             salirMetodo=constraint();
             if(salirMetodo){return salirMetodo;}
         }
        return salirMetodo;
    }
     public boolean constraint_sincoma(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CONSTRAINT");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PRIMARY");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"KEY");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
        if(salirMetodo){return salirMetodo;}
            salirMetodo=constraint2();
            if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
        if(salirMetodo){return salirMetodo;}       
        return salirMetodo;
    }
     public boolean constraint2(){
        boolean salirMetodo=false;
        salirMetodo=identExp();
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("ASC")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ASC");    
            if(salirMetodo){ return salirMetodo;} 
       }
        else if(instrucciones.get(posAnalizador).getToken().equals("DESC")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DESC");    
            if(salirMetodo){ return salirMetodo;} 
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=constraint2();
                    if(salirMetodo){return salirMetodo;}
        }

        
        return salirMetodo;
    }
    public boolean createDatabase(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DATABASE");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("PRIMARY")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PRIMARY");    
                if(salirMetodo){ return salirMetodo;}    
         }
        salirMetodo=database();
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("COLLATE")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"COLLATE");    
              if(salirMetodo){ return salirMetodo;}   
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
        }
        
        return salirMetodo;
    }
    public boolean database(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=database();
                    if(salirMetodo){return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean createUser(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"USER");    
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
        if(salirMetodo){ return salirMetodo;}
                return salirMetodo;
    }
    public boolean createIndex(){
        boolean salirMetodo=false;
            if(instrucciones.get(posAnalizador).getToken().equals("UNIQUE")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"UNIQUE");    
                    if(salirMetodo){ return salirMetodo;} 
        }
        if(instrucciones.get(posAnalizador).getToken().equals("CLUSTERED")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CLUSTERED");    
                    if(salirMetodo){ return salirMetodo;} 
        }else if(instrucciones.get(posAnalizador).getToken().equals("NONCLUSTERED")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NONCLUSTERED");    
                    if(salirMetodo){ return salirMetodo;} 
        }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INDEX");    
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");    
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
        if(salirMetodo){ return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");    
        if(salirMetodo){ return salirMetodo;}
         salirMetodo=index();
         if(salirMetodo){ return salirMetodo;}         
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");         
        if(salirMetodo){ return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("INCLUDE")){
                    salirMetodo=include();
                    if(salirMetodo){ return salirMetodo;}   
        }
         if(instrucciones.get(posAnalizador).getToken().equals("WHERE")){
                    salirMetodo=sigExp();
                    if(salirMetodo){ return salirMetodo;}   
        }
        
        return salirMetodo;
    }
    public boolean index(){
        boolean salirMetodo=false;
         salirMetodo=identExp();
         if(salirMetodo){ return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("ASC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ASC");    
                    if(salirMetodo){ return salirMetodo;} 
        }
        else if(instrucciones.get(posAnalizador).getToken().equals("DESC")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DESC");    
                    if(salirMetodo){ return salirMetodo;} 
        }
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=index();
                    if(salirMetodo){ return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean include(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INCLUDE");         
         if(salirMetodo){ return salirMetodo;}
          salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");         
        if(salirMetodo){ return salirMetodo;}
            salirMetodo=index2();
         if(salirMetodo){ return salirMetodo;}  
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");         
        if(salirMetodo){ return salirMetodo;}
        
        return salirMetodo;
    }
    public boolean index2(){
        boolean salirMetodo=false;
         salirMetodo=identExp();
         if(salirMetodo){ return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=index2();
                    if(salirMetodo){ return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean createView(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("OR")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OR");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ALTER");    
                    if(salirMetodo){ return salirMetodo;} 
        }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"VIEW");    
                    if(salirMetodo){ return salirMetodo;} 
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
                    if(salirMetodo){ return salirMetodo;}            
        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Punto");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
                    if(salirMetodo){ return salirMetodo;}                    
        }  
        if(instrucciones.get(posAnalizador).getToken().equals("WITH")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WITH");    
                    if(salirMetodo){ return salirMetodo;} 
                     salirMetodo=col();
                    if(salirMetodo){ return salirMetodo;}                   
        }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");    
                    if(salirMetodo){ return salirMetodo;}
           //preguntar si va esto
           salirMetodo=selectStatement();
        if(salirMetodo){ return salirMetodo;}
        
        if(instrucciones.get(posAnalizador).getToken().equals("WITH")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WITH");    
                    if(salirMetodo){ return salirMetodo;} 
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CHECK");    
                    if(salirMetodo){ return salirMetodo;}                  
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OPTION");    
                    if(salirMetodo){ return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean col(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
         if(salirMetodo){ return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");    
                    if(salirMetodo){ return salirMetodo;} 
                    salirMetodo=col();
                    if(salirMetodo){ return salirMetodo;}
        }
        return salirMetodo; 
    }
    public boolean alterStatement(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ALTER");
        if(salirMetodo){return salirMetodo;}
        switch(instrucciones.get(posAnalizador).getToken()){
            case "TABLE":
                    salirMetodo=alterTable();
                    if(salirMetodo){return salirMetodo;}
                break;
             case "DATABASE":
                 salirMetodo=alterDatabase();
                    if(salirMetodo){return salirMetodo;}
                break; 
                case "USER":
                    salirMetodo=alterUser();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "INDEX":
                    salirMetodo=alterIndex();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "VIEW":
                    salirMetodo=alterView();
                    if(salirMetodo){return salirMetodo;}
                break;
                default:
                    errores+="Error se esperaba ALTER en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
                   
        }
         if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
    public boolean alterTable(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TABLE");    
                if(salirMetodo){ return salirMetodo;} 
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
              MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
              salirMetodo= MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
        }
        if(instrucciones.get(posAnalizador).getToken().equals("ALTER")){
              MatchToken(instrucciones.get(posAnalizador),"ALTER");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"COLUMN");
                if(salirMetodo){return salirMetodo;}   
                salirMetodo=identExp();
                if(salirMetodo){return salirMetodo;}                
                   if(instrucciones.get(posAnalizador).getToken().equals("ADD")){
                    MatchToken(instrucciones.get(posAnalizador),"ADD");
                     if(salirMetodo){return salirMetodo;}
                    }
                   else{
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DROP");
                     if(salirMetodo){return salirMetodo;}
                }
                   if(instrucciones.get(posAnalizador).getToken().equals("ROWGUIDCOL")){
                    MatchToken(instrucciones.get(posAnalizador),"ROWGUIDCOL");
                     if(salirMetodo){return salirMetodo;}
                    }
                   else{
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NOT");
                     if(salirMetodo){return salirMetodo;}
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FOR");
                     if(salirMetodo){return salirMetodo;}
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"REPLICATION");
                     if(salirMetodo){return salirMetodo;}
                }                                            
        }            else if(instrucciones.get(posAnalizador).getToken().equals("ADD")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ADD");
                     if(salirMetodo){return salirMetodo;}
                 salirMetodo=altertable2();
                 if(salirMetodo){return salirMetodo;}                
            }else if(instrucciones.get(posAnalizador).getToken().equals("DROP")){
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DROP");
                     if(salirMetodo){return salirMetodo;}        
                 salirMetodo=altertable3();
                 if(salirMetodo){return salirMetodo;} 
        }
        
        return salirMetodo;
    }
    public boolean altertable2(){
        boolean salirMetodo=false;
        if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
                    salirMetodo=columnDef();
                     if(salirMetodo){return salirMetodo;}
        }else if(instrucciones.get(posAnalizador).getToken().equals("CONSTRAINT")){
            salirMetodo=constraint_sincoma();
                     if(salirMetodo){return salirMetodo;}
        }
        else{
            errores+="Error se esperaba constraint o definicion de columna en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
            MatchToken(instrucciones.get(posAnalizador),"Coma");
                    salirMetodo=altertable2();
                     if(salirMetodo){return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean altertable3(){
        boolean salirMetodo=false;
         if(instrucciones.get(posAnalizador).getToken().equals("COLUMN")){
            MatchToken(instrucciones.get(posAnalizador),"COLUMN");
            if(salirMetodo){return salirMetodo;}
                    }   
        else  if(instrucciones.get(posAnalizador).getToken().equals("CONSTRAINT")){
            MatchToken(instrucciones.get(posAnalizador),"CONSTRAINT");
            if(salirMetodo){return salirMetodo;}
                    }
         if(instrucciones.get(posAnalizador).getToken().equals("IF")){
            MatchToken(instrucciones.get(posAnalizador),"IF");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");
            if(salirMetodo){return salirMetodo;}
                    }
         salirMetodo=col();
         if(salirMetodo){return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
            MatchToken(instrucciones.get(posAnalizador),"Coma");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=altertable3();
         if(salirMetodo){return salirMetodo;}
        } 
        return salirMetodo;
    }
    public boolean alterDatabase(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DATABASE");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
            MatchToken(instrucciones.get(posAnalizador),"Identificador");
            if(salirMetodo){return salirMetodo;}
                    }   
        else  if(instrucciones.get(posAnalizador).getToken().equals("CURRENT")){
            MatchToken(instrucciones.get(posAnalizador),"CURRENT");
            if(salirMetodo){return salirMetodo;}
                    }
        else{
                    errores+="Error se esperaba Identificador o CURRENT en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;        
         }
        if(instrucciones.get(posAnalizador).getToken().equals("COLLATE")){
            MatchToken(instrucciones.get(posAnalizador),"COLLATE");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
            if(salirMetodo){return salirMetodo;}
                    }   
        else  if(instrucciones.get(posAnalizador).getToken().equals("SET")){
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"SET");
            if(salirMetodo){return salirMetodo;}
            salirMetodo=col();
            if(salirMetodo){return salirMetodo;}
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WITH");
            if(salirMetodo){return salirMetodo;}
             salirMetodo=col();
            if(salirMetodo){return salirMetodo;}
                    }
        else{
                    errores+="Error se esperaba Identificador o CURRENT en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;        
         }
        return salirMetodo;
    }
    public boolean alterIndex(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INDEX");
        if(salirMetodo){return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("Identificador")){
            MatchToken(instrucciones.get(posAnalizador),"Identificador");
            if(salirMetodo){return salirMetodo;}
                    }   
        else  if(instrucciones.get(posAnalizador).getToken().equals("ALL")){
            MatchToken(instrucciones.get(posAnalizador),"ALL");
            if(salirMetodo){return salirMetodo;}
                    }
        else{
                    errores+="Error se esperaba Identificador o ALL en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;        
         }
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
              MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
        }
        
        return salirMetodo;
    }
    public boolean alterUser(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"USER");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}      
        return salirMetodo;
    }
    public boolean alterView(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"VIEW");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=index2();
        if(salirMetodo){return salirMetodo;}
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"AS");    
                    if(salirMetodo){ return salirMetodo;}
           //preguntar si va esto
           salirMetodo=selectStatement();
        if(salirMetodo){ return salirMetodo;}
        
        if(instrucciones.get(posAnalizador).getToken().equals("WITH")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WITH");    
                    if(salirMetodo){ return salirMetodo;} 
                     salirMetodo=MatchToken(instrucciones.get(posAnalizador),"CHECK");    
                    if(salirMetodo){ return salirMetodo;}                  
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"OPTION");    
                    if(salirMetodo){ return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean dropStatement(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DROP");
        if(salirMetodo){return salirMetodo;}
        switch(instrucciones.get(posAnalizador).getToken()){
            case "TABLE":
                    salirMetodo=dropTable();
                    if(salirMetodo){return salirMetodo;}
                break;
             case "DATABASE":
                 salirMetodo=dropDatabase();
                    if(salirMetodo){return salirMetodo;}
                break; 
                case "USER":
                    salirMetodo=dropUser();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "INDEX":
                    salirMetodo=dropIndex();
                    if(salirMetodo){return salirMetodo;}
                break;
                case "VIEW":
                    salirMetodo=dropView();
                    if(salirMetodo){return salirMetodo;}
                break;
                default:
                    errores+="Error se esperaba ALTER en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
                   
        }
         if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
    public boolean dropTable(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TABLE");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("IF")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IF");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");    
                if(salirMetodo){ return salirMetodo;}    
         }
        salirMetodo=sigtabla();
        if(salirMetodo){return salirMetodo;}
        
        return salirMetodo;
    }
     public boolean sigtabla(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
                if(salirMetodo){return salirMetodo;}
        }
        if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=sigtabla();
                if(salirMetodo){return salirMetodo;}
        }
        return salirMetodo;
    }
     public boolean dropIndex(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INDEX");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("IF")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IF");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");    
                if(salirMetodo){ return salirMetodo;}    
         }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");    
         if(salirMetodo){ return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("ON")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ON");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=col();    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              salirMetodo=index2();    
                if(salirMetodo){ return salirMetodo;} 
         }
        return salirMetodo;
    }
      public boolean dropUser(){
        boolean salirMetodo=false;
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"USER");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("IF")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IF");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");    
                if(salirMetodo){ return salirMetodo;}    
         }
        salirMetodo=col();
        if(salirMetodo){return salirMetodo;}
        
        return salirMetodo;
    }
       public boolean dropDatabase(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DATABASE");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("IF")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IF");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");    
                if(salirMetodo){ return salirMetodo;}    
         }
        salirMetodo=col();
        if(salirMetodo){return salirMetodo;}
        return salirMetodo;
    }
        public boolean dropView(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"VIEW");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("IF")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"IF");    
                if(salirMetodo){ return salirMetodo;}    
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"EXISTS");    
                if(salirMetodo){ return salirMetodo;}    
         }
         salirMetodo=index2();    
                if(salirMetodo){ return salirMetodo;}
        return salirMetodo;
    }
    public boolean truncateStatement(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TRUNCATE");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"TABLE");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=sigtabla();
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
    return salirMetodo;
    }
    public boolean updateStatement(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"UPDATE");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("TOP")){ //evalue si hay TOP
                 salirMetodo=TOP();//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
         }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("WITH")){ //evalue si hay TOP
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WITH");
                if(salirMetodo){return salirMetodo;} 
                salirMetodo=col();
                if(salirMetodo){return salirMetodo;}
         }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"SET");
        if(salirMetodo){return salirMetodo;}
        salirMetodo=sigUpdate();
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("FROM")){ //evalue si hay TOP
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FROM");
            if(salirMetodo){return salirMetodo;}        
            salirMetodo=sigtabla();
            if(salirMetodo){return salirMetodo;}  
           }
        if(instrucciones.get(posAnalizador).getToken().equals("WHERE")){ //evalue si hay TOP       
            salirMetodo=sigExp();
            if(salirMetodo){return salirMetodo;}  
        }     
        if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
    public boolean sigUpdate(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
         if(salirMetodo){return salirMetodo;} 
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("Igual")){ //evalue si es igual
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                  if(salirMetodo){return salirMetodo;} 
                  
            switch (instrucciones.get(posAnalizador).getToken()) {
                case "DEFAULT":
                    //evalue si hay default
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DEFAULT");
                    if(salirMetodo){return salirMetodo;}
                    break;
                case "NULL":
                    //evalue si hay null
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"NULL");
                    if(salirMetodo){return salirMetodo;}
                    break;
                default:
                    salirMetodo=expression();
                    if(salirMetodo){return salirMetodo;}
                    break;
            }
         }else if(instrucciones.get(posAnalizador).getToken().equals("Punto")){
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
               if(salirMetodo){return salirMetodo;}
                switch (instrucciones.get(posAnalizador).getToken()) {
                case "Igual":
                    //evalue si hay default
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Igual");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=expression();
                    if(salirMetodo){return salirMetodo;}
                    break;
                case "ParentesisAbierto":
                    //evalue si hay null
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=muchasExpresiones();
                    if(salirMetodo){return salirMetodo;}  
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                    if(salirMetodo){return salirMetodo;}
                    break;
                default:
                     errores+="Error se esperaba ( o = en la linea"+instrucciones.get(posAnalizador).getLinea();
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
            }
         }else if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Punto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"WRITE");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=expression();
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Numero");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Numero");
               if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                 if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
         }else{
              errores="Error se esperaba ( o . o = en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                    recorrerFinal();
                    salirMetodo=true;
                    return salirMetodo;
         }
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
              salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
               if(salirMetodo){return salirMetodo;}
               salirMetodo=sigUpdate();
                if(salirMetodo){return salirMetodo;}
        }
        return salirMetodo;
    }
    public boolean muchasExpresiones(){
        boolean salirMetodo=false;
         salirMetodo=expression();
         if(salirMetodo){return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){ //evalue si hay TOP
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
                  if(salirMetodo){return salirMetodo;}          
                  salirMetodo=muchasExpresiones();
                  if(salirMetodo){return salirMetodo;}  
           }
        return salirMetodo;
    }
    public boolean insertStatement(){
        boolean salirMetodo=false;
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INSERT");
        if(salirMetodo){return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("TOP")){ //evalue si hay TOP
                 salirMetodo=TOP();//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
         }
        if(instrucciones.get(posAnalizador).getToken().equals("INTO")){ //evalue si hay TOP
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"INTO");//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
         }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("ParentesisAbierto")){ //evalue si hay TOP
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
                salirMetodo=index2();
                if(salirMetodo){ return salirMetodo;} 
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
         }
        salirMetodo=MatchToken(instrucciones.get(posAnalizador),"VALUES");
        if(salirMetodo){return salirMetodo;}
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");//ejecuta TOP
            if(salirMetodo){ return salirMetodo;} 
            salirMetodo=insertValues();
            if(salirMetodo){ return salirMetodo;} 
            salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");//ejecuta TOP
            if(salirMetodo){ return salirMetodo;} 
        if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
    public boolean insertValues(){
        boolean salirMetodo=false;
        switch (instrucciones.get(posAnalizador).getToken()) {
            case "CASE":
                salirMetodo=caseState();
                if(salirMetodo){ return salirMetodo;}
                break;
            case "String":
                MatchToken(instrucciones.get(posAnalizador),"String");
                break;
            case "COUNT":
            case "MAX":
            case "MIN":
            case "SUM":
            case "AVG":
                if(instrucciones.get(posAnalizador).getToken().equals("COUNT")){
                    MatchToken(instrucciones.get(posAnalizador),"COUNT");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MAX")){
                    MatchToken(instrucciones.get(posAnalizador),"MAX");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("MIN")){
                    MatchToken(instrucciones.get(posAnalizador),"MIN");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("AVG")){
                    MatchToken(instrucciones.get(posAnalizador),"AVG");
                }
                if(instrucciones.get(posAnalizador).getToken().equals("SUM")){
                    MatchToken(instrucciones.get(posAnalizador),"SUM");
                }
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisAbierto");
                if(salirMetodo){return salirMetodo;}
                salirMetodo=count_exp();
                if(salirMetodo){return salirMetodo;}
                salirMetodo=MatchToken(instrucciones.get(posAnalizador),"ParentesisCerrado");
                if(salirMetodo){return salirMetodo;}
                break;
            case "ParentesisAbierto":
            case "Bit":
            case "Float":
            case "Numero":
            case "Identificador":
                salirMetodo=expression();
                if(salirMetodo){return salirMetodo;}
                break;
            default:
                errores+="Error se esperaba columna, funcion o expresion en la linea"+instrucciones.get(posAnalizador).getLinea()+"\n";
                recorrerFinal();
                salirMetodo=true;
                return salirMetodo;
        }
         if(instrucciones.get(posAnalizador).getToken().equals("Coma")){
                    salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Coma");
                    if(salirMetodo){return salirMetodo;}
                    salirMetodo=insertValues();
                    if(salirMetodo){return salirMetodo;}
                }
        return salirMetodo;
    }
    public boolean deleteStatement(){
        boolean salirMetodo=false;
         salirMetodo=MatchToken(instrucciones.get(posAnalizador),"DELETE");
        if(salirMetodo){return salirMetodo;}
         if(instrucciones.get(posAnalizador).getToken().equals("FROM")){ //evalue si hay TOP
                 salirMetodo=MatchToken(instrucciones.get(posAnalizador),"FROM");//ejecuta TOP
                if(salirMetodo){ return salirMetodo;} 
         }
          salirMetodo=MatchToken(instrucciones.get(posAnalizador),"Identificador");
        if(salirMetodo){return salirMetodo;}
        if(instrucciones.get(posAnalizador).getToken().equals("WHERE")){ //evalue si hay TOP       
            salirMetodo=sigExp();
            if(salirMetodo){return salirMetodo;}  
        } 
        if(instrucciones.get(posAnalizador).getToken().equals("PuntoyComa")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"PuntoyComa");    
                if(salirMetodo){ return salirMetodo;}    
         }
         else if(instrucciones.get(posAnalizador).getToken().equals("GO")){
             salirMetodo=MatchToken(instrucciones.get(posAnalizador),"GO");    
                if(salirMetodo){ return salirMetodo;}    
         }else{
              errores+="Error de sintaxis se esperaba ; o GO en la linea: "+instrucciones.get(posAnalizador).getLinea()+"\n";
                        recorrerFinal();
                        salirMetodo=true;
                        return salirMetodo;  
         }
        return salirMetodo;
    }
 }  
