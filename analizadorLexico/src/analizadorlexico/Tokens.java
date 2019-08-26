/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico;

/**
 *
 * @author garya
 */
public enum Tokens {
    Reservadas,
    Igual,
    Suma,
    Resta,
    Multiplicacion,
    Division,
    Identificador,
    Numero,
    Guionbajo,
    Porcentaje,
    Menor,
    Mayor,
    MenorIgual,
    MayorIgual,
    IgualIgual,
    NoIgual,
    y,
    and,
    or,
    SignoExclamacion,
    PuntoyComa,
    Coma,
    Punto,
    CorcheteAbierto,
    CorcheteCerrado,
    Corchetes,
    ParentesisAbierto,
    ParentesisCerrado,
    Parentesis,
    LlaveAbierta,
    LlaveCerrada,
    Llaves,
    Arroba,
    Numeral,
    DobleNumeral,
    Bit,
    Float,
    String,
    ERROR,
    ERRORComentario,
    ERRORDecimal,
    ERRORString
}
