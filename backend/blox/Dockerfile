# Imagem do backend do Portal Blox (Spring Boot 4 / Java 21)

# --- Etapa de build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Camada de dependências cacheada: só re-executa quando o pom.xml muda
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
# Sem -DskipTests: os testes existentes não exigem banco (sem @SpringBootTest)
RUN mvn -B package

# --- Etapa de runtime ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
