FROM openjdk:11

EXPOSE 8080

WORKDIR /app

COPY target/messaging-service-executable.jar .

ENTRYPOINT [ "java", "-jar", "messaging-service-executable.jar" ]