# 📚 Sistema de Gestión de Biblioteca - Arquitectura de Microservicios

Este proyecto es una plataforma de gestión bibliotecaria construida bajo una arquitectura de microservicios con Spring Boot, Spring Cloud y contenedores Docker. Utiliza un API Gateway como punto de entrada único, Eureka como servidor de descubrimiento, y bases de datos relacionales independientes por servicio.

## ⚙️ Arquitectura y Puertos

El ecosistema se compone de 10 microservicios. Todos los servicios de negocio están protegidos y enrutados a través del API Gateway.

| Microservicio | Puerto Docker | Base de Datos (MySQL) | Descripción |
| :--- | :--- | :--- | :--- |
| **ms-Eureka** | `8761` | N/A | Servidor de descubrimiento de servicios. |
| **ms-Gateway** | `8080` | N/A | Puerta de entrada, enrutamiento y validación JWT. |
| **ms-Auth** | `8089` | `db_auth` | Emisión y validación de tokens de seguridad. |
| **ms-User** | `8082` | `db_user` | Gestión de usuarios del sistema. |
| **ms-Book** | `8081` | `db_book` | Catálogo e inventario de libros. |
| **ms-Loan** | `8083` | `db_loan` | Gestión de préstamos y devoluciones. |
| **ms-Reservation**| `8084` | `db_reservation` | Sistema de listas de espera y reservas. |
| **ms-Penalty** | `8085` | `db_penalty` | Registro de multas por atrasos. |
| **ms-Review** | `8086` | `db_review` | Reseñas y calificaciones de libros. |
| **ms-Notification**| `8087` | N/A | Servicio de mensajería asíncrona. |

---

## 🛠️ Prerrequisitos del Sistema

Para ejecutar este proyecto en un entorno local, el equipo debe contar con:
1. **Java 21** y **Maven**.
2. **Docker Desktop** (con WSL 2 habilitado en Windows).
3. **Laragon**, XAMPP o MySQL Server instalado localmente (Puerto `3306`).
   * *Nota:* El proyecto utiliza `host-gateway` en Docker para conectarse al motor de base de datos del equipo anfitrión.

---

## 🚀 Guía de Despliegue (Paso a Paso)

### Paso 1: Configuración de Base de Datos (Laragon)
Inicie los servicios de MySQL en Laragon y ejecute el siguiente script SQL en su gestor preferido (HeidiSQL, DBeaver, Workbench) para crear las bases de datos necesarias antes de levantar los servicios:

```sql
CREATE DATABASE db_auth;
CREATE DATABASE db_user;
CREATE DATABASE db_book;
CREATE DATABASE db_loan;
CREATE DATABASE db_reservation;
CREATE DATABASE db_penalty;
CREATE DATABASE db_review;
```
*(Hibernate/Liquibase se encargarán de crear las tablas y esquemas automáticamente al iniciar).*

### Paso 2: Generar los Ejecutables (.jar)
Abra una terminal en la raíz del proyecto. Debe compilar cada microservicio para generar su archivo ejecutable. 
Puede navegar a cada carpeta y ejecutar `mvn clean package -DskipTests`, o usar el siguiente comando de PowerShell desde la carpeta raíz para compilarlos todos en lote:

```powershell
Get-ChildItem -Directory -Filter "ms-*" | ForEach-Object { Write-Host "Construyendo $_..."; Set-Location $_.FullName; mvn clean package -DskipTests; Set-Location .. }
```

### Paso 3: Levantar la Infraestructura con Docker
Una vez generados los 10 archivos `.jar`, asegúrese de tener Docker Desktop abierto y ejecute el siguiente comando en la raíz del proyecto para construir y levantar los contenedores en segundo plano:

```powershell
docker compose up --build -d
```

### Paso 4: Verificación
1. Espere un aproximado de **1 a 2 minutos** para que todos los servicios inicien y se registren.
2. Abra su navegador en `http://localhost:8761`.
3. Compruebe en el panel de **Eureka** que los servicios de negocio aparezcan con el estado **UP**.

---

## 🧪 Instrucciones de Prueba (API Gateway)

Todas las peticiones deben dirigirse al puerto **8080** (API Gateway). 

1. **Obtener Token:** Realizar petición POST a `http://localhost:8080/api/v1/auth/login` (o el endpoint correspondiente) para obtener el JWT.
2. **Realizar Peticiones:** Enviar el token generado en los Headers bajo la clave `Authorization: Bearer <token>` para consumir los microservicios de la biblioteca (ej: `http://localhost:8080/api/v1/libros`).

---

## 🛑 Detener el Sistema
Para apagar los microservicios sin eliminar las imágenes construidas, ejecute en la raíz del proyecto:

```powershell
docker compose down
```