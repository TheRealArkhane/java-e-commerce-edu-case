FROM openjdk:17-jdk-alpine
ADD /target/ecommerce-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]
