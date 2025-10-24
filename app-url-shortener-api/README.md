# URL Shortener API

Módulo principal de la API REST para el servicio de acortamiento de URLs. Este módulo es responsable de resolver y redirigir URLs cortas a sus destinos originales.

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Componentes Principales](#componentes-principales)
- [Flujo de Datos](#flujo-de-datos)
- [Tecnologías](#tecnologías)
- [Configuración](#configuración)
- [API Endpoints](#api-endpoints)
- [Manejo de Errores](#manejo-de-errores)
- [Caching](#caching)
- [Métricas y Monitoreo](#métricas-y-monitoreo)
- [Despliegue](#despliegue)

## Descripción General

El módulo `app-url-shortener-api` es una aplicación Spring Boot que proporciona un servicio de redirección de URLs. Cuando un usuario accede a una URL corta (por ejemplo, `http://servicio.com/abc123`), el sistema busca la URL original asociada a esa clave y redirige al usuario al destino correspondiente.

### Características Principales

- Redirección HTTP 302 a URLs originales
- Caché distribuida con Redis para alto rendimiento
- Persistencia en MongoDB
- Manejo gracioso de errores con redirección fallback
- Métricas y observabilidad con Micrometer/Prometheus
- Arquitectura hexagonal (puertos y adaptadores)
- Validación de URLs habilitadas/deshabilitadas

## Arquitectura

El proyecto sigue una **Arquitectura Hexagonal** (también conocida como Puertos y Adaptadores), que proporciona una separación clara entre la lógica de negocio y los detalles de infraestructura.

### Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                     │
│  ┌────────────────────────────────────────────────────┐     │
│  │        UrlShortenerController (REST)               │     │
│  │        UrlExceptionHandler (Error Handler)         │     │
│  └────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE APLICACIÓN                       │
│  ┌────────────────────────────────────────────────────┐     │
│  │              UrlService (Casos de Uso)             │     │
│  └────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                      CAPA DE DOMINIO                        │
│  ┌────────────────────────────────────────────────────┐     │
│  │  UrlUsesCases (Puerto Entrada)                     │     │
│  │  UrlRepository (Puerto Salida)                     │     │
│  │  RedirectBaseUrlProperty (Puerto Entrada)          │     │
│  └────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  CAPA DE INFRAESTRUCTURA                    │
│  ┌────────────────────────────────────────────────────┐     │
│  │  MongoRepositoryAdapter (Adaptador Persistencia)   │     │
│  │  UrlMongoRepository (Spring Data)                  │     │
│  │  UrlProperties (Configuración)                     │     │
│  │  UrlDocumentStringConverter (Converter)            │     │
│  └────────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ↓
                   ┌──────────────────┐
                   │  MongoDB + Redis │
                   └──────────────────┘
```

### Principios Arquitectónicos

1. **Inversión de Dependencias**: Las capas internas no dependen de las externas. Los puertos (interfaces) definen contratos que los adaptadores implementan.

2. **Separación de Responsabilidades**:
   - **Domain**: Define la lógica de negocio y los contratos (puertos)
   - **Application**: Implementa los casos de uso
   - **Infrastructure**: Adaptadores para persistencia, API REST, configuración

3. **Testabilidad**: La arquitectura facilita pruebas unitarias mockeando las interfaces de los puertos.

## Estructura del Proyecto

```
app-url-shortener-api/
├── src/
│   ├── main/
│   │   ├── java/com/github/aaivan28/url/shortener/
│   │   │   ├── UrlShortenerApplication.java
│   │   │   ├── application/
│   │   │   │   ├── exception/
│   │   │   │   │   └── UrlNotFoundException.java
│   │   │   │   └── service/
│   │   │   │       └── UrlService.java
│   │   │   ├── domain/
│   │   │   │   └── port/
│   │   │   │       ├── inbound/
│   │   │   │       │   ├── properties/
│   │   │   │       │   │   └── RedirectBaseUrlProperty.java
│   │   │   │       │   └── usescases/
│   │   │   │       │       └── UrlUsesCases.java
│   │   │   │       └── outbound/
│   │   │   │           └── UrlRepository.java
│   │   │   └── infrastructure/
│   │   │       ├── adapter/
│   │   │       │   ├── converter/
│   │   │       │   │   └── UrlDocumentStringConverter.java
│   │   │       │   ├── inbound/
│   │   │       │   │   ├── handler/
│   │   │       │   │   │   └── UrlExceptionHandler.java
│   │   │       │   │   ├── properties/
│   │   │       │   │   │   └── UrlProperties.java
│   │   │       │   │   └── rest/
│   │   │       │   │       └── UrlShortenerController.java
│   │   │       │   └── outbound/
│   │   │       │       └── persistence/
│   │   │       │           ├── MongoRepositoryAdapter.java
│   │   │       │           ├── UrlDocument.java
│   │   │       │           └── UrlMongoRepository.java
│   │   │       └── configuration/
│   │   │           ├── ApplicationConfiguration.java
│   │   │           ├── ConverterConfiguration.java
│   │   │           ├── MetricsConfiguration.java
│   │   │           ├── MongoConfiguration.java
│   │   │           └── PropertiesConfiguration.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       └── java/com/github/aaivan28/url/shortener/
│           ├── application/service/
│           ├── infrastructure/adapter/
│           │   ├── converter/
│           │   ├── inbound/
│           │   └── outbound/
├── Dockerfile
└── pom.xml
```

## Componentes Principales

### 1. Capa de Dominio

#### UrlUsesCases (Puerto de Entrada)
**Ubicación**: `domain/port/inbound/usescases/UrlUsesCases.java:4`

Interface que define el caso de uso principal:
```java
public interface UrlUsesCases {
    String getUrl(final String urlKey);
}
```

#### UrlRepository (Puerto de Salida)
**Ubicación**: `domain/port/outbound/UrlRepository.java:6`

Interface que define el contrato para la persistencia:
```java
public interface UrlRepository {
    Optional<String> getUrl(final String urlKey);
}
```

#### RedirectBaseUrlProperty (Puerto de Entrada)
**Ubicación**: `domain/port/inbound/properties/RedirectBaseUrlProperty.java:4`

Interface para obtener la URL de fallback:
```java
public interface RedirectBaseUrlProperty {
    String redirectBaseUrl();
}
```

### 2. Capa de Aplicación

#### UrlService
**Ubicación**: `application/service/UrlService.java:14`

Implementa el caso de uso `UrlUsesCases`. Recupera la URL original desde el repositorio y lanza una excepción si no existe:

```java
@Override
public String getUrl(final String urlKey) {
    return this.repository.getUrl(urlKey)
        .orElseThrow(() -> new UrlNotFoundException("Key not found: " + urlKey));
}
```

#### UrlNotFoundException
**Ubicación**: `application/exception/UrlNotFoundException.java:1`

Excepción personalizada que se lanza cuando una clave no existe en el sistema.

### 3. Capa de Infraestructura

#### UrlShortenerController
**Ubicación**: `infrastructure/adapter/inbound/rest/UrlShortenerController.java:22`

Controlador REST que maneja las peticiones de redirección:

```java
@GetMapping("/{urlKey}")
@Timed(value = "url.shortener.api.controller.redirect",
       description = "Redirect to the original URL")
public void redirect(HttpServletResponse response,
                    @PathVariable("urlKey") String urlKey) throws IOException {
    response.sendRedirect(this.urlUsesCases.getUrl(urlKey));
}
```

**Características**:
- Endpoint: `GET /{urlKey}`
- Respuesta: HTTP 302 (Redirect)
- Métricas: Anotado con `@Timed` para observabilidad

#### UrlExceptionHandler
**Ubicación**: `infrastructure/adapter/inbound/handler/UrlExceptionHandler.java:21`

Maneja globalmente las excepciones `UrlNotFoundException` y redirige a una URL base configurada:

```java
@ExceptionHandler({UrlNotFoundException.class})
public void KeyNotFoundExceptionHandler(HttpServletResponse response,
                                       UrlNotFoundException exception) throws IOException {
    log.debug("Key not found: {}", exception.getMessage());
    response.sendRedirect(this.redirectBaseUrlProperty.redirectBaseUrl());
}
```

#### MongoRepositoryAdapter
**Ubicación**: `infrastructure/adapter/outbound/persistence/MongoRepositoryAdapter.java:18`

Adaptador que implementa `UrlRepository` usando Spring Data MongoDB:

```java
@Override
public Optional<String> getUrl(final String urlKey) {
    return this.repository.findByKey(urlKey)
            .filter(UrlDocument::isEnabled)
            .map(this::extractUrl);
}
```

**Características**:
- Filtra URLs deshabilitadas (`isEnabled` flag)
- Usa `ConversionService` para convertir `UrlDocument` a `String`

#### UrlMongoRepository
**Ubicación**: `infrastructure/adapter/outbound/persistence/UrlMongoRepository.java:12`

Repository de Spring Data con caché integrado:

```java
@Cacheable(value = "url", key = "#key")
Optional<UrlDocument> findByKey(final String key);
```

#### UrlDocument
**Ubicación**: `infrastructure/adapter/outbound/persistence/UrlDocument.java:16`

Entidad de MongoDB que representa una URL acortada:

```java
@Document(UrlDocument.COLLECTION_NAME)
public class UrlDocument {
    String id;
    @Indexed(unique = true)
    String key;
    String description;
    String url;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean enabled;
}
```

**Características**:
- Colección: `shortened_url`
- Índice único en el campo `key`
- Flag `enabled` para activar/desactivar URLs

#### UrlDocumentStringConverter
**Ubicación**: `infrastructure/adapter/converter/UrlDocumentStringConverter.java:12`

Converter que extrae la URL del documento:

```java
@Override
public String convert(final UrlDocument urlDocument) {
    return urlDocument.getUrl();
}
```

#### UrlProperties
**Ubicación**: `infrastructure/adapter/inbound/properties/UrlProperties.java:7`

Record que implementa `RedirectBaseUrlProperty` y mapea la configuración:

```java
@ConfigurationProperties(prefix = "github.aaivan28.url-shortener")
public record UrlProperties(String redirectBaseUrl)
    implements RedirectBaseUrlProperty {
}
```

## Flujo de Datos

### Flujo de Redirección Exitosa

```
1. Cliente → GET /{urlKey}
                ↓
2. UrlShortenerController recibe la petición
                ↓
3. Invoca UrlUsesCases.getUrl(urlKey)
                ↓
4. UrlService consulta UrlRepository.getUrl(urlKey)
                ↓
5. MongoRepositoryAdapter busca en caché Redis
                ↓
6. Cache Miss → Consulta UrlMongoRepository.findByKey(urlKey)
                ↓
7. MongoDB devuelve UrlDocument
                ↓
8. Filtra si enabled=true
                ↓
9. UrlDocumentStringConverter extrae la URL
                ↓
10. Resultado cacheable guardado en Redis
                ↓
11. URL devuelta al Controller
                ↓
12. Controller → response.sendRedirect(url)
                ↓
13. Cliente ← HTTP 302 Location: {url_original}
```

### Flujo de Error (URL No Encontrada)

```
1. Cliente → GET /{urlKey_invalida}
                ↓
2. UrlShortenerController recibe la petición
                ↓
3. Invoca UrlUsesCases.getUrl(urlKey)
                ↓
4. UrlService consulta UrlRepository.getUrl(urlKey)
                ↓
5. MongoRepositoryAdapter → Optional.empty()
                ↓
6. UrlService lanza UrlNotFoundException
                ↓
7. UrlExceptionHandler captura la excepción
                ↓
8. Handler → response.sendRedirect(redirectBaseUrl)
                ↓
9. Cliente ← HTTP 302 Location: https://www.google.com
```

## Tecnologías

### Framework y Librerías

- **Java 21**: Lenguaje de programación
- **Spring Boot 3.x**: Framework principal
  - Spring Web: API REST
  - Spring Data MongoDB: Persistencia
  - Spring AOP: Aspectos para métricas
  - Spring Actuator: Health checks y endpoints de gestión
  - Spring Cache: Abstracción de caché
- **Lombok**: Reducción de boilerplate
- **Micrometer**: Métricas y observabilidad
  - Micrometer Prometheus: Export de métricas

### Infraestructura

- **MongoDB**: Base de datos NoSQL para persistencia
- **Redis**: Caché distribuida
- **Maven**: Gestión de dependencias y build
- **Docker**: Contenedorización

### Dependencias Personalizadas

- **cache-redis** (`lib-cache-redis`): Librería interna para configuración de Redis

## Configuración

### application.yaml

**Ubicación**: `src/main/resources/application.yaml`

```yaml
server:
  port: 8080

spring:
  cache:
    type: redis
    redis:
      enable-statistics: true
      key-prefix: url-shortener-cache-
      use-key-prefix: true
      cache-null-values: false
      time-to-live: 1H

  data:
    redis:
      host: localhost
      port: 6379
      database: 0

    mongodb:
      host: localhost
      port: 27017
      database: url-shortener
      auto-index-creation: true

github:
  aaivan28:
    url-shortener:
      redirect-base-url: https://www.google.com

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### Parámetros de Configuración

| Parámetro | Descripción | Default |
|-----------|-------------|---------|
| `server.port` | Puerto del servidor HTTP | 8080 |
| `spring.cache.type` | Tipo de caché | redis |
| `spring.cache.redis.time-to-live` | TTL del caché | 1H |
| `spring.data.redis.host` | Host de Redis | localhost |
| `spring.data.redis.port` | Puerto de Redis | 6379 |
| `spring.data.mongodb.host` | Host de MongoDB | localhost |
| `spring.data.mongodb.port` | Puerto de MongoDB | 27017 |
| `spring.data.mongodb.database` | Base de datos | url-shortener |
| `github.aaivan28.url-shortener.redirect-base-url` | URL fallback | https://www.google.com |

### Variables de Entorno

Puedes sobrescribir la configuración usando variables de entorno:

```bash
SERVER_PORT=9090
SPRING_DATA_MONGODB_HOST=mongo-server
SPRING_DATA_MONGODB_PORT=27017
SPRING_DATA_REDIS_HOST=redis-server
SPRING_DATA_REDIS_PORT=6379
GITHUB_AAIVAN28_URL_SHORTENER_REDIRECT_BASE_URL=https://example.com
```

## API Endpoints

### Redirección de URL

```http
GET /{urlKey}
```

**Descripción**: Redirige a la URL original asociada a la clave proporcionada.

**Parámetros**:
- `urlKey` (path): Clave única de la URL acortada

**Respuestas**:

| Código | Descripción | Location Header |
|--------|-------------|-----------------|
| 302 | URL encontrada y habilitada | URL original |
| 302 | URL no encontrada o deshabilitada | URL base de fallback |

**Ejemplo de Uso**:

```bash
# URL existente
curl -I http://localhost:8080/abc123
# HTTP/1.1 302 Found
# Location: https://www.example.com/articulo-largo

# URL no existente
curl -I http://localhost:8080/invalid
# HTTP/1.1 302 Found
# Location: https://www.google.com
```

### Actuator Endpoints

```http
GET /actuator/health
```

**Descripción**: Health check de la aplicación

```http
GET /actuator/metrics
```

**Descripción**: Métricas de la aplicación

```http
GET /actuator/prometheus
```

**Descripción**: Métricas en formato Prometheus

## Manejo de Errores

### Estrategia de Manejo de Errores

1. **UrlNotFoundException**: Se captura globalmente con `@ExceptionHandler`
2. **Redirección Graceful**: En lugar de devolver un error 404, se redirige a una URL base
3. **Logging**: Se registra la clave no encontrada en nivel DEBUG

### Casos de Error

| Caso | Comportamiento |
|------|----------------|
| Clave no existe | Redirige a `redirect-base-url` |
| URL deshabilitada (`enabled=false`) | Redirige a `redirect-base-url` |
| MongoDB caído | Error 500 (no capturado, falla rápido) |
| Redis caído | Funciona sin caché (degradación elegante) |

## Caching

### Estrategia de Caché

- **Proveedor**: Redis
- **TTL**: 1 hora (configurable)
- **Key Pattern**: `url-shortener-cache-{urlKey}`
- **Invalidación**: No automática (TTL based)

### Comportamiento del Caché

1. **Cache Hit**: La URL se devuelve directamente desde Redis
2. **Cache Miss**: Se consulta MongoDB y el resultado se almacena en Redis
3. **Null Values**: No se cachean valores nulos (`cache-null-values: false`)
4. **Statistics**: Habilitadas para métricas de hit/miss ratio

### Beneficios

- Reducción de latencia (Redis es ~100x más rápido que MongoDB)
- Reducción de carga en MongoDB
- Escalabilidad horizontal (múltiples instancias comparten el mismo caché)

## Métricas y Monitoreo

### Métricas Personalizadas

**Métrica**: `url.shortener.api.controller.redirect`

- **Tipo**: Timer
- **Tags**: `key` (URL key)
- **Descripción**: Tiempo de respuesta de redirección
- **Ubicación**: `UrlShortenerController.java:21`

### Métricas Estándar

- **JVM**: Memoria, GC, hilos
- **HTTP**: Requests, latencia, errores
- **Cache**: Hit ratio, evictions
- **MongoDB**: Conexiones, operaciones
- **Redis**: Comandos, conexiones

### Visualización

Las métricas se exportan en formato Prometheus y pueden visualizarse en:
- Grafana
- Prometheus UI
- Spring Boot Admin

## Despliegue

### Requisitos

- Java 21+
- MongoDB 4.x+
- Redis 6.x+
- Maven 3.9+

### Build Local

```bash
# Compilar el proyecto
mvn clean package

# Ejecutar la aplicación
java -jar target/url-shortener-api-1.0.0-SNAPSHOT.jar
```

### Docker

**Dockerfile**: `Dockerfile`

```dockerfile
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar url-shortener-rest-api.jar
EXPOSE 8080
```

**Build de la imagen**:

```bash
docker build -t url-shortener-api:latest .
```

**Ejecutar con Docker**:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_HOST=mongo \
  -e SPRING_DATA_REDIS_HOST=redis \
  url-shortener-api:latest
```

### Docker Compose

Para levantar el stack completo (API, MongoDB, Redis):

```bash
# Desde el directorio raíz del proyecto
docker-compose up -d
```

### Variables de Entorno para Producción

```bash
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
SPRING_DATA_MONGODB_HOST=mongodb.production.svc
SPRING_DATA_MONGODB_PORT=27017
SPRING_DATA_MONGODB_DATABASE=url-shortener-prod
SPRING_DATA_MONGODB_USERNAME=admin
SPRING_DATA_MONGODB_PASSWORD=${MONGO_PASSWORD}
SPRING_DATA_REDIS_HOST=redis.production.svc
SPRING_DATA_REDIS_PORT=6379
SPRING_DATA_REDIS_PASSWORD=${REDIS_PASSWORD}
GITHUB_AAIVAN28_URL_SHORTENER_REDIRECT_BASE_URL=https://mycompany.com
```

## Testing

### Estructura de Tests

```
src/test/java/
├── application/service/
│   └── UrlServiceTest.java
├── infrastructure/adapter/
    ├── converter/
    │   └── UrlDocumentStringConverterTest.java
    ├── inbound/
    │   ├── handler/
    │   │   └── UrlExceptionHandlerTest.java
    │   ├── properties/
    │   │   └── UrlPropertiesTest.java
    │   └── rest/
    │       └── UrlShortenerControllerTest.java
    └── outbound/
        └── persistence/
            └── MongoRepositoryAdapterTest.java
```

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Tests de un módulo específico
mvn test -Dtest=UrlServiceTest

# Con cobertura
mvn test jacoco:report
```

## Dependencias del Proyecto

### Módulos Internos

- `cache-redis`: Configuración de Redis para caché

### Módulos Relacionados

- `app-url-shortener-admin-api`: API de administración (CRUD de URLs)

## Autor

Proyecto desarrollado por aaivan28

## Licencia

Este proyecto es parte del sistema URL Shortener Project.