FOR ASSIGNMENT AND CLASSWORK GITHUB REPO https://github.com/Aaditya239/23BCE9100-Aaditya
# College Management System

Backend-only College Management System built with Java 17, Spring Boot, Spring Data MongoDB, Swagger/OpenAPI, Lombok, Bean Validation, and Maven.

## Features

- Student management
- Staff management
- Department management
- Course management
- Book management
- Book issue and return management
- Global exception handling
- DTO-based API layer
- Swagger UI documentation
- MongoDB collection mapping
- Postman collection for API testing

## Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data MongoDB
- MongoDB
- Maven
- Swagger/OpenAPI via springdoc-openapi
- Lombok
- Jakarta Validation

## Project Structure

- `src/main/java/com/college/management/controller`
- `src/main/java/com/college/management/service`
- `src/main/java/com/college/management/repository`
- `src/main/java/com/college/management/model`
- `src/main/java/com/college/management/dto`
- `src/main/java/com/college/management/exception`
- `src/main/java/com/college/management/config`

## Prerequisites

- Java 17 or later
- Maven 3.9+
- MongoDB running locally on `mongodb://localhost:27017`

## Run the Application

1. Start MongoDB locally.
2. Update `src/main/resources/application.properties` if your MongoDB URI is different.
3. Run the app:

```bash
export JAVA_HOME=$(/usr/libexec/java_home)
mvn spring-boot:run
```

## Build and Test

```bash
export JAVA_HOME=$(/usr/libexec/java_home)
mvn test
```

## Swagger UI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## REST Endpoints

### Students

- `POST /api/students`
- `GET /api/students`
- `GET /api/students/{id}`
- `PUT /api/students/{id}`
- `DELETE /api/students/{id}`

### Staff

- `POST /api/staff`
- `GET /api/staff`
- `GET /api/staff/{id}`
- `PUT /api/staff/{id}`
- `DELETE /api/staff/{id}`

### Departments

- `POST /api/departments`
- `GET /api/departments`
- `GET /api/departments/{id}`
- `PUT /api/departments/{id}`
- `DELETE /api/departments/{id}`

### Courses

- `POST /api/courses`
- `GET /api/courses`
- `GET /api/courses/{id}`
- `PUT /api/courses/{id}`
- `DELETE /api/courses/{id}`

### Books

- `POST /api/books`
- `GET /api/books`
- `GET /api/books/{id}`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`

### Book Issues

- `POST /api/book-issues/issue`
- `POST /api/book-issues/return`
- `GET /api/book-issues`
- `GET /api/book-issues/student/{studentId}`

## Deliverables

- Source code in this workspace
- Maven build file: [`pom.xml`](pom.xml)
- MongoDB configuration: [`src/main/resources/application.properties`](src/main/resources/application.properties)
- Swagger configuration: [`src/main/java/com/college/management/config/OpenApiConfig.java`](src/main/java/com/college/management/config/OpenApiConfig.java)
- MongoDB setup script: [`mongo/college_management_db_setup.js`](mongo/college_management_db_setup.js)
- MongoDB schemas: [`docs/mongodb-collection-schemas.md`](docs/mongodb-collection-schemas.md)
- Sample request/response payloads: [`docs/sample-json-requests-responses.md`](docs/sample-json-requests-responses.md)
- Postman collection: [`postman/college-management-system.postman_collection.json`](postman/college-management-system.postman_collection.json)

## Notes

- The API uses ID strings as MongoDB document identifiers.
- Student, staff, department, course, and book records are separated into independent collections.
- Book issue operations update the book quantity when a book is issued or returned.
