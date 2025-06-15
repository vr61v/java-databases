FROM openjdk:17-jdk-slim
LABEL authors="vr61v"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]