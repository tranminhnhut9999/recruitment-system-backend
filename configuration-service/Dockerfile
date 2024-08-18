FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the service's jar file to the container
COPY target/configuration-service-0.0.1-SNAPSHOT.jar /app/configuration-service-0.0.1-SNAPSHOT.jar

# Set environment variables for MySQL
ENV MYSQL_HOST=configuration-db
ENV MYSQL_DATABASE=configuration-db
ENV MYSQL_USER=configuser
ENV MYSQL_PASSWORD=configpassword

# Expose the service's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/configuration-service-0.0.1-SNAPSHOT.jar"]
