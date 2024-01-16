FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE

# Copy dependencies
COPY "${JAR_FILE}/dependencies/*" /app/libs/

# Copy application jar
COPY "${JAR_FILE}/*.jar" /app/app.jar

# Set the working directory to /app
WORKDIR /app

# Run the application with the dependencies in the classpath
ENTRYPOINT ["java", "-cp", "app.jar:libs/*", "com.example.demo.DemoApplication"]


