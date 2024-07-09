FROM openjdk:21-jdk

ARG LOCAL_APP_FILE=wonderworld-0.0.1-SNAPSHOT.jar

RUN mkdir /home/app

COPY target/${LOCAL_APP_FILE} /home/app/app.jar

WORKDIR /home/app

EXPOSE 8080

ENTRYPOINT exec java -jar /home/app/app.jar --spring.config.location=$SPRING_CONFIG
