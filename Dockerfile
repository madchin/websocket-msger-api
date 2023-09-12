# Stage 1 Build app
FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

#Stage 2 Create final image
FROM openjdk:11
EXPOSE $PORT:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/websocket-msger-api.jar
ENTRYPOINT ["java","-jar","/app/websocket-msger-api.jar"]