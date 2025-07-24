# Library Management System (LMS)

This is a Spring Boot based Library Management System that provides RESTful APIs for managing a library's resources.

## Project Structure

```
src/main/java/com/lms/
├── config/         # Configuration classes
├── controller/     # REST controllers
├── model/          # Entity classes
├── repository/     # JPA repositories
└── service/        # Business logic
```

## Technologies Used

- Spring Boot 2.7.0
- Spring Data JPA
- MySQL Database
- Maven
- Java 11
- Lombok

## Setup Instructions

1. Ensure you have the following installed:
   - Java 11
   - Maven
   - MySQL

2. Configure the database connection in `src/main/resources/application.properties`

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on port 8080.

## Database Configuration

The default database configuration is:
- URL: jdbc:mysql://localhost:3306/lms
- Username: root
- Password: root

You can modify these settings in the `application.properties` file.
