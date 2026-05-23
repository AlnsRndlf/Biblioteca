========================================================================
🚀 MICROSERVICIO: ms-user
========================================================================

DESCRIPCION:
Este microservicio es responsable de: Gestionar los usuarios (estudiantes/lectores) de la biblioteca, sus datos personales y estados.

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
3. Crea una base de datos VACIA llamada exactamente: db_users

NOTA IMPORTANTE: 
No debes crear tablas manualmente ni ejecutar scripts .sql. 
El microservicio utiliza LIQUIBASE. Al levantar la aplicacion por primera
vez, las tablas y estructuras se crearan de manera automatica.

------------------------------------------------------------------------
📝 3. ARCHIVO ARCHIVO application.properties
------------------------------------------------------------------------
Asegurate de que el archivo ubicado en 'src/main/resources/application.properties'
tenga la siguiente estructura adaptada para este entorno:

spring.application.name=ms-user
server.port=8082

# Configuracion de Conexion a Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/db_users
spring.datasource.username=root
spring.datasource.password=

# Configuracion de Liquibase y ORM JPA
spring.jpa.hibernate.ddl-auto=none
spring.liquibase.change-log=classpath:db/changelog/changelog-master.xml
spring.jpa.show-sql=true

------------------------------------------------------------------------
🔗 4. DEPENDENCIAS Y ORDEN DE EJECUCION
------------------------------------------------------------------------

No aplica porque es un servicio base (no utiliza OpenFeign)

------------------------------------------------------------------------
▶️ 5. PASOS PARA EJECUTAR EL PROYECTO
------------------------------------------------------------------------
1. Importa el proyecto Maven en tu IDE.
2. Espera a que el IDE descargue todas las dependencias del pom.xml.
3. Presiona el boton del Martillo (Build) para compilar y asegurar que no haya errores.
4. Abre la clase ejecutable principal (la que contiene la anotacion @SpringBootApplication).
5. Dale al boton "Run" (Play).
6. Monitorea la consola de Laragon/IDE para confirmar que levanto exitosamente en el puerto 8080.

------------------------------------------------------------------------
📡 6. ENDPOINTS DISPONIBLES (PROBAR EN POSTMAN)
------------------------------------------------------------------------
La URL base para consumir este servicio localmente sera: 
http://localhost:8082

Rutas expuestas:
  * GET    /api/v1/users                                  - Obtener todos los usuarios
  * GET    /api/v1/users/{rut}                            - Obtener un usuario especifico por su RUT
  * GET    /api/v1/users/email/{email}                    - Obtener un usuario especifico por email
  * POST   /api/v1/users                                  - Registrar un nuevo usuario
  * PATCH  /api/v1/users/{rut}/cambioNombre/{newFullName} - Cambia el nombre a un usuario según rut
  * PATCH  /api/v1/users/{rut}/cambioEmail/{newEmail}     - Cambia el email a un usuario según rut
  * DELETE /api/v1/users{rut}                             - Elimina a un usuario según rut

