
FROM openjdk:17-oracle
WORKDIR /app
ADD /target/TravelPlanner-0.0.1-SNAPSHOT.jar /app/travel_planner.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/travel_planner.jar"]