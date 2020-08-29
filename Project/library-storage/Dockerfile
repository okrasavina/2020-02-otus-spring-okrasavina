FROM maven:3.6.3-jdk-11

COPY /target/library-storage*.jar /library-storage.jar
EXPOSE 7001
ENTRYPOINT ["java", "-jar", "/library-storage.jar"]
