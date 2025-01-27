FROM openjdk:latest
ARG JAR_FILE=target/MqttSubscriber-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]