# 1. Usar la imagen oficial de Java 21
FROM eclipse-temurin:21-jdk-alpine

# 2. Crear una carpeta de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar el archivo .jar compilado desde tu carpeta target al contenedor
COPY target/*.jar app.jar

# 4. Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]