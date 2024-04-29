FROM eclipse-temurin:21
ARG WAR_FILE=./leichtathletik-vereinsdatenbank-controller/target/leichtathletik-vereinsdatenbank-controller-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} application.war
ENTRYPOINT ["java", "-jar", "application.war"]