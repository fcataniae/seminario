## REQUESTS

#### Obtención del TOKEN

Se obtiene el token que luego se utilizará en todas los metodos.

```fish
export TOKEN=$(curl -X POST \
      http://localhost:9081/auth/login/ \
      -H 'cache-control: no-cache' \
      -H 'content-type: application/json' \
      -H 'postman-token: 59739453-a534-a362-f28d-daefe2d8439a' \
      -d '{
        "username": "admin",
        "password": "admin"
    }' | jq -r ".token")
```

#### Testing /alta-movimiento

#### Envio ✅ - WORKING

Envio (CD = 7460, Tienda = 1023)

```bash
curl -X POST \
  http://localhost:9081/bienes/alta-movimiento \
  -H 'Authorization: Bearer '$TOKEN'' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
    "id": null,
    "origen": 7460,
    "destino": 1023,
    "tipoMovimiento": {"tipo":"ENVIO","tipoAgenteDestino":{"nombre":"TIENDA"} ,"tipoAgenteOrigen":{"nombre":"CD"} },
    "id_tipo_documento": 1,
    "itemMovimientos": [
      {"bienIntercambiable":{"id":1},"cantidad":30},
      {"bienIntercambiable":{"id":2},"cantidad":30}
    ]
  }'
```

- Verificar Stock bien en local.

#### Recepción PROVEEDOR - CD | ✅ - WORKING

Recepción (Origen: Proveedor = 2, Destino: CD = 7460)

```bash
curl -X POST \
  http://localhost:9081/bienes/alta-movimiento \
  -H 'Authorization: Bearer '$TOKEN'' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "id": null,
  "origen": 2,
  "destino": 7460,
  "tipoMovimiento": {"tipo":"RECEPCION","tipoAgenteDestino":{"nombre":"CD"} ,"tipoAgenteOrigen":{"nombre":"PROVEEDOR"} },
  "id_tipo_documento": 1,
  "itemMovimientos": [
    {"bienIntercambiable":{"id":1},"cantidad":30},
    {"bienIntercambiable":{"id":2},"cantidad":30}
  ]
}'
```

#### Recepción TIENDA - CD | ✅ - WORKING

Recepción (Origen: Tienda = 1024, Destino: CD = 7460)

```bash
curl -X POST \
  http://localhost:9081/bienes/alta-movimiento \
  -H 'Authorization: Bearer '$TOKEN'' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
  "id": null,
  "origen": 2,
  "destino": 7460,
  "tipoMovimiento": {"tipo":"RECEPCION","tipoAgenteDestino":{"nombre":"CD"} ,"tipoAgenteOrigen":{"nombre":"PROVEEDOR"} },
  "id_tipo_documento": 3,
  "nroDocumento": 201923032133,
  "itemMovimientos": [
    {"bienIntercambiable":{"id":4},"cantidad":300,"vacio":true},
    {"bienIntercambiable":{"id":2},"cantidad":30,"vacio":true},
    {"bienIntercambiable":{"id":5},"cantidad":30,"vacio":true}
  ]
}'
```
