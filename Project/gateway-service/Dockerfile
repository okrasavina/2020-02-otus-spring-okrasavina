FROM maven:3.6.3-jdk-11

COPY /target/gateway-service*.jar /gateway-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/gateway-service.jar"]