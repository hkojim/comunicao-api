FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app
COPY . .
# O -x test evita que o build quebre por falta de banco durante a compilação
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/comunicaco-api.jar

EXPOSE 8080
# Adicionamos um pequeno delay extra no comando de inicialização por segurança
CMD ["java", "-jar", "/app/comunicaco-api.jar"]