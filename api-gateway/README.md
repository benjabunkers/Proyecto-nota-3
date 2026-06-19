# API Gateway

Entrada central para los microservicios del sistema de arriendo.

## Puertos

| Aplicacion | Puerto |
| --- | ---: |
| API Gateway | 8080 |
| Clientes | 8081 |
| Vehiculos | 8082 |
| Reservas | 8083 |

## Ejecucion

Inicia primero Eureka Server en el puerto `8761`, luego los tres
microservicios y finalmente ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

Las APIs quedan disponibles a traves de `http://localhost:8080/api/v1/...`.
La documentacion unificada esta en `http://localhost:8080/doc/swagger-ui.html`.
El estado del Gateway esta en `http://localhost:8080/actuator/health`.

Los servicios se descubren en Eureka por los nombres `ms-clientes`,
`ms-vehiculos` y `ms-reservas`. La URL de Eureka puede cambiarse mediante
la variable `EUREKA_URL`.
