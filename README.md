Travel Plan Backend App

This is a Spring Boot application for our group project for module 50.001

Group 23

Oleksii Onishchenko 1006207

----------------------------------------------------------------------------------

Code located in: src/main/java/com/example/TravelPlanner

There are 3 packages:

auth - implementation of the authentication
common - implementation of some common things such as security and configurations
travelplanning - business logic for our api. here you can find models used for database, controllers layer, services, repositories (for communicating and retrieving data from the database)

Patterns used:
Chain of responsibility : used in common/filters
                          Custom filters which call next filter in filterChain through doFilter()
Singleton : all Spring beans https://www.baeldung.com/spring-bean
Builder : using Lombok builder annotation for DTO (data transfer objects)

OOP principles:
Polymorphism : using interfaces
Inheritance

Solid principles:
Single Responsibility Principle
