FROM openjdk:17 as build

EXPOSE 8222

ADD target/attendance-microservice.jar attendance-microservice.jar

ENTRYPOINT ["java","-jar","attendance-microservice.jar"]