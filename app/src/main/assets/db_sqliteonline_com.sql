-- TABLE
CREATE TABLE cat_secciones (idSecc integer not NULL PRIMARY KEY AUTOINCREMENT, Descripcion  varchar(20));
CREATE TABLE sqlite_sequence(name,seq);
CREATE TABLE usuario ( iduser INTEGER PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR(50), Genero varchar(10), FechaReg datetime);
CREATE TABLE usuario_avances (idUA integer PRIMARY KEY AUTOINCREMENT, iduser integer not NULL, idSeccion integer not NULL, Acertado integer , Fecha datetime);
 
-- INDEX
 
-- TRIGGER
 
-- VIEW
 
