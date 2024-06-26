FROM openjdk:17 as build

EXPOSE 8444

ADD target/api-gateway.jar api-gateway.jar

ENTRYPOINT ["java","-jar","api-gateway.jar"]