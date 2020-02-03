FROM openjdk:latest
COPY ./target/OrderService-1.0-SNAPSHOT.jar /order.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "order.jar"]