FROM maven:3.6.3-jdk-11

COPY /target/eureka-discovery-server*.jar /eureka-discovery-server.jar
EXPOSE 8001
ENTRYPOINT ["java", "-Dspring.profiles.active=standalone", "-jar", "/eureka-discovery-server.jar"]