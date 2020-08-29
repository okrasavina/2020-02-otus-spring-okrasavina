FROM maven:3.6.3-jdk-11

COPY /target/reader-service*.jar /reader-service.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/reader-service.jar"]
