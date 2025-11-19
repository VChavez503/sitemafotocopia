FROM tomcat:9-jdk17

COPY target/sitemafotocopia-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/sitemafotocopia.war
