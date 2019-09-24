# Updated 03/09/20190

# From image
FROM openjdk:8-jdk-alpine

# Maintainer / developer
MAINTAINER Brede Frtijof Klausen "bfk@itverket.no"

# Expose port 8080
EXPOSE 8080

# Copy
COPY ./target/alfred-2.0.1.jar app.jar

#Environment variables
ENV SLACK-BOT-TOKEN=""
ENV CHANNEL-ID-GENERAL=""

# Command (IMPORTANT with space between CMD and [)
CMD ["java -jar app.jar"]