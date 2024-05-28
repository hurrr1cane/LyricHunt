# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY target/lyrichunt-1.0.0.jar /app/lyrichunt-1.0.0.jar

# Expose the port that your application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "lyrichunt-1.0.0.jar"]
