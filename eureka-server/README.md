# Eureka Server

Servidor de registro y descubrimiento para los microservicios del sistema.

## Ejecucion

Desde IntelliJ, ejecuta `EurekaServerApplication`. Tambien puedes iniciarlo con:

```powershell
.\mvnw.cmd spring-boot:run
```

El panel de Eureka queda disponible en `http://localhost:8761` y su estado en
`http://localhost:8761/actuator/health`.

Debe iniciarse antes que `ms-clientes`, `ms-vehiculos`, `ms-reservas` y
`api-gateway`.
