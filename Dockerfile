FROM openjdk:17
ADD target/wanderland-server-0.0.1-SNAPSHOT.jar wanderland-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar","wanderland-server-0.0.1-SNAPSHOT.jar"]
