# Book Service

This is a sample project illustrating a gRPC-based book service implemented in Java using Spring Boot and protobuf for communication.

## Prerequisites

Make sure you have the following installed:
- Java Development Kit (JDK) version 17 or higher
- PostgreSQL database

## Setup

1. Clone this repository to your local machine:
```bash
git clone https://github.com/whereismidel/bookstore-management-app.git
```
2. Navigate to the project directory:
```bash
cd book-service
```

3. Update the database configurations in `src/main/resources/application.properties` according to your PostgreSQL setup:
```springdataql
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Build the project using Gradle:
```bash
gradlew clean build
```

## Run

1. Once the project is built successfully, you can run it using Maven:
```bash
gradlew run
```
This will start the Spring Boot application and the gRPC server will be up and running.

## gRPC Service

The gRPC service will be available at `localhost:9090`.

## APIs

- `GetAllBooks`: Fetches all books.
- `GetBookById`: Fetches a book by its ID.
- `AddBook`: Adds a new book.
- `UpdateBook`: Updates an existing book.
- `DeleteBook`: Deletes a book by its ID.

## Technologies Used

- Java
- Spring Boot
- gRPC
- Protocol Buffers (protobuf)
- MapStruct
- Lombok
- PostgreSQL
