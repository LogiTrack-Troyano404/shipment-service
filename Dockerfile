# ----- ETAPA 1: Compilación (Build) -----
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Copiar archivos de configuración de Maven / Gradle
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargar dependencias en caché
RUN ./mvnw dependency:go-offline

# Copiar código fuente y compilar omitiendo los tests para acelerar el build
COPY src ./src
RUN ./mvnw clean package -DskipTests

# ----- ETAPA 2: Ejecución (Runtime) -----
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de compilación
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]