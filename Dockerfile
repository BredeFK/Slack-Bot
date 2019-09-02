# we are extending everything from tomcat:9.0.24 image ...
FROM tomcat:9.0.24
MAINTAINER bredefk
# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
COPY ./alfred.war ./web

#WORKDIR /alfred