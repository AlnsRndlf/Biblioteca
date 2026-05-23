========================================================================
🚀 MICROSERVICIO: penalty
========================================================================

DESCRIPCION:
Este microservicio es responsable de: Gestionar las multas y sanciones aplicadas a los usuarios por devoluciones tardias o infracciones.

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
3. Crea una base de datos VACIA llamada exactamente: db_penalties

NOTA IMPORTANTE: 
No debes crear tablas manualmente ni ejecutar scripts .sql. 
El microservicio utiliza LIQUIBASE. Al levantar la aplicacion por primera
vez, las tablas y estructuras se crearan de manera automatica.

------------------------------------------------------------------------
📝 3. ARCHIVO ARCHIVO application.properties
------------------------------------------------------------------------
Asegurate de que el archivo ubicado en 'src/main/resources/application.properties'
tenga la siguiente estructura adaptada para este entorno:

spring.application.name=penalty
server.port=8086

# Configuracion de Conexion a Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/db_penalties
spring.datasource.username=root
spring.datasource.password=

# Configuracion de Liquibase y ORM JPA
spring.jpa.hibernate.ddl-auto=none
spring.liquibase.change-log=classpath:db/changelog/changelog-master.xml
spring.jpa.show-sql=true

------------------------------------------------------------------------
🔗 4. DEPENDENCIAS Y ORDEN DE EJECUCION
------------------------------------------------------------------------
Para que este microservicio funcione correctamente (especialmente en flujos
de creacion o validacion mediante OpenFeign), se requiere:

- ms-user (Puerto 8082) -> Para identificar y asociar la multa al usuario.
- ms-loan (Puerto 8083) -> Para identificar el prestamo vencido que gatillo la sancion.

RECOMENDACION: Se sugiere levantar primero los servicios base (ms-user, ms-book)
antes de levantar los servicios relacionales (ms-loan, ms-review, ms-penalty).

------------------------------------------------------------------------
▶️ 5. PASOS PARA EJECUTAR EL PROYECTO
------------------------------------------------------------------------
1. Importa el proyecto Maven en tu IDE.
2. Espera a que el IDE descargue todas las dependencias del pom.xml.
3. Presiona el boton del Martillo (Build) para compilar y asegurar que no haya errores.
4. Abre la clase ejecutable principal (la que contiene la anotacion @SpringBootApplication).
5. Dale al boton "Run" (Play).
6. Monitorea la consola de Laragon/IDE para confirmar que levanto exitosamente en el puerto 8087.

------------------------------------------------------------------------
📡 6. ENDPOINTS DISPONIBLES (PROBAR EN POSTMAN)
------------------------------------------------------------------------
La URL base para consumir este servicio localmente sera: 
http://localhost:8086

Rutas expuestas:
  * GET  /api/v1/multas             - Obtener todas las multas/sanciones
  * GET  /api/v1/multas/user/{rut } - Obtener todas las multas de un rut especifico
  * POST /api/v1/multas             - Registrar una nueva multa en el sistema