FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/user-service-1.0-SNAPSHOT.jar user-service-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/user-service-1.0-SNAPSHOT.jar"]