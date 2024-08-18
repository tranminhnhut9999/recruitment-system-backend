# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the service's jar file to the container
COPY target/recruitment-gateway-0.0.1-SNAPSHOT.jar /app/recruitment-gateway-0.0.1-SNAPSHOT.jar

# Expose the service's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/recruitment-gateway-0.0.1-SNAPSHOT.jar"]
