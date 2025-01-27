FROM openjdk:latest
ARG JAR_FILE=target/RestServer-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app.jar"]