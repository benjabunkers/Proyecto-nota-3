Sistema de arriendo de vehiculos

Proyecto de microservicios desarrollado con Spring Boot para administrar clientes, vehiculos, reservas, pagos, reportes, empleados y sucursales. Incluye descubrimiento de servicios con Eureka, comunicacion mediante OpenFeign, API Gateway, Swagger/OpenAPI, HATEOAS, DataFaker, Flyway y pruebas con JUnit y Mockito.

Arquitectura

Aplicacion | Puerto | Funcion
Eureka Server | 8761 | Registro y descubrimiento de servicios
API Gateway | 8080 | Punto de entrada central y Swagger agregado
ms-clientes | 8081 | Gestion de clientes y direcciones
ms-vehiculos | 8082 | Gestion de vehiculos y categorias
ms-reservas | 8083 | Gestion de reservas y estados
ms-pagos | 8084 | Gestion de pagos asociados a reservas
ms-sucursales | 8085 | Gestion de sucursales y regiones
ms-empleados | 8086 | Gestion de empleados
ms-reportes | 8087 | Gestion de reportes asociados a reservas y pagos

Las rutas /api/v1/** entregan respuestas JSON estandar. Las rutas /api/v2/** mantienen la misma logica e incorporan enlaces HATEOAS.

Requisitos

- Java 17 o superior.
- Maven Wrapper incluido en cada modulo.
- MySQL disponible en localhost:3306.
- Bases de datos prueba1, prueba2, prueba3 y prueba4.

Verificar la version de Java:

java -version
$env:JAVA_HOME

Si java -version muestra Java 8, usar el JDK configurado en JAVA_HOME:

$env:Path="$env:JAVA_HOME\bin;$env:Path"

Bases de datos

Crear las bases antes de iniciar los microservicios:

CREATE DATABASE IF NOT EXISTS prueba1;
CREATE DATABASE IF NOT EXISTS prueba2;
CREATE DATABASE IF NOT EXISTS prueba3;
CREATE DATABASE IF NOT EXISTS prueba4;

- prueba1: clientes, vehiculos, sucursales y empleados.
- prueba2: reservas y reportes.
- prueba3: pagos.

Las credenciales predeterminadas son usuario root y contrasena vacia. Pueden cambiarse mediante las variables o propiedades de cada microservicio.

Orden de inicio recomendado

Iniciar cada aplicacion en una terminal distinta. El orden importa porque algunos servicios consultan a otros mediante OpenFeign y todos deben registrarse en Eureka antes de usarse desde el Gateway.

1. MySQL.
2. Eureka Server.
3. Microservicios base sin dependencias fuertes entre servicios:
   - ms-clientes
   - ms-vehiculos
   - ms-sucursales
   - ms-empleados
4. ms-reservas.
   - Reservas consulta clientes y vehiculos mediante OpenFeign.
5. ms-pagos.
   - Pagos consulta reservas mediante OpenFeign.
6. ms-reportes.
   - Reportes consulta reservas y pagos mediante OpenFeign.
7. API Gateway.
   - Iniciarlo al final para que ya pueda descubrir todos los servicios registrados en Eureka.

Comando de inicio dentro de cada modulo:

.\mvnw.cmd spring-boot:run

Rutas de los modulos

1. eureka-server
2. ms-clientes\ms-clientes
3. ms-vehiculos\ms-vehiculos
4. ms-sucursales\ms-sucursales
5. ms-empleados\ms-empleados
6. ms-reservas\ms-reservas
7. ms-pagos\ms-pagos
8. ms-reportes\ms-reportes
9. api-gateway


URLs principales

- Eureka Dashboard: http://localhost:8761
- Swagger central del API Gateway: http://localhost:8080/doc/swagger-ui.html
- Salud del API Gateway: http://localhost:8080/actuator/health

Probar mediante API Gateway

Clientes y direcciones

- Clientes V1: http://localhost:8080/api/v1/clientes
- Cliente V1 por ID: http://localhost:8080/api/v1/clientes/1
- Clientes V2: http://localhost:8080/api/v2/clientes
- Cliente V2 por ID: http://localhost:8080/api/v2/clientes/1
- Buscar cliente por correo: http://localhost:8080/api/v1/clientes/buscar-email?texto=gmail
- Direcciones V1: http://localhost:8080/api/v1/direcciones
- Direcciones V2: http://localhost:8080/api/v2/direcciones

Vehiculos y categorias

- Vehiculos V1: http://localhost:8080/api/v1/vehiculos
- Vehiculo V1 por ID: http://localhost:8080/api/v1/vehiculos/1
- Vehiculos V2: http://localhost:8080/api/v2/vehiculos
- Vehiculo V2 por ID: http://localhost:8080/api/v2/vehiculos/1
- Vehiculos disponibles bajo $50.000: http://localhost:8080/api/v1/vehiculos/disponibles/precio-menor/50000
- Categorias V1: http://localhost:8080/api/v1/categorias
- Categorias V2: http://localhost:8080/api/v2/categorias

Reservas y estados

- Reservas V1: http://localhost:8080/api/v1/reservas
- Reserva V1 por ID: http://localhost:8080/api/v1/reservas/1
- Reservas V2: http://localhost:8080/api/v2/reservas
- Reserva V2 por ID: http://localhost:8080/api/v2/reservas/1
- Reservas desde una fecha: http://localhost:8080/api/v1/reservas/desde-fecha?fecha=2026-06-21
- Estados de reserva V1: http://localhost:8080/api/v1/estados-reserva
- Estados de reserva V2: http://localhost:8080/api/v2/estados-reserva

Pagos

- Pagos V1: http://localhost:8080/api/v1/pagos
- Pago V1 por ID: http://localhost:8080/api/v1/pagos/1
- Pagos V2: http://localhost:8080/api/v2/pagos
- Pago V2 por ID: http://localhost:8080/api/v2/pagos/1
- Pagos por rango: http://localhost:8080/api/v1/pagos/rango?min=10000&max=50000

Reportes

- Reportes V1: http://localhost:8080/api/v1/reportes
- Reporte V1 por ID: http://localhost:8080/api/v1/reportes/1
- Reportes V2: http://localhost:8080/api/v2/reportes
- Reporte V2 por ID: http://localhost:8080/api/v2/reportes/1
- Reportes por reserva: http://localhost:8080/api/v1/reportes/reserva/1
- Reportes por pago confirmado: http://localhost:8080/api/v1/reportes/pago-confirmado?confirmado=true

Sucursales y regiones

- Sucursales V1: http://localhost:8080/api/v1/sucursales
- Sucursal V1 por ID: http://localhost:8080/api/v1/sucursales/1
- Sucursales V2: http://localhost:8080/api/v2/sucursales
- Sucursal V2 por ID: http://localhost:8080/api/v2/sucursales/1
- Sucursales operativas: http://localhost:8080/api/v1/sucursales/operativas
- Regiones V1: http://localhost:8080/api/v1/regiones
- Regiones V2: http://localhost:8080/api/v2/regiones

Empleados

- Empleados V1: http://localhost:8080/api/v1/empleados
- Empleado V1 por ID: http://localhost:8080/api/v1/empleados/1
- Empleados V2: http://localhost:8080/api/v2/empleados
- Empleado V2 por ID: http://localhost:8080/api/v2/empleados/1
- Empleados activos por anio: http://localhost:8080/api/v1/activos/anio/2024

Acceso directo a cada microservicio

Swagger

- Swagger de clientes: http://localhost:8081/doc/swagger-ui.html
- Swagger de vehiculos: http://localhost:8082/doc/swagger-ui.html
- Swagger de reservas: http://localhost:8083/doc/swagger-ui.html
- Swagger de pagos: http://localhost:8084/doc/swagger-ui.html
- Swagger de sucursales: http://localhost:8085/doc/swagger-ui.html
- Swagger de empleados: http://localhost:8086/doc/swagger-ui.html
- Swagger de reportes: http://localhost:8087/doc/swagger-ui.html

Endpoints directos

- Clientes directos: http://localhost:8081/api/v1/clientes
- Vehiculos directos: http://localhost:8082/api/v1/vehiculos
- Reservas directas: http://localhost:8083/api/v1/reservas
- Pagos directos: http://localhost:8084/api/v1/pagos
- Sucursales directas: http://localhost:8085/api/v1/sucursales
- Empleados directos: http://localhost:8086/api/v1/empleados
- Reportes directos: http://localhost:8087/api/v1/reportes

Documentos OpenAPI

El Gateway agrega las especificaciones de los microservicios en estas rutas:

- OpenAPI clientes: http://localhost:8080/docs/clientes
- OpenAPI vehiculos: http://localhost:8080/docs/vehiculos
- OpenAPI reservas: http://localhost:8080/docs/reservas
- OpenAPI pagos: http://localhost:8080/docs/pagos
- OpenAPI reportes: http://localhost:8080/docs/reportes
- OpenAPI empleados: http://localhost:8080/docs/empleados
- OpenAPI sucursales: http://localhost:8080/docs/sucursales

DataFaker

El perfil predeterminado es dev en clientes, vehiculos y reservas. DataFaker completa cada base hasta la cantidad indicada por:

app.data-faker.enabled=true
app.data-faker.records=10

No agrega diez registros en cada inicio: solo crea los necesarios para alcanzar el total configurado. En reservas, los datos de cliente y vehiculo se consultan desde sus microservicios de origen.

Pruebas

Ejecutar dentro de cada modulo:

.\mvnw.cmd test

Las pruebas usan JUnit 5, Mockito y una base H2 temporal. Eureka y DataFaker externo estan desactivados en el perfil de pruebas.

Problemas frecuentes

Puerto ocupado

Get-NetTCPConnection -LocalPort 8083 -State Listen

Swagger del Gateway devuelve 404

Reiniciar el API Gateway despues de modificar application.yml y comprobar que este configurado:

springdoc:
  api-docs:
    enabled: true

Error UnsupportedClassVersionError

El comando java esta utilizando una version antigua. Configurar el IDE, Maven y PATH con Java 17 o superior.

Comprobar que MySQL este activo y que existan prueba1, prueba2, prueba3 y prueba4.
