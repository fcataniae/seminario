DROP DATABASE seminario;

CREATE DATABASE seminario;

INSERT INTO seminario.ESTADO
(DESCRIP)
VALUES('ACTIVO');

INSERT INTO seminario.ESTADO
(DESCRIP)
VALUES('INACTIVO');

INSERT INTO seminario.PERSONA
(APELLIDO, EMAIL, FECHA_NACIMIENTO, NOMBRE, NRODOC, TIPODOC, ESTADO_ID)
VALUES('PISTOLA', 'PEPE@ADMIN.COM', '1947-01-23', 'PEPE', 909000000, 'DNI', 1);

INSERT INTO seminario.USUARIO
(NOMBREUSUARIO, PASSWORD, ESTADO_ID, PERSONA_ID)
VALUES('admin', 'admin', 1, 1);

INSERT INTO seminario.ROL VALUES(100, 'Administrador', 'Administrador del sistema', 1);

INSERT INTO seminario.Usuario_Rol VALUES(1, 100);

INSERT INTO seminario.PERMISO VALUES(997, 'Alta persona', 'Permite dar de alta personas.', 'ALTA-PERSONA', 1);
INSERT INTO seminario.PERMISO VALUES(998, 'Baja persona', 'Permite dar de baja personas.', 'BAJA-PERSONA', 1);
INSERT INTO seminario.PERMISO VALUES(999, 'Consulta personas', 'Permite consultar personas.', 'CONS-PERSONA', 1);
INSERT INTO seminario.PERMISO VALUES(1000, 'Modificar personas', 'Permite Modificar personas.', 'MODI-PERSONA', 1);

INSERT INTO seminario.PERMISO VALUES(1001, 'Alta Usuario', 'Permite dar de alta usuarios.', 'ALTA-USUARIO', 1);
INSERT INTO seminario.PERMISO VALUES(1002, 'Baja Usuario', 'Permite dar de baja usuarios.', 'BAJA-USUARIO', 1);
INSERT INTO seminario.PERMISO VALUES(1003, 'Consulta Usuario', 'Permite consultar usuarios.', 'CONS-USUARIO', 1);
INSERT INTO seminario.PERMISO VALUES(1004, 'Modificar Usuario', 'Permite Modificar usuarios.', 'MODI-USUARIO', 1);

INSERT INTO seminario.PERMISO VALUES(1005, 'Alta Rol', 'Permite dar de alta Roles.', 'ALTA-ROL', 1);
INSERT INTO seminario.PERMISO VALUES(1006, 'Baja Rol', 'Permite dar de baja Roles.', 'BAJA-ROL', 1);
INSERT INTO seminario.PERMISO VALUES(1007, 'Consulta Rol', 'Permite consultar Roles.', 'CONS-ROL', 1);
INSERT INTO seminario.PERMISO VALUES(1008, 'Modificar Rol', 'Permite Modificar Roles.', 'MODI-ROL', 1);


INSERT INTO seminario.PERMISO VALUES(1009, 'Alta Permiso', 'Permite dar de alta Permiso.', 'ALTA-PERMISO', 1);
INSERT INTO seminario.PERMISO VALUES(1010, 'Baja Permiso', 'Permite dar de baja Permiso.', 'BAJA-PERMISO', 1);
INSERT INTO seminario.PERMISO VALUES(1011, 'Consulta Permiso', 'Permite consultar Permiso.', 'CONS-PERMISO', 1);
INSERT INTO seminario.PERMISO VALUES(1012, 'Modificar Permiso', 'Permite Modificar Permisos.', 'MODI-PERMISO', 1);


INSERT INTO seminario.ROL_PERMISO VALUES(100, 997);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 998);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 999);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1000);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1001);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1002);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1003);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1004);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1005);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1006);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1007);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1008);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1009);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1010);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1011);
INSERT INTO seminario.ROL_PERMISO VALUES(100, 1012);
