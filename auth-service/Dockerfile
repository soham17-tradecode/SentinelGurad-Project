FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY keys/private_key.pem keys/private_key.pem

COPY keys/public_key.pem keys/public_key.pem



EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]