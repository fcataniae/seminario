
## REQUESTS

##### ALTA DE PERSONA

```
curl -X PUT \
  http://localhost:9081/service/alta-persona \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 98f7e7f8-286b-f9b3-cfc3-7d273909f0a1' \
  -d '{
	"nombre": "MARIA",
	"apellido": "DB",
	"nroDoc" : 40433304,
	"tipoDoc": "DNI",
	"fecha_nacimiento" :"2018-09-13",
	"email" : "maria@db.com",
	"estado_id": "1"
}'
```

##### ALTA DE USUARIO

Ingresar el <id-persona> a la cual estará asociado el nuevo usuario.

```
curl -X POST \
  http://localhost:9081/service/<id-persona>/usuario \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 51ff0bf1-e26c-668d-8e8c-32b3aa62ed08' \
  -d '{
	"nombreUsuario": "NicholasCage",
	"password": "Cage123"
}'
```

##### ALTA DE ROL

```
curl -X POST \
  http://localhost:9081/service/alta-rol \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 36b5d42b-850e-12b9-3066-e0561436f25b' \
  -d '{
    "nombre": "ROL1",
    "descripcion": "Novato"
}'
```

##### ALTA DE PERMISO

```
curl -X POST \
  http://localhost:9081/service/alta-permiso \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 9c9dbf72-247c-0b56-6e2f-af7738f3c021' \
  -d '{
	"nombre": "NUEVO_PERMISO",
	"descripcion" : "FUNCION X al sistema",
	"funcionalidad": "Hace bla, bla, bla."
}'
```

### Modificaciones

##### MODIFICAR PERSONA

```
curl -X PUT \
  http://localhost:9081/service/update-persona \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a2224b17-53db-8de7-614a-431b4102cb9f' \
  -d '{
    "id": 1,
    "nombre": "PANZA",
    "apellido": "SANCHO",
    "nroDoc": 909000000,
    "tipoDoc": "DNI",
    "fecha_nacimiento": "2018-09-02",
    "email": "PAPO@ADMIN.COM",
    "estado": {
        "id": 1,
        "descrip": "ACTIVO"
    }
}'
```


##### MODIFICAR USUARIO

Ver que se puede injectar una lista de roles al usuario.

```
curl -X PUT \
  http://localhost:9081/service/update-usuario \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a96343ae-90af-2722-00b1-e163efcfe6ef' \
  -d '{
	"nombreUsuario": "reatt", 
	"password": "123"
}'
```

##### MODIFICAR ROLES

Ver que se puede injectar una lista de permisos al rol.

```
curl -X PUT \
  http://localhost:9081/service/update-rol \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ce1d5c2b-ee97-63b5-0c39-788c11b0c8dc' \
  -d '{
        "id": 100,
        "nombre": "Novato del sistema",
        "descripcion": "Novato",
        "permisos": [{
                "id": 1007,
                "nombre": "CONS-ROL",
                "descripcion": "Consulta Rol",
                "funcionalidad": "Permite consultar Roles.",
                "estado": {
                    "id": 1,
                    "descrip": "ACTIVO"
                }
        }]
}'
```

##### MODIFICAR PERMISOS

```
curl -X PUT \
  http://localhost:9081/service/update-permiso \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: a75eb315-1740-49fa-f486-7fae9d4ac3ba' \
  -d '{
        "id": 997,
        "nombre": "ALTA-PERSONA",
        "descripcion": "Alta persona",
        "funcionalidad": "Permite dar de alta a personas. >.<",
        "estado": {
            "id": 1,
            "descrip": "ACTIVO"
        }
}'
```

### Listar (SELECT * FROM...)

##### LISTAR DE PERSONAS/USUARIOS/ROLES/PERMISOS

Cambiar en <objeto> por [personas/usuarios/roles/permisos]`

```
curl -X GET \
  http://localhost:9081/service/listar-<objeto> \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: o-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 65dbfc43-39cb-281f-2b3a-f32fc376adac'
```
