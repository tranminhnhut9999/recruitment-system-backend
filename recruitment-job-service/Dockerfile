# Use an official OpenJDK 11 runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the service's jar file to the container
COPY target/jobs-service-0.0.1-SNAPSHOT.jar /app/jobs-service-0.0.1-SNAPSHOT.jar

# Set environment variables for MySQL
ENV MYSQL_HOST=job-db
ENV MYSQL_DATABASE=recruitment-job-db
ENV MYSQL_USER=jobuser
ENV MYSQL_PASSWORD=jobpassword

# Expose the service's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/jobs-service-0.0.1-SNAPSHOT.jar"]
