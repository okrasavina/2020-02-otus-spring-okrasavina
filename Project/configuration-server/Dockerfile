FROM maven:3.6.3-jdk-11

COPY /target/configuration-server*.jar /configuration-server.jar
EXPOSE 5000
ENTRYPOINT ["java", "-jar", "/configuration-server.jar"]