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
# -cp app.jar:libs/*: This part specifies the classpath for the Java application.
# It includes the main application JAR file app.jar and any JAR files in the libs/ directory.
# The -cp flag is short for the classpath, and it tells Java where to find the necessary classes and resources for your application.
ENTRYPOINT ["java", "-cp", "app.jar:libs/*", "com.example.demo.DemoApplication"]

