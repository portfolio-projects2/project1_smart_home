
FROM openjdk:latest
ARG JAR_FILE=Core/target/Core-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]