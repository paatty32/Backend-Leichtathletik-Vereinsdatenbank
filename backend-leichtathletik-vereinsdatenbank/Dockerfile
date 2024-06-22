FROM eclipse-temurin:21
ARG WAR_FILE=./leichtathletik-vereinsdatenbank-controller/target/leichtathletik-vereinsdatenbank-controller-0.0.1-SNAPSHOT.war
COPY ${WAR_FILE} application.war
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "application.war", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]



