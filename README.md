# Backend demo

This project is a **Java Spring Boot** backend demonstrating the use of **Hibernate/JPA** for persistence, **Spring Data JPA repositories**, **H2 or MySQL Databases**, and RESTful CRUD endpoints for a `User` entity at the moment.
This project is solely a sandbox for testing backend/frontend techs, concepts and does not address a specific business need.
It was started very recently and is a work in progress. A Next Steps section below lists the next developments that would be necessary/good to enhance the application.

It also includes **unit tests and endpoint tests** using JUnit and Mockito.

You can interact with this backend app via a Typescript/React frontend which I am also developing in parallel. You can also interact with it via Postman.
The frontend is available at : `https://github.com/Kalan8/frontend-demo`

---

## Table of Contents

* [Tech Stack](#tech-stack)
* [Features](#features)
* [What's next](#whats-next)
* [Getting Started](#getting-started)
* [Running Tests](#running-tests)
* [API Endpoints](#api-endpoints)



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
* In-memory H2 database for fast development and testing but works also with My SQL database. Dependency commented in pom.xml.
* JUnit and Mockito tests for Service and Controller layers.
* Exception handling and proper HTTP response codes.

---

## What's next

* Add a logger to track application behavior, and debug/diagnose issues
* Add data validations (Name/Surname not empty or malformed email). The frontend app manages these validations but it is also necessary to validate data in the backend side.
* Add a random user feature

Some possible further features :

* Create your own pets (dog, cat for example)
* Connect with friends
* Search people around or with the specific pet

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

4. The backend will be available at `http://localhost:8080`

---

## Running Tests

To run **all tests**:

```bash
mvn test
```

---

## API Endpoints

| Method | Endpoint           | Description                     | Request Body     | Response Code |
|--------|------------------|---------------------------------|-----------------|---------------|
| GET    | `/api/users`       | Get all users                   | N/A             | 200           |
| GET    | `/api/users/{id}`  | Get a user by ID                | N/A             | 200 / 404     |
| POST   | `/api/users`       | Create a new user               | JSON `User`     | 201           |
| PUT    | `/api/users/{id}`  | Update a user                   | JSON `User`     | 200 / 404     |
| DELETE | `/api/users/{id}`  | Delete a user                   | N/A             | 204 / 404     |

Example JSON `User`:

```json
{
  "name": "John",
  "surname": "Doe",
  "email": "john@example.com"
}
```

---

