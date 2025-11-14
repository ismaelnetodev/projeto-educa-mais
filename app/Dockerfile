FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY app /build
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/target/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]