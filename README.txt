Sistema de arriendo de vehículos

Proyecto de microservicios desarrollado con Spring Boot para administrar clientes, vehículos y reservas. Incluye descubrimiento de servicios con Eureka, comunicación mediante OpenFeign, API Gateway, Swagger/OpenAPI, HATEOAS, DataFaker, Flyway y pruebas con JUnit y Mockito.

Arquitectura

Aplicación | Puerto | Función
Eureka Server | 8761 | Registro y descubrimiento de servicios
API Gateway | 8080 | Punto de entrada central y Swagger agregado
ms-clientes | 8081 | Gestión de clientes y direcciones
ms-vehiculos | 8082 | Gestión de vehículos y categorías
ms-reservas | 8083 | Gestión de reservas y estados

Las rutas /api/v1/** entregan respuestas JSON estándar. Las rutas /api/v2/** mantienen la misma lógica e incorporan enlaces HATEOAS.

Requisitos

- Java 17 o superior.
- Maven Wrapper incluido en cada módulo.
- MySQL disponible en localhost:3306.
- Bases de datos prueba1 y prueba2.

Verificar la versión de Java:

java -version
$env:JAVA_HOME

Si java -version muestra Java 8, usar el JDK configurado en JAVA_HOME:

$env:Path="$env:JAVA_HOME\bin;$env:Path"

Bases de datos

Crear las bases antes de iniciar los microservicios:

CREATE DATABASE IF NOT EXISTS prueba1;
CREATE DATABASE IF NOT EXISTS prueba2;

- prueba1: clientes y vehículos.
- prueba2: reservas.

Las credenciales predeterminadas son usuario root y contraseña vacía. Pueden cambiarse mediante DB_HOST, DB_PORT, DB_NAME, DB_USERNAME y DB_PASSWORD.

Orden de inicio

Iniciar cada aplicación en una terminal distinta:

1. MySQL.
2. Eureka Server.
3. ms-clientes.
4. ms-vehiculos.
5. ms-reservas.
6. API Gateway.

Comando de inicio dentro de cada módulo:

.\mvnw.cmd spring-boot:run

Rutas de los módulos:

eureka-server
ms-clientes\ms-clientes
ms-vehiculos\ms-vehiculos
ms-reservas\ms-reservas
api-gateway

Reservas obtiene mediante OpenFeign el nombre y correo del cliente, además del nombre del vehículo. Por esta razón, Eureka, clientes y vehículos deben estar disponibles para sincronizar correctamente esos datos.

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

Vehículos y categorías

- Vehículos V1: http://localhost:8080/api/v1/vehiculos
- Vehículo V1 por ID: http://localhost:8080/api/v1/vehiculos/1
- Vehículos V2: http://localhost:8080/api/v2/vehiculos
- Vehículo V2 por ID: http://localhost:8080/api/v2/vehiculos/1
- Vehículos disponibles bajo $50.000: http://localhost:8080/api/v1/vehiculos/disponibles/precio-menor/50000
- Categorías V1: http://localhost:8080/api/v1/categorias
- Categorías V2: http://localhost:8080/api/v2/categorias

Reservas y estados

- Reservas V1: http://localhost:8080/api/v1/reservas
- Reserva V1 por ID: http://localhost:8080/api/v1/reservas/1
- Reservas V2: http://localhost:8080/api/v2/reservas
- Reserva V2 por ID: http://localhost:8080/api/v2/reservas/1
- Reservas desde una fecha: http://localhost:8080/api/v1/reservas/desde-fecha?fecha=2026-06-21
- Estados de reserva V1: http://localhost:8080/api/v1/estados-reserva
- Estados de reserva V2: http://localhost:8080/api/v2/estados-reserva

Acceso directo a cada microservicio

Swagger

- Swagger de clientes: http://localhost:8081/doc/swagger-ui.html
- Swagger de vehículos: http://localhost:8082/doc/swagger-ui.html
- Swagger de reservas: http://localhost:8083/doc/swagger-ui.html

Endpoints directos

- Clientes directos: http://localhost:8081/api/v1/clientes
- Vehículos directos: http://localhost:8082/api/v1/vehiculos
- Reservas directas: http://localhost:8083/api/v1/reservas

Documentos OpenAPI

El Gateway agrega las especificaciones de los microservicios en estas rutas:

- OpenAPI clientes: http://localhost:8080/docs/clientes
- OpenAPI vehículos: http://localhost:8080/docs/vehiculos
- OpenAPI reservas: http://localhost:8080/docs/reservas

DataFaker

El perfil predeterminado es dev. DataFaker completa cada base hasta la cantidad indicada por:

app.data-faker.enabled=true
app.data-faker.records=10

No agrega diez registros en cada inicio: solo crea los necesarios para alcanzar el total configurado. En reservas, los datos de cliente y vehículo se consultan desde sus microservicios de origen.

Pruebas

Ejecutar dentro de cada módulo:

.\mvnw.cmd test

Las pruebas usan JUnit 5, Mockito y una base H2 temporal. Eureka y DataFaker externo están desactivados en el perfil de pruebas.

Problemas frecuentes

Puerto ocupado

Get-NetTCPConnection -LocalPort 8083 -State Listen

Swagger del Gateway devuelve 404

Reiniciar el API Gateway después de modificar application.yml y comprobar que esté configurado:

springdoc:
  api-docs:
    enabled: true

Error UnsupportedClassVersionError

El comando java está utilizando una versión antigua. Configurar el IDE, Maven y PATH con Java 17 o superior.

Comprobar que MySQL esté activo, que exista prueba2 y que las migraciones Flyway no estén marcadas como fallidas.
