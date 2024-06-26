FROM openjdk:17 as build

EXPOSE 8333

ADD target/eureka-server.jar eureka-server.jar

ENTRYPOINT ["java","-jar","eureka-server.jar"]