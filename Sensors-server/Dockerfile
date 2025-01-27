FROM openjdk:latest
ARG JAR_FILE=target/Sensors-server-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

