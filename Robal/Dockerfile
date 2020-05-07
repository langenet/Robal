FROM openjdk:8-jdk-alpine
MAINTAINER Robert and Alex
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /opt/robal
COPY target/AlRobify-0.0.1-SNAPSHOT.jar robal.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "robal.jar"]
EXPOSE 8080 8443






#VOLUME /tmp
#EXPOSE 8080
#RUN mkdir -p /app/
#RUN mkdir -p /app/logs/
#ADD target/AlRobify-0.0.1-SNAPSHOT.jar /app/app.jar
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]