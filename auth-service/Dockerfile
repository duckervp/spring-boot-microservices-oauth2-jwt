FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/auth-service-1.0-SNAPSHOT.jar auth-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/auth-service-1.0-SNAPSHOT.jar"]