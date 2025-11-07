# Backend Lottery

This project is a java backend side of a web Lottery application. \
The associated frontend is developed in Typescript/React and it is available at :
[`https://github.com/Kalan8/frontend-demo`](https://github.com/Kalan8/frontend-demo)

The purpose of this couple of projects is to develop a lottery application. \
A brief specification of the app is:

* provide a page for creating/importing users and display the list of registered users,
* provide another page for launching the lottery and displaying the winner(s).

The [Further Potential Features](#further-potential-features) section below presents what would
be
good to do to improve
the application.

This project was started very recently and is a work in progress but both sides can be running and interact together. \
And you can have a look in the [Changelog for Frontend/Backend projects](#changelog-for-frontendbackend-projects)
section to see more on tasks or improvements already realised in both parts of the app.

This project is mainly a sandbox for testing backend/frontend techs and concepts.

---

## Table of Contents

| Sections                                                                                                                                                                                                                                                      | Concerns                       |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| <ul><li>[Actual Features](#actual-features)</li><li>[Further Potential Features](#further-potential-features)</li><li>[Changelog for Frontend/Backend projects](#changelog-for-frontendbackend-projects)</li></ul>                                            | ****Backend** / **Frontend**** | 
| <ul><li>[Tech Stack](#tech-stack)</li><li>[Features](#features)</li><li>[Getting Started](#getting-started)</li><li>[Running Tests](#running-tests)</li><li>[API Endpoints](#api-endpoints)</li><li>[Error Response Format](#error-response-format)</li></ul> | ****Backend****                |

---

### ⚠️ Notice that these 3 next sections below concern Backend and Frontend parts

## Actual Features

So far so good ! \
For now, there is a page which fetches the data from the backend and then display a user form for registering
users. The page displays dynamically the users already registered. Two buttons are present in each row of user to let
deleting the user or updating the info of the user (frontend modal not yet implemented but backend is ready).

---

## Further Potential Features

The actual specification is simple but it could be nice then to improve the app to provide more features and be more
user-friendly.
It could be good to have a simple page for people who want to register for the lottery.
An authentication system to allow an admin or administration staff to reach administration page.
On this page, the user can administrate the list of users so create/update/delete or import a file of users.
And on this page, the user can set the lottery: the number of winners and ordered/unordered winners.
The application could send mail to the registered people to indicate if they have won or lost.

---

## Changelog for Frontend/Backend projects

* **[Back]** Create an empty project and decide/install all the tools and frameworks
  needed
* **[Back]** Configure tools and frameworks basically to make the app running
* **[Back]** Structure my project and create my user entity, repository, service and
  REST controller
* **[Back]** Install My SQL and connect it to the app
* **[Front]** Decide the stack (React + TypeScript + Vite + TailwindCss) and install
  it
* **[Front]** Structure my project and create API service, a component and a page
* **[Front]** Improve API service, create UserCard and UserForm components for
  registration
* **[Front]** Add data validations (Name/Surname not empty or malformed email)
* **[Back]** Improve the service and the controller with implementing all the CRUD
  operations
* **[Front]** Implement the CRUD operations in the front codebase
* **[Front]** Refactor UserCard component and add to it Update button and Delete button
* **[Front]** Upgrade Tailwind from v3 to v4 and fix issues
* **[Front]** Adding styling libraries and dependencies
* **[Front]** Redesign the page and clean the project
* **[Back]** Add tests for UserService and UserController
* **[Back]** Fix CVE-2025-11226 Arbitrary Code Execution (ACE), forced to get a
  higher version than v1.5.18 for logback-core
  and logback-classic
* **[Back]** Add configuration and data file to populate automatically the database
  is needed
* **[Front]** Create a .env file to externalise the api backend url and make the
  frontend exposure port configurable
* **[Back]** Create CorsConfig class to configure and manage cross-origin requests
* **[Back]** Externalise the frontend URL in application.properties
* **[Back]** Make the backend URL configurable
* **[Back]** Add data validations (Name/Surname not null or empty, malformed email
  and unicity email)
* **[Back]** Create a GlobalExceptionHandler to manage all the exceptions occurred in
  the app and send back a clean http
  response to the frontend part

### Tasks coming soon:

* **[Back]** Create json file with data to import in Postman and add it to the repo
* **[Back]** Add a logger to track application behavior, and debug/diagnose issues
* **[Back]** / **[Front]** Add a random user feature

---

### ⚠️ Notice that these next sections below only concern Backend part

###                                 

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
* JUnit and Mockito tests for Service and Controller layers. HTTP responses are also tested.
* Exception handling and proper HTTP response codes.

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

The tests strategy is based on unit tests of every layer, so Service and Controller layers.
Both test-to-pass and test-to-fail are implemented to ensure the correct behavior of the application.\
HTTP responses are also tested.
To know more about, look at the class located in: `src\test\*`

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

### Interact with Postman

** TODO: ** Create json file with data to import, add it in the repo and detail it here. 

