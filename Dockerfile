#FROM openjdk:8-jdk-alpine
FROM openjdk:18-ea-11-jdk-alpine
EXPOSE 8053
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
