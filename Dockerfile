## Change this to match the version of JDK you are using for your server application
#FROM amazoncorretto:17.0.9-alpine3.18
##COPY target/server-0.0.1-SNAPSHOT.jar /app.jar
#COPY /target/FredCarella_csd226_lab3-0.0.1-SNAPSHOT.jar /app.jar
## Inform Docker that the container is listening on the specified port at runtime.
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]

# Change this to match the version of JDK you are using for your server application
FROM openjdk:19-jdk-alpine
COPY target/FredCarella_csd226_lab3-0.0.1-SNAPSHOT.jar /app.jar
# Inform Docker that the container is listening on the specified port at runtime.
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
