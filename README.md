# Product Service

Descripción
-----------
Microservicio de gestión y consulta de productos. Expone endpoints REST para listar, obtener y administrar productos.

Puerto
------
Por defecto el servicio usa el puerto 8081. La configuración por defecto está en `src/main/resources/application.properties`.

Arrancar localmente (desarrollador)
----------------------------------
Desde la raíz del proyecto:

Windows (cmd.exe):

```cmd
cd C:\Users\MASTER\Desktop\SpringWebFlux\workspace\product-service
.\mvnw.cmd spring-boot:run
```

O ejecutar el JAR empaquetado:

```cmd
cd C:\Users\MASTER\Desktop\SpringWebFlux\workspace\product-service
.\mvnw.cmd -DskipTests package
java -jar target\product-service-0.0.1-SNAPSHOT.jar
```

Forzar puerto diferente (anular configuración):

```cmd
java -Dserver.port=9091 -jar target\product-service-0.0.1-SNAPSHOT.jar
```

Docker
------
Si quieres construir la imagen Docker (imagen multistage recomendada):

```bash
# desde la carpeta product-service
docker build -t product-service:latest .
# ejecutar exponiendo el puerto 8081
docker run -p 8081:8081 --env SERVER_PORT=8081 --env SPRING_PROFILES_ACTIVE=prod product-service:latest
```

Endpoints (ejemplos)
--------------------
- GET /products — listar productos
- GET /products/{id} — obtener producto por id
- POST /products — crear producto (payload JSON)

(Revisa los controladores del proyecto para rutas y payloads exactos si necesitas ejemplos precisos.)

Tests
-----
Ejecutar tests unitarios con Maven Wrapper:

```cmd
.\mvnw.cmd test
```

Recomendaciones y mejoras
-------------------------
- Añadir validaciones (DTOs + `@Valid`) para entradas y tests MockMvc para validar errores 400/200.
- Añadir tests de integración con Testcontainers para validar la integración con la base de datos.
- Documentar los endpoints con OpenAPI/Swagger si no está ya configurado.
- Añadir un `Docker HEALTHCHECK` o `actuator/health` para facilitar orquestación y readiness/liveness checks.
