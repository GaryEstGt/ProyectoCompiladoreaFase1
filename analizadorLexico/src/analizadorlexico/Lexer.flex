package analizadorlexico;
import static analizadorlexico.Tokens.*;
%%
%class Lexer
%type Tokens
%line
%column
L=[a-z]+
L2=[A-Z_]+
D=[0-9]+
InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
    int line;
    int column;
%}
%%
ADD |
ALL |
ALTER |
AND |
ANY |
AS |
ASC |
AUTHORIZATION |
BACKUP |
BEGIN |
BETWEEN |
BREAK |
BROWSE |
BULK |
BY |
CASCADE |
CASE |
CHECK |
CHECKPOINT |
CLOSE |
CLUSTERED |
COALESCE |
COLLATE |
COLUMN |
COMMIT |
COMPUTE |
CONSTRAINT |
CONTAINS |
CONTAINSTABLE |
CONTINUE |
CONVERT |
CREATE |
CROSS |
CURRENT |
CURRENT_DATE |
CURRENT_TIME |
CURRENT_TIMESTAMP |
CURRENT_USER |
CURSOR |
DATABASE |
DBCC |
DEALLOCATE |
DECLARE |
DEFAULT |
DELETE |
DENY |
DESC |
DISK |
DISTINCT |
DISTRIBUTED |
DOUBLE |
DROP |
DUMP |
ELSE |
END |
ERRLVL |
ESCAPE |
EXCEPT |
EXEC |
EXECUTE |
EXISTS |
EXIT |
EXTERNAL |
FETCH |
FILE |
FILLFACTOR |
FOR |
FOREIGN | 
FREETEXT |
FREETEXTTABLE |
FROM |
FULL |
FUNCTION |
GOTO |
GRANT |
GROUP |
HAVING |
HOLDLOCK |
IDENTITY |
IDENTITY_INSERT |
IDENTITYCOL |
IF |
IN |
INDEX |
INNER |
INSERT |
INTERSECT |
INTO |
IS |
JOIN |
KEY |
KILL |
LEFT |
LIKE |
LINENO |
LOAD |
MERGE |
NATIONAL |
NOCHECK |
NONCLUSTERED |
NOT |
NULL |
NULLIF |
OF |
OFF |
OFFSETS |
ON |
OPEN |
OPENDATASOURCE |
OPENQUERY  |
OPENROWSET |
OPENXML |
OPTION |
OR |
ORDER |
OUTER |
OVER |
PERCENT |
PIVOT |
PLAN |
PRECISION |
PRIMARY |
PRINT |
PROC |
PROCEDURE |
PUBLIC |
RAISERROR |
READ |
READTEXT |
RECONFIGURE |
REFERENCES |
REPLICATION |
RESTORE |
RESTRICT |
RETURN |
REVERT |
REVOKE |
RIGHT |
ROLLBACK |
ROWCOUNT |
ROWGUIDCOL |
RULE |
SAVE |
SCHEMA |
SECUTIRYAUDIT |
SELECT|
SEMANTICKEYPHRASETABLE |
SEMANTICSIMILARITYDETAILSTABLE |
SEMANTICSIMILARITYTABLE |
SESSION_USER |
SET |
SETUSER |
SHUTDOWN |
SOME |
STATISTICS |
SYSTEM_USER |
TABLE |
TABLESAMPLE |
TEXTSIZE |
THEN |
TO |
TOP |
TRAN |
TRANSACTION |
TRIGGER |
TRUNCATE |
TRY_CONVERT |
TSEQUAL |
UNION |
UNIQUE |
UNPIVOT |
UPDATE |
UPDATETEXT |
USE |
USER |
VALUES |
VARYING |
VIEW |
WAITFOR |
WHEN |
WHERE |
WHILE |
WITH |
ABSOLUTE |
ACTION |
ADA |
ALLOCATE |
AND |
ARE |
ASSERTION |
AT |
AVG |
BIT |
BIT_LENGTH |
BOTH |
CASCADED |
CAST |
CATALOG |
CHAR |
CHAR_LENGTH |
CHARACTER |
CHARACTER_LENGTH |
COLLATION |
CONNECT |
CONNECTION |
CONSTRAINTS |
CORRESPONDING |
COUNT |
DATE |
DAY |
DEC |
DECIMAL |
DESCRIBE |
DESCRIPTOR |
DIAGNOSTICS |
DISCONNECT |
DOMAIN |
END-EXEC |
EXCEPTION |
EXTRACT |
FALSE |
FIRST |
FLOAT |
FORTRAN |
FOUND |
GET |
GLOBAL |
GO |
HOUR |
IMMEDIATE |
INCLUDE |
INDICATOR |
INITIALLY |
INPUT |
INSENSITIVE |
INT |
INTEGER |
INTERVAL |
ISOLARION |
LENGUAGE |
LAST |
LEADING |
LEVEL |
LOCAL |
LOWER |
MATCH |
MAX |
MIN |
MINUTE |
MODULE |
MONTH |
NAMES |
NATURAL |
NCHAR |
NEXT |
NO |
NONE |
NUMERIC |
OCTET_LENGTH |
ONLY |
OUTPUT |
OVERLAPS |
PAD |
PARTIAL |
PASCAL |
POSITION |
PREPARE |
PRESERVE |
PRIOR |
PRIVILEGES |
REAL |
RELATIVE |
ROWS |
SCROLL |
SECOND |
SECTION |
SESSION |
SIZE |
SMALLINT |
SPACE |
SQL |
SQLCA |
SQLCODE |
SQLERROR |
SQLSTATE |
SQLWARNING |
SUBSTRING |
SUM |
SYSTEM_USER |
TEMPORARY |
TIME |
TIMESTAMP |
TIMEZONE_HOUR |
TIMEZONE_MINUTE |
TRAILING |
TRANSLATE |
TRANSLATION |
TRIM |
TRUE |
UNKNOWN |
UPPER |
USAGE |
USING |
VALUE |
VARCHAR |
WHENEVER |
WORK |
WRITE |
YEAR |
ZONE |
WRITETEXT {lexeme=yytext(); line=yyline; column=yycolumn; return Reservadas;}
{espacio} {/*Ignore*/}
"--" {InputCharacter}*{LineTerminator}? {/*Ignore*/}
"=" {lexeme=yytext(); line=yyline; column=yycolumn; return Igual;}
"+" {lexeme=yytext(); line=yyline; column=yycolumn; return Suma;}
"-" {lexeme=yytext(); line=yyline; column=yycolumn; return Resta;}
"*" {lexeme=yytext(); line=yyline; column=yycolumn; return Multiplicacion;}
"/" {lexeme=yytext(); line=yyline; column=yycolumn; return Division;}
"_" {lexeme=yytext(); line=yyline; column=yycolumn; return Guionbajo;}
"%" {lexeme=yytext();line=yyline; column=yycolumn; return Porcentaje;}
"<" {lexeme=yytext(); line=yyline; column=yycolumn; return Menor;}
">" {lexeme=yytext(); line=yyline; column=yycolumn; return Mayor;}
"<=" {lexeme=yytext(); line=yyline; column=yycolumn; return MenorIgual;}
">=" {lexeme=yytext(); line=yyline; column=yycolumn; return MayorIgual;}
"==" {lexeme=yytext(); line=yyline; column=yycolumn; return IgualIgual;}
"!=" {lexeme=yytext(); line=yyline; column=yycolumn; return NoIgual;}
"&" {lexeme=yytext(); line=yyline; column=yycolumn; return y;}
"&&" {lexeme=yytext(); line=yyline; column=yycolumn; return and;}
"||" {lexeme=yytext(); line=yyline; column=yycolumn; return or;}
"!" {lexeme=yytext(); line=yyline; column=yycolumn; return SignoExclamacion;}
";" {lexeme=yytext(); line=yyline; column=yycolumn; return PuntoyComa;}
"," {lexeme=yytext(); line=yyline; column=yycolumn; return Coma;}
"." {lexeme=yytext(); line=yyline; column=yycolumn; return Punto;}
"[" {lexeme=yytext(); line=yyline; column=yycolumn; return CorcheteAbierto;}
"]" {lexeme=yytext(); line=yyline; column=yycolumn; return CorcheteCerrado;}
"[]" {lexeme=yytext(); line=yyline; column=yycolumn; return Corchetes;}
"(" {lexeme=yytext(); line=yyline; column=yycolumn; return ParentesisAbierto;}
")" {lexeme=yytext(); line=yyline; column=yycolumn; return ParentesisCerrado;}
"()" {lexeme=yytext(); line=yyline; column=yycolumn; return Parentesis;}
"{" {lexeme=yytext(); line=yyline; column=yycolumn; return LlaveAbierta;}
"}" {lexeme=yytext(); line=yyline; column=yycolumn; return LlaveCerrada;}
"{}" {lexeme=yytext(); line=yyline; column=yycolumn; return Llaves;}
"@" {lexeme=yytext(); line=yyline; column=yycolumn; return Arroba;}
"#" {lexeme=yytext(); line=yyline; column=yycolumn; return Numeral;}
"##" {lexeme=yytext(); line=yyline; column=yycolumn; return DobleNumeral;}
({L}|{L2})({L}|{L2}|{D})* {lexeme=yytext(); line=yyline; column=yycolumn; return Identificador;}
(1)|(0)| NULL {lexeme=yytext(); line=yyline; column=yycolumn; return Bit;}
("(-"{D}+")")|{D}+ {lexeme=yytext(); line=yyline; column=yycolumn; return Numero;}
(({D}+)"."({D}*))|(({D}+)"."({D}*)E"+"(1|2|3|4|5|6|7|8|9)({D}*)) {lexeme=yytext(); column=yycolumn; line=yyline; return Float;}
"'".*"'" {lexeme=yytext(); line=yyline; column=yycolumn; return String;}
"/*" [^*] ~"*/" | "/*" "*"+ "/" {/*Ignore*/}
"/*" [^*] | "/*" "*"+ {line=yyline; column=yycolumn; return ERRORComentario;}
"."{D}+ {line=yyline; column=yycolumn; lexeme=yytext(); return ERRORDecimal;}
 . {line=yyline; column=yycolumn; return ERROR;}
