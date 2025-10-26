# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a URL shortener system built with Java 21 and Spring Boot 3.5.3, following **Hexagonal Architecture** (Ports and Adapters) principles. The project is a Maven multi-module application consisting of three modules that work together to provide URL shortening and administration capabilities.

## Project Structure

```
url-shortener-project/
├── app-url-shortener-api/          # Public API for URL redirection
├── app-url-shortener-admin-api/    # Admin API for URL management (CRUD)
└── lib-cache-redis/                # Shared Redis caching library
```

## Architecture Principles

This codebase follows strict **Hexagonal Architecture** patterns:

### Layer Structure (Critical)

Each application module follows this structure:

```
domain/                  # Pure business logic, NO framework dependencies
├── port/
│   ├── inbound/        # Use case interfaces (e.g., UrlUsesCases)
│   └── outbound/       # Repository interfaces (e.g., UrlRepository)
application/            # Use case implementations, NO Spring annotations
└── service/            # Services implement inbound ports
infrastructure/         # All framework-specific code
├── adapter/
│   ├── inbound/       # REST controllers, handlers, properties
│   └── outbound/      # Persistence adapters, external integrations
└── configuration/     # Spring beans, dependency injection setup
```

### Critical Architecture Rules

1. **Domain Layer**: Must be completely framework-agnostic
   - Only Java standard library and domain concepts
   - Define contracts through interfaces (ports)
   - No Spring, no MongoDB, no Redis annotations

2. **Application Layer**: Pure business logic
   - Implements domain ports (use cases)
   - Can use Lombok annotations ONLY (`@RequiredArgsConstructor`, `@Slf4j`)
   - NEVER use `@Service`, `@Component`, or other Spring annotations
   - Keep framework-independent for testability

3. **Infrastructure Layer**: All framework code lives here
   - Spring configuration classes in `infrastructure/configuration/`
   - REST controllers in `infrastructure/adapter/inbound/rest/`
   - Persistence adapters in `infrastructure/adapter/outbound/persistence/`
   - Bean definitions use explicit `@Bean` methods in `@Configuration` classes

4. **Dependency Direction**: Always flows inward
   ```
   infrastructure → application → domain
   (adapters implement ports defined by inner layers)
   ```

### Configuration Pattern

Services in the application layer are wired as beans in infrastructure configuration:

```java
@Configuration
public class ApplicationConfiguration {
    @Bean
    UrlUsesCases urlUsesCases(final UrlRepository repository) {
        return new UrlService(repository);  // POJO instantiation
    }
}
```

This keeps the application layer free of Spring annotations while maintaining dependency injection.

## Build and Run Commands

### Building the Project

```bash
# Build entire multi-module project
mvn clean install

# Build without running tests
mvn clean install -DskipTests

# Build specific module
mvn clean install -pl app-url-shortener-api

# Build with dependencies
mvn clean install -pl app-url-shortener-api -am
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl app-url-shortener-api

# Run specific test class
mvn test -Dtest=UrlServiceTest

# Run specific test method
mvn test -Dtest=UrlServiceTest#shouldReturnUrlWhenKeyExists

# Run tests with coverage
mvn test jacoco:report
```

### Running the Applications

```bash
# Start infrastructure (MongoDB + Redis)
docker-compose up -d mongo redis

# Run URL shortener API (port 8080)
cd app-url-shortener-api
mvn spring-boot:run

# Run Admin API (port 8081)
cd app-url-shortener-admin-api
mvn spring-boot:run

# Or run with Docker Compose (all services)
docker-compose up -d
```

### Service Endpoints

- **URL Shortener API**: `http://localhost:8080`
  - Redirects: `GET /{urlKey}`
  - Actuator: `GET /actuator/health`
  - Metrics: `GET /actuator/prometheus`

- **Admin API**: `http://localhost:8081`
  - Search URLs: `GET /api/v1/url?key={key}&description={desc}&page={n}&limit={m}`
  - Get URL: `GET /api/v1/url/{id}`
  - Delete URL: `DELETE /api/v1/url/{id}`
  - Health: `GET /actuator/health`

## Module Details

### app-url-shortener-api

Public-facing service that handles URL redirection. When a user accesses `/{urlKey}`, it:
1. Checks Redis cache for the URL key
2. If cache miss, queries MongoDB
3. Filters disabled URLs (`enabled=false`)
4. Returns HTTP 302 redirect to original URL
5. If not found, redirects to configured fallback URL

**Key Components**:
- `UrlShortenerController`: HTTP endpoint at `GET /{urlKey}`
- `UrlService`: Implements `UrlUsesCases` use case
- `MongoRepositoryAdapter`: Implements `UrlRepository` with caching
- `UrlExceptionHandler`: Global exception handling with graceful fallback

**Configuration**: `application.yaml`
- Redis cache TTL: 1 hour
- Fallback URL: `github.aaivan28.url-shortener.redirect-base-url`

### app-url-shortener-admin-api

Administrative service for managing shortened URLs (CRUD operations).

**Key Components**:
- `UrlShortenerAdminController`: REST endpoints for URL management
- `UrlService`: Implements search, detail, and delete use cases
- Pagination support with configurable page size

### lib-cache-redis

Shared library providing Redis caching configuration. Auto-configured via Spring Boot's `AutoConfiguration.imports` mechanism.

**Features**:
- Centralized Redis configuration
- Cache statistics enabled
- Key prefix management
- Reusable across both API modules

## Technology Stack

- **Java 21**: Language (uses records, pattern matching)
- **Spring Boot 3.5.3**: Application framework
- **MongoDB 4.4**: Primary data store
- **Redis 8.0**: Distributed cache
- **Lombok 1.18.38**: Code generation
- **Micrometer 1.15.1**: Metrics (Prometheus export)
- **Maven**: Build tool
- **Docker**: Containerization

## Database Schema

### MongoDB Collection: `shortened_url`

```javascript
{
  _id: ObjectId,
  key: String,        // Unique index - the short URL key
  url: String,        // Original URL to redirect to
  description: String,
  createdAt: ISODate,
  updatedAt: ISODate,
  enabled: Boolean    // Flag to enable/disable redirects
}
```

## Adding New Features

### Adding a New Use Case

1. **Define the port** in `domain/port/inbound/usescases/`:
   ```java
   public interface CreateUrlUseCase {
       String createUrl(String originalUrl);
   }
   ```

2. **Implement in application layer** without Spring annotations:
   ```java
   @RequiredArgsConstructor
   public class UrlService implements CreateUrlUseCase {
       private final UrlRepository repository;

       @Override
       public String createUrl(String originalUrl) {
           // Pure business logic
       }
   }
   ```

3. **Wire in infrastructure configuration**:
   ```java
   @Configuration
   public class ApplicationConfiguration {
       @Bean
       CreateUrlUseCase createUrlUseCase(UrlRepository repository) {
           return new UrlService(repository);
       }
   }
   ```

4. **Create REST adapter**:
   ```java
   @RestController
   @RequestMapping("/api/v1/url")
   public class UrlController {
       private final CreateUrlUseCase createUrlUseCase;

       @PostMapping
       public ResponseEntity<String> create(@RequestBody CreateUrlRequest request) {
           return ResponseEntity.ok(createUrlUseCase.createUrl(request.url()));
       }
   }
   ```

### Adding a New Repository Method

1. **Add to outbound port** in `domain/port/outbound/`:
   ```java
   public interface UrlRepository {
       Optional<String> getUrl(String urlKey);
       void save(String urlKey, String url);  // New method
   }
   ```

2. **Implement in adapter**:
   ```java
   @Component
   public class MongoRepositoryAdapter implements UrlRepository {
       @Override
       public void save(String urlKey, String url) {
           // MongoDB implementation
       }
   }
   ```

## Testing Guidelines

### Test Structure

Tests are organized by layer and follow the same package structure:
```
src/test/java/
├── application/service/           # Unit tests for services
├── infrastructure/adapter/
│   ├── inbound/                  # Controller and handler tests
│   └── outbound/                 # Repository adapter tests
```

### Testing Patterns

- **Use JUnit 5**: `@Test`, `@DisplayName`, `@BeforeEach`
- **Mock with Mockito**: `@Mock`, `@InjectMocks`, or manual mocking
- **Assertions with AssertJ**: `assertThat(...).isEqualTo(...)`
- **Given-When-Then**: Structure tests clearly
- **Test edge cases**: null, empty, special characters, Unicode

### Example Test Pattern

```java
@DisplayName("UrlService Tests")
class UrlServiceTest {

    @Test
    @DisplayName("Should return URL when key exists")
    void shouldReturnUrlWhenKeyExists() {
        // Given
        UrlRepository repository = mock(UrlRepository.class);
        when(repository.getUrl("key")).thenReturn(Optional.of("http://example.com"));
        UrlService service = new UrlService(repository);

        // When
        String result = service.getUrl("key");

        // Then
        assertThat(result).isEqualTo("http://example.com");
    }
}
```

## Common Pitfalls to Avoid

1. **DO NOT add Spring annotations to application layer**
   - ❌ `@Service`, `@Component`, `@Cacheable`
   - ✅ Configure beans in `infrastructure/configuration/`

2. **DO NOT mix layers**
   - ❌ Controller calling Repository directly
   - ✅ Controller → UseCase → Repository

3. **DO NOT leak infrastructure types to domain**
   - ❌ MongoDB annotations in domain models
   - ✅ Pure domain objects, map in adapters

4. **DO NOT skip the cache**
   - Caching is implemented at repository level with `@Cacheable`
   - Cache invalidation is TTL-based (1 hour default)

5. **DO NOT forget to update both APIs**
   - Admin API creates/modifies data
   - URL Shortener API reads data
   - Ensure consistency between modules

## Configuration Management

### Environment Variables Override

Spring Boot property precedence allows environment variable overrides:

```bash
# Override MongoDB host
SPRING_DATA_MONGODB_HOST=prod-mongo-server

# Override Redis host
SPRING_DATA_REDIS_HOST=prod-redis-server

# Override fallback URL
GITHUB_AAIVAN28_URL_SHORTENER_REDIRECT_BASE_URL=https://company.com
```

### Configuration Files

- `application.yaml`: Base configuration
- Profile-specific: `application-{profile}.yaml` (if needed)
- Configure via `SPRING_PROFILES_ACTIVE` environment variable

## Lombok Configuration

The project uses Lombok with annotation processing configured in Maven:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

Safe Lombok annotations to use:
- `@RequiredArgsConstructor`, `@AllArgsConstructor`
- `@Value`, `@Data`
- `@Builder`
- `@Slf4j`

## Metrics and Observability

### Available Metrics

- Custom timers: `@Timed(value = "metric.name")`
- JVM metrics: Memory, GC, threads
- HTTP metrics: Request count, latency, status codes
- Cache metrics: Hit/miss ratio, evictions
- MongoDB metrics: Connection pool, query time

### Accessing Metrics

```bash
# Prometheus format (for scraping)
curl http://localhost:8080/actuator/prometheus

# JSON format
curl http://localhost:8080/actuator/metrics

# Specific metric
curl http://localhost:8080/actuator/metrics/url.shortener.api.controller.redirect
```

## Project Quality Standards

Based on the REPORT.md analysis, this project achieves:
- **98% Hexagonal Architecture compliance**
- **100% test coverage** on core business logic (40 unit tests)
- **Clean dependency direction** (no circular dependencies)
- **Framework independence** in domain and application layers

When making changes, maintain these standards:
- Keep domain and application layers framework-agnostic
- Write unit tests for all new business logic
- Follow the existing package structure
- Use explicit bean configuration in infrastructure layer
- Document architectural decisions if deviating from patterns