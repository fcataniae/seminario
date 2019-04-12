# Seminario - Proyecto Bienes Int.

* TODO: agregar el archivo web.xml/clase de configuracion para deployarlo en tomcat

## FrontEnd

### Pasos para correr el front, sobre la carpeta frontend-application

* descargar node.js e instalarlo
* `npm install @angular/cli --global`
* `npm install`
* `ng serve --open`

### Build

* ng build --prod -aot href /

## BackEnd

### Dependencias

* [maria-db](https://mariadb.org/download/)

### Pasos

* Crear usuario en mariadb

* Crear la base de Datos 'seminario'

* Importar el proyecto a IDEA (en caso de hacerlo otro IDE, por la terminal, cargar dependencias de Maven `mvn install`).

* En IDEA, ir a **Run/Debug Configurations > VM Options**, añadir:

`-Dspring.profiles.active=desarrollo`

**TODO: (ver como se realiza en otros IDES)**

* Correr el backend desde:

 `backend-application/Webservices.../ApiRestController`

* Realizar los insert de `/backend-application/backend/src/main/java/com/seminario/backend/bd-scripts/INSERTS.sql;` en la BD 'seminario'.
