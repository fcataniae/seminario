
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


##### LISTAR DE PERSONAS/USUARIOS/ROLES/PERMISOS


Cambiar en <objeto> por [personas/usuarios/roles/permisos]
```
curl -X GET \
  http://localhost:9081/service/listar-<objeto> \
  -H 'authorization: Basic YWRtaW46YWRtaW4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 65dbfc43-39cb-281f-2b3a-f32fc376adac'
```