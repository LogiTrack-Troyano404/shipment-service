# shipment-service

Microservicio de gestión de envíos y repartidores para **LogiTrack-Troyano404**, plataforma de logística para comercios electrónicos y entregas de última milla.

Core transaccional del sistema: ciclo de vida completo de los paquetes (registro, consulta, cancelación, cambios de estado) y asignación de repartidores, con historial de auditoría de cada movimiento.

**Sprint 1** — Desarrollo y Arquitecturas Avanzadas de Software · Ingeniería en Informática · 2026

## Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 25 | Lenguaje |
| Spring Boot | 4.1.1 | Framework base |
| Spring Data JPA / Hibernate | 7.x | Persistencia y auditoría |
| MySQL | 8.4 | Base de datos |
| springdoc-openapi | 3.1.1 | Swagger UI / OpenAPI 3 |
| Lombok | — | Reducción de boilerplate |
| Docker + Docker Compose | — | Empaquetado y orquestación |
| JUnit 5 + Mockito | — | Tests unitarios de servicios |
| MockMvc (`@WebMvcTest`) | — | Tests de integración web |
| Testcontainers | 2.0.5 | Tests de contexto con MySQL dockerizado |

## Estructura del proyecto

```
src/main/java/com/example/shipmentservice/
├── controller/   → Endpoints REST
├── dto/          → Contratos de la API (records, sin exponer entidades)
├── mapper/       → Conversión DTO ↔ entidad
├── model/        → Entidades JPA (envios, clientes, repartidores, historial)
├── repository/   → Repositorios Spring Data JPA
├── service/      → Interfaces de negocio
│   └── impl/     → Implementaciones transaccionales
├── Exception/    → Excepciones de dominio + GlobalExceptionHandler
└── config/       → OpenAPI y auditoría JPA
```

## Reglas de negocio

- Todo envío nuevo nace en estado `PENDIENTE`.
- Solo se puede **cancelar** un envío en estado `PENDIENTE` (`422` en otro caso).
- Solo se puede **asignar** un repartidor `ACTIVO` a un envío `PENDIENTE` (`422` si el repartidor está inactivo o el envío ya salió).
- Cada cambio de estado queda registrado en `historial_de_estados` (auditoría).
- Los errores responden con el formato `{timestamp, status, error, message, path}` y códigos semánticos: `400` validación, `404` no encontrado, `422` regla de negocio.

## API REST

Base: `/api/v1` — documentación interactiva en **Swagger UI**.

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/envios` | Registrar envío (201) |
| `GET` | `/envios/{id}` | Consultar por ID |
| `GET` | `/envios/tracking/{codigo}` | Consultar por código de seguimiento |
| `GET` | `/envios` | Listar todos o filtrar `?estado=PENDIENTE` |
| `POST` | `/envios/{id}/cancelar` | Cancelar envío |
| `POST` | `/envios/{id}/asignar-repartidor` | Asignar repartidor activo |
| `POST` | `/repartidores` | Registrar repartidor (201) |
| `GET` | `/repartidores/{id}` | Consultar repartidor |
| `GET` | `/repartidores` | Listar todos o filtrar `?estado=ACTIVO` |
| `PATCH` | `/repartidores/{id}/estado` | Cambiar estado del repartidor |

## Cómo correrlo

### Opción A — Docker Compose (todo con un comando)

Requiere solo Docker. Compila el código, levanta MySQL y la app, y carga datos de ejemplo:

```bash
cp .env.example .env   # completar MYSQL_PASSWORD
docker compose up --build
```

Swagger UI: **http://localhost:8080/swagger-ui.html**

```bash
docker compose down    # bajar el stack
```

### Opción B — Desarrollo local

Requiere JDK 25 y un MySQL 8.x corriendo en `localhost:3306`.

```bash
cp .env.example .env   # completar SPRING_DATASOURCE_PASSWORD
# crear la base: CREATE DATABASE shipment_db;
# crear el usuario: CREATE USER 'admin_shipment'@'%' IDENTIFIED BY '<password>';
#                   GRANT ALL ON shipment_db.* TO 'admin_shipment'@'%';

./mvnw spring-boot:run
```

La app lee las credenciales desde `.env`. Hibernate crea/actualiza las tablas (`ddl-auto: update`); `schema.sql` y `data.sql` están en `src/main/resources/` como scripts de referencia/seed — en modo local la inicialización está desactivada (`sql.init.mode: never`) para no borrar datos en cada reinicio.

## Tests

```bash
./mvnw test
```

La suite completa no necesita una base externa: los tests de servicios son unitarios con Mockito, los de controllers usan `@WebMvcTest` + MockMvc, y el test de contexto levanta su propio MySQL con **Testcontainers** (requiere el daemon de Docker activo).

## Equipo — LogiTrack-Troyano404

- Gimenez Nicole
- Gomez Andrés
- Liquin José
