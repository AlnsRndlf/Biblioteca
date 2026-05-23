========================================================================
🚀 MICROSERVICIO: ms-book
========================================================================

DESCRIPCION:
Este microservicio es responsable de: Gestionar el catalogo de libros de la biblioteca, titulos, autores, stock e ISBNs.

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
3. Crea una base de datos VACIA llamada exactamente: db_books

NOTA IMPORTANTE: 
No debes crear tablas manualmente ni ejecutar scripts .sql. 
El microservicio utiliza LIQUIBASE. Al levantar la aplicacion por primera
vez, las tablas y estructuras se crearan de manera automatica.

------------------------------------------------------------------------
📝 3. ARCHIVO ARCHIVO application.properties
------------------------------------------------------------------------
Asegurate de que el archivo ubicado en 'src/main/resources/application.properties'
tenga la siguiente estructura adaptada para este entorno:

spring.application.name=ms-book
server.port=8081

# Configuracion de Conexion a Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/db_books
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
6. Monitorea la consola de Laragon/IDE para confirmar que levanto exitosamente en el puerto 8081.

------------------------------------------------------------------------
📡 6. ENDPOINTS DISPONIBLES (PROBAR EN POSTMAN)
------------------------------------------------------------------------
La URL base para consumir este servicio localmente sera: 
http://localhost:8081

Rutas expuestas:
  * GET    /api/v1/books         - Obtener todos los libros del catalogo
  * GET    /api/v1/books/{isbn}  - Obtener un libro especifico por su ISBN
  * GET    /api/v1/books/{title} - Obtener un libro especifico por su titulo
  * POST   /api/v1/books         - Registrar un nuevo libro en el catalogo
  * DELETE /api/v1/books/{isbn}  - Eliminar un libro especifico por su ISBN

