# ============================
# Etapa 1: Construcción con Maven
# ============================
FROM maven:3.9.8-eclipse-temurin-21 AS builder

# Crear carpeta de trabajo
WORKDIR /app

# Copiar los archivos de configuración primero (mejora cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente
COPY src ./src

# Compilar y generar el .jar
RUN mvn clean package -DskipTests

# ============================
# Etapa 2: Imagen final ligera
# ============================
FROM eclipse-temurin:21-jre-alpine

# Crear carpeta para la app
WORKDIR /app

# Copiar el jar generado desde la etapa builder
COPY --from=builder /app/target/product-service-*.jar app.jar

# Exponer el puerto del microservicio
EXPOSE 8081

# Variables de entorno opcionales
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
