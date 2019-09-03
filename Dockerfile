# Updated 03/09/20190
# Inspired from the yt video: https://www.youtube.com/watch?v=B84_umDg2kY
# Tutorial for tomcat and docker: https://hub.docker.com/_/tomcat

# From tomcat image
FROM tomcat:9.0.24-jdk12-openjdk-oracle

# Maintainer / developer
MAINTAINER Brede Frtijof Klausen "bfk@itverket.no"

# Expose port 8080
EXPOSE 8080

# Remove useless files
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy
COPY ./target/alfred-1.9.jar /usr/local/tomcat/webapps/ROOT.jar

#VM argument
ENV SLACK-BOT-TOKEN=""
ENV CHANNEL-ID-GENERAL=""

# Command (IMPORTANT with space between CMD and [)
CMD ["catalina.sh","run"]