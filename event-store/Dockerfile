FROM openjdk:17 as build

EXPOSE 8111

ADD target/event-store.jar event-store.jar

ENTRYPOINT ["java","-jar","event-store.jar"]