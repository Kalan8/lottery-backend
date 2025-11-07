# Backend demo

This project is a **Java Spring Boot** backend demonstrating the use of **Hibernate/JPA** for persistence, **Spring Data JPA repositories**, **H2 or MySQL Databases**, and RESTful CRUD endpoints for a `User` entity at the moment.
This project is solely a sandbox for testing backend/frontend techs, concepts and does not address a specific business need.
It was started very recently and is a work in progress. A [What's next](#whats-next) section below lists the next developments that would be necessary/good to enhance the application.

It also includes **unit tests and endpoint tests** using JUnit, Mockito and MockMvc includes in the SpringFramework.

You can interact with this backend app via a Typescript/React frontend which I am also developing in parallel. You can
also interact with it via Postman.
The frontend is available at : `https://github.com/Kalan8/frontend-demo`

---

## Table of Contents

* [Tech Stack](#tech-stack)
* [Features](#features)
* [What's next](#whats-next)
* [Getting Started](#getting-started)
* [Running Tests](#running-tests)
* [API Endpoints](#api-endpoints)
* [Error Response Format](#error-response-format)

---

## Tech Stack

* Java 25
* Spring Boot 3.x
* Spring Data JPA
* Hibernate ORM 6.x
* H2 database (in-memory) / MySQL (optional)
* JUnit 5
* Mockito
* Maven

---

## Features

* Create, read, update, and delete users via REST endpoints.
* In-memory H2 database for fast development and testing but works also with My SQL database. Dependency commented in
  pom.xml and configurations commented in application.properties
* JUnit and Mockito tests for Service and Controller layers.
* Exception handling and proper HTTP response codes.

---

## What's next

* Add a logger to track application behavior, and debug/diagnose issues
* Add data validations (Name/Surname not empty or malformed email). The frontend app manages these validations but it is
  also necessary to validate data in the backend side.
* Add a random user feature

Some possible further features :

* Connect with friends
* Create your own pets (dog, cat for example)
* Search people around or with a specific pet

---

## Getting Started

### Prerequisites

* Java 25 SDK installed
* Maven installed

### Installation

1. Clone the repository:

```bash
git clone https://github.com/Kalan8/backend-demo.git
```

2. Build the project:

```bash
cd backend-demo
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

4. The backend will be available by default at `http://localhost:8080`

But you can set another exposure port in the `application.properties` file via `server.port` and `server.address`
properties.
In this case, make sure to configure the new backend url in the frontend `.env` file to let both communicate.

---

## Running Tests

To run **all tests**:

```bash
mvn test
```

---

## API Endpoints

| HTTP Method | Endpoint          | Description                    | Request Body (JSON) Example                                                       | Response Status  | Possible Errors                                                                                              |
|-------------|-------------------|--------------------------------|-----------------------------------------------------------------------------------|------------------|--------------------------------------------------------------------------------------------------------------|
| **GET**     | `/api/users`      | Retrieve all users             | –                                                                                 | `200 OK`         | `500 Internal Server Error`                                                                                  |
| **GET**     | `/api/users/{id}` | Retrieve a specific user by ID | –                                                                                 | `200 OK`         | `404 Not Found` if user doesn’t exist                                                                        |
| **POST**    | `/api/users`      | Create a new user              | ```json { "name": "John", "surname": "Doe", "email": "john.doe@example.com" } ``` | `201 Created`    | `400 Bad Request` (validation error) / `409 Conlict` (DB integrity violation)                                |
| **PUT**     | `/api/users/{id}` | Update an existing user by ID  | ```json { "name": "Jane", "surname": "Doe", "email": "jane.doe@example.com" } ``` | `200 OK`         | `404 Not Found` (user not found) / `400 Bad Request` (invalid data) / `409 Conlict` (DB integrity violation) |
| **DELETE**  | `/api/users/{id}` | Delete a user by ID            | –                                                                                 | `204 No Content` | `404 Not Found` if user doesn’t exist                                                                        |

---

### Error Response Format

All error responses from the backend follow a **uniform JSON structure**:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2025-11-06T15:15:31.0727526",
  "details": {
    "name": "Name cannot be blank",
    "surname": "Surname cannot be blank",
    "email": "Email should be valid"
  }
}
```

---
