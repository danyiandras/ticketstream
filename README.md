# TASK
## The main objective
The main objective of this project is to provide the implementation of a backend
service in Java which exposes a REST API or uses a message broker and is used
by some other services for booking online movie tickets. Booking a ticket means
just blocking the required number of particular seats.

## Requirements
  * The service should expose the following endpoints:
  * View all playing movies
  * Select a movie
  * See all theaters showing the movie
  * Select a theater
  * See available seats (e.g., a1, a2, a3) for the selected theater & movie.
  * You can assume that all theaters have 20 seats capacity.
  * Book one or more of the available seats. Service should be able to handle multiple requests simultaneously (no over-bookings)

## EXPECTATIONS:
  * Clean, well-documented source code.
  * At least 50% unit-test coverage.
  * Documented APIs (REST, broker messages etc) for the consumer.
  * You are allowed to use third-party libraries whenever deemed suitable.
  * Docker support

## Other resources used during modeling and to refine requirements
  * [Movie_theater wikipedia](https://en.wikipedia.org/wiki/Movie_theater)
  * [film-copyright-licensing](https://www.independentcinemaoffice.org.uk/advice-support/what-licences-do-i-need/film-copyright-licensing/)

# Notes

The implementation is not production-ready and still requires lots of work, but the happy path is working.

The implementation uses:
  * Spring Boot
  * Testcontainters
  * Apache Casandra
  * RabbitMq

The implementation of the API does not contain exception handling and signaling errors. This time the API documentation is only a small description in the README.md file. 

# Compile and Run

To compile the project use:

  * Java 17
  * Maven

To compile, test, and package the Spring Boot jar, run the following command in the root of the project:

    mvn clean install

To create the docker image:

    mvn spring-boot:build-image

To run the docker image (this command also starts a Cassandra and a RabbitMQ container)

    cd scripts/docker-compose/
    docker-compose up -d

# API Documentation and Usage

The API documentation is currently just a pointer to the "com.example.ticketstream.functions.dto" package. This package contains the DTO objects the API uses.

## API Usage for the use case

Movies have Screenings which are in a certain time interval. The use case to reserve Seats (Reservations represented by Tickets):

  1. View all playing movies in the given time interval
  * Select a movie
  * See all theaters showing the movie in the given time interval
  * Select a theater
  * See all screenings in the theater showing the movie in the given time interval
  * Select a screening
  * Calculate available seats for the selected screening
  * Book one or more of the available seats.

The appropriate API usage can be found in the "src/test/java/com.example.ticketstream.functions.ApiUsage.buyTickets" test method.


# Implementation
## Data Schema
The schema is a denormalized Cassandra schema in the "com.example.ticketstream.model" package.
  * The Theater class represents a movie theater with only one auditorium. Every Theater has a unique name.
  * The SeatUDT class represents a seat in a Theater. Every Seat has a unique name in a Theater.
  * The Movie class represents a movie with a unique title.
  * The Licence class represents the time interval a Theater is allowed to show a Movie.
  * The Screening class represents a show of a Movie in a Theater in a certain time interval. Two Screenings in a Theater can not overlap in time. (This constraint is not implemented.)
  



