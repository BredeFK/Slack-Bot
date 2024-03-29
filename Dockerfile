# Updated 03/09/20190
# Source: https://spring.io/guides/gs/spring-boot-docker/

# From image
FROM openjdk:8-jdk-alpine

# Maintainer / developer
MAINTAINER Brede Frtijof Klausen "bfk@itverket.no"

# Expose port 8080
EXPOSE 8080

# Copy jar to root
COPY ./target/alfred-2.0.1.jar app.jar
COPY files files

#Environment variables
ENV SLACK-BOT-TOKEN=""
ENV CHANNEL-ID-GENERAL=""
ENV WEBSITE-NAME=""
ENV WEBSITE-URL=""
ENV CONTACT_EMAIL=""
ENV IPIFY-TOKEN=""
ENV SLACK-SIGNING-SECRET=""

# Run the .jar file fancy
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]