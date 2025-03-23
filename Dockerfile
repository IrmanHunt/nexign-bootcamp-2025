FROM openjdk:21-jdk
WORKDIR /app
COPY target/nexign-bootcamp-2025-0.0.1-SNAPSHOT.jar /app/nexign-bootcamp-2025.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "nexign-bootcamp-2025.jar"]