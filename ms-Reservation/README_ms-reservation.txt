========================================================================
🚀 MICROSERVICIO: ms-reservation
========================================================================

DESCRIPCION:
Este microservicio es responsable de: Gestionar las reservas de libros cuando no hay stock disponible. Permite a los usuarios quedar en lista de espera.

------------------------------------------------------------------------
📋 1. PRE-REQUISITOS DE ENTORNO
------------------------------------------------------------------------
* Java JDK 26
* IDE con soporte Maven (IntelliJ IDEA, Eclipse, VS Code)
* Laragon con el servicio MySQL encendido (Puerto 3306)

------------------------------------------------------------------------
⚙️ 2. CONFIGURACION DE LA BASE DE DATOS (LARAGON)
------------------------------------------------------------------------
1. Abre Laragon y presiona "Iniciar Todo".
2. Abre tu herramienta de bases de datos (HeidiSQL, phpMyAdmin, etc.).
3. Crea una base de datos VACIA llamada exactamente: db_reservations

NOTA IMPORTANTE: 
No debes crear tablas manualmente ni ejecutar scripts .sql. 
El microservicio utiliza LIQUIBASE. Al levantar la aplicacion por primera
vez, las tablas y estructuras se crearan de manera automatica.

------------------------------------------------------------------------
📝 3. ARCHIVO ARCHIVO application.properties
------------------------------------------------------------------------
Asegurate de que el archivo ubicado en 'src/main/resources/application.properties'
tenga la siguiente estructura adaptada para este entorno (¡Verifica si cambiaste el puerto o la DB!):

spring.application.name=ms-reservation
server.port=8085

# Configon de Conexion a Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/db_reservations
spring.datasource.username=root
spring.datasource.password=

# Configon de Liquibase y ORM JPA
spring.jpa.hibernate.ddl-auto=none
spring.liquibase.change-log=classpath:db/changelog/changelog-master.xml
spring.jpa.show-sql=true

------------------------------------------------------------------------
🔗 4. DEPENDENCIAS Y ORDEN DE EJECUCION
------------------------------------------------------------------------
Para que este microservicio funcione correctamente (especialmente en flujos
de creacion o validacion mediante OpenFeign), se requiere:

- ms-user (Puerto 8082) -> Para validar que el usuario que reserva exista.
- ms-book (Puerto 8081) -> Para validar el libro y verificar si realmente no tiene stock.

RECOMENDACION: Se sugiere levantar primero los servicios base (ms-user, ms-book)
antes de levantar este servicio.

------------------------------------------------------------------------
▶️ 5. PASOS PARA EJECUTAR EL PROYECTO
------------------------------------------------------------------------
1. Importa el proyecto Maven en tu IDE.
2. Espera a que el IDE descargue todas las dependencias del pom.xml.
3. Presiona el boton del Martillo (Build) para compilar y asegurar que no haya errores.
4. Abre la clase ejecutable principal (la que contiene la anotacion @SpringBootApplication).
5. Dale al boton "Run" (Play).
6. Monitorea la consola de Laragon/IDE para confirmar que levanto exitosamente en el puerto 8085.

------------------------------------------------------------------------
📡 6. ENDPOINTS DISPONIBLES (PROBAR EN POSTMAN)
------------------------------------------------------------------------
La URL base para consumir este servicio localmente sera: 
http://localhost:8085

Rutas expuestas:
  * GET   /api/v1/reservas               - Obtener todas las reservas
  * GET   /api/v1/reservas/{id}          - Obtener una reserva especifica por ID
  * POST  /api/v1/reservas               - Crear una nueva reserva (lista de espera)
  * Patch /api/v1/reservas/{id}/{status} - actualiza el estado de un libro según id