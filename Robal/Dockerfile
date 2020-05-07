FROM openjdk:8-jdk-alpine
MAINTAINER Robert and Alex
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /opt/robal
COPY target/AlRobify-0.0.1-SNAPSHOT.jar robal.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "robal.jar"]
EXPOSE 8080 8443
