FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:11.0.2

WORKDIR /usr/local/tomcat

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/web-forum-app-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
