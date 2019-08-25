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
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
    int line;
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
WRITETEXT {lexeme=yytext(); line=yyline; return Reservadas;}
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"=" {line=yyline; return Igual;}
"+" {line=yyline; return Suma;}
"-" {line=yyline; return Resta;}
"*" {line=yyline; return Multiplicacion;}
"/" {line=yyline; return Division;}
"_" {line=yyline; return Guionbajo;}
"%" {line=yyline; return Porcentaje;}
"<" {line=yyline; return Menor;}
">" {line=yyline; return Mayor;}
"<=" {line=yyline; return MenorIgual;}
">=" {line=yyline; return MayorIgual;}
"==" {line=yyline; return IgualIgual;}
"!=" {line=yyline; return NoIgual;}
"&" {line=yyline; return y;}
"&&" {line=yyline; return and;}
"||" {line=yyline; return or;}
"!" {line=yyline; return SignoExclamacion;}
";" {line=yyline; return PuntoyComa;}
"," {line=yyline; return Coma;}
"." {line=yyline; return Punto;}
"[" {line=yyline; return CorcheteAbierto;}
"]" {line=yyline; return CorcheteCerrado;}
"[]" {line=yyline; return Corchetes;}
"(" {line=yyline; return ParentesisAbierto;}
")" {line=yyline; return ParentesisCerrado;}
"()" {line=yyline; return Parentesis;}
"{" {line=yyline; return LlaveAbierta;}
"}" {line=yyline; return LlaveCerrada;}
"{}" {line=yyline; return Llaves;}
"@" {line=yyline; return Arroba;}
"#" {line=yyline; return Numeral;}
"##" {line=yyline; return DobleNumeral;}
({L}|{L2})({L}|{L2}|{D})* {lexeme=yytext(); line=yyline; return Identificador;}
(1)|(0)| NULL {lexeme=yytext(); line=yyline; return Bit;}
("(-"{D}+")")|{D}+ {lexeme=yytext(); line=yyline; return Numero;}
(({D}+)"."({D}*))|(({D}+)"."({D}*)E"+"(1|2|3|4|5|6|7|8|9)({D}*)) {lexeme=yytext(); line=yyline; return Float;}
"'".*"'"? {lexeme=yytext(); line=yyline; return String;}
"/*" [^*] ~"*/" | "/*" "*"+ "/" {/*Ignore*/}
 . {line=yyline; return ERROR;}
