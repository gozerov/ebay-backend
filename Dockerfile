FROM openjdk:17-jdk-alpine3.14
RUN mkdir /app
COPY ./build/libs/ru.gozerov.ebay-backend-all.jar /app/ru.gozerov.ebay-backend-0.0.1.jar
ENTRYPOINT ["java","-jar","/app/ru.gozerov.ebay-backend-0.0.1.jar"]