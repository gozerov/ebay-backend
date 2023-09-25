FROM openjdk:17-jdk-alpine3.14
RUN mkdir /app
COPY ./build/libs/ru.gozerov.ebay-backend-all.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]