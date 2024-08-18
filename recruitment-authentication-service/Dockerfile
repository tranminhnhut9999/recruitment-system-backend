# Use an official OpenJDK 11 runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the service's jar file to the container
COPY target/authentication-service-0.0.1-SNAPSHOT.jar /app/authentication-service-0.0.1-SNAPSHOT.jar

# Set environment variables for MySQL
ENV MYSQL_HOST=authentication-db
ENV MYSQL_DATABASE=recruitment-account-db
ENV MYSQL_USER=authuser
ENV MYSQL_PASSWORD=authpassword

# Expose the service's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/authentication-service-0.0.1-SNAPSHOT.jar"]
