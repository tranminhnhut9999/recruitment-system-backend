# Use an official OpenJDK 11 runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the service's jar file to the container
COPY target/email-service-0.0.1-SNAPSHOT.jar /app/email-service-0.0.1-SNAPSHOT.jar

# Set environment variables for MySQL
ENV MYSQL_HOST=email-db
ENV MYSQL_DATABASE=email-db
ENV MYSQL_USER=emailuser
ENV MYSQL_PASSWORD=emailpassword

# Expose the service's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/email-service-0.0.1-SNAPSHOT.jar"]
