# Seminario - Proyecto Bienes Int.

## FrontEnd

### Pasos para correr el front, sobre la carpeta frontend-application

* descargar node.js e instalarlo
* `npm install @angular/cli --global`
* `npm install`
* `ng serve --open`

### Build

* configurar archivo environment.prod.ts
* ng build --prod -aot

**TODO: Generar war automatico**

## BackEnd

### Dependencias

* [maria-db](https://mariadb.org/download/)

### Pasos

* Crear usuario en mariadb

* Crear archivo:

```
backend-application/backend/src/main/resources/application.properties
```

Completar la configuracion (credenciales, host, etc). Basarse en:

```
backend-application/backend/src/main/resources/application.properties.example
```
* Crear la base de Datos 'seminario'

* Importar el proyecto a IDEA (en caso de hacerlo otro IDE, por la terminal, cargar dependencias de Maven `mvn install`).

* En IDEA, ir a **Run/Debug Configurations > VM Options**, añadir:

`-Dspring.profiles.active=desarrollo`

**TODO: (ver como se realiza en otros IDES)**

* Correr el backend desde:

 `backend-application/Webservices.../ApiRestController`
