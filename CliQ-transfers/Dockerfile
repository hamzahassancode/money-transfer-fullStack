FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /usr/app

COPY --from=build /app/target/CliQ-transfers-0.0.1-SNAPSHOT.jar /usr/app/CliQ-transfers.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "CliQ-transfers.jar"]

