FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8090:8090
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ebay-backend.jar
ENTRYPOINT ["java","-jar","/app/ebay-backend.jar"]