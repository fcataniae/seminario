DROP DATABASE seminario;

CREATE DATABASE seminario;

SELECT ID, NOMBREUSUARIO, PASSWORD, ESTADO_ID, PERSONA_ID
FROM seminario.USUARIO;

INSERT INTO seminario.ESTADO
(DESCRIP)
VALUES('ACTIVO');

INSERT INTO seminario.ESTADO
(DESCRIP)
VALUES('INACTIVO');

INSERT INTO seminario.USUARIO
(NOMBREUSUARIO, PASSWORD, ESTADO_ID)
VALUES('admin', 'admin', 1);

INSERT INTO seminario.ROL VALUES(100, 'Administrador', 'Administrador del sistema', 1);

INSERT INTO seminario.Usuario_Rol VALUES(1, 100);

INSERT INTO seminario.PERMISO VALUES(1000, 'Alta Usuario', 'Permite dar de alta usuarios.', 'ALTA-USUARIO', 1, 1, 1);

INSERT INTO seminario.PERMISO VALUES(1001, 'Baja Usuario', 'Permite dar de baja usuarios.', 'BAJA-USUARIO', 1, 1, 1);
INSERT INTO seminario.PERMISO VALUES(1002, 'Consulta Usuario', 'Permite dar de Consulta usuarios.', 'CONS-USUARIO', 1, 1, 1);

INSERT INTO seminario.ROL_PERMISO VALUES(100, 1000);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1001);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1002);



SELECT * from dbtest.PERSONA_USUARIO