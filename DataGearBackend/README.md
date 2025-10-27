# DataGear Backend

A high-performance REST API for transaction management built with Spring Boot, featuring clean architecture, comprehensive testing, and advanced database optimization.

## 📋 Overview

DataGear Backend is a Spring Boot application that provides a robust, scalable solution for managing financial transactions. It implements clean architecture principles, comprehensive caching strategies, and high-performance database queries.

## 🚀 Features

### Core Functionality
- **3 REST Endpoints** for complete transaction management
  - `GET /api/v1/transactions` - Retrieve all transactions with advanced filtering, sorting, and pagination
  - `POST /api/v1/transactions` - Create new transactions with validation
  - `GET /api/v1/transactions/totals` - Get real-time transaction statistics

### Filtering & Sorting
- Filter by transaction type (Credit/Debit)
- Filter by date range
- Filter by amount range (min/max)
- Dynamic sorting by any field (date, amount, type)
- Server-side pagination for optimal performance

### Architecture & Best Practices
- **Clean Architecture** with clear separation of concerns:
  - Controllers - REST API layer
  - Services - Business logic layer
  - Repositories - Data access layer
- **MapStruct** for efficient DTO-to-Entity mapping
- **Liquibase** for database version control and migrations
- **Global Exception Handling** with comprehensive error responses

### Performance Optimization
- **Caching** implementation using Spring Cache abstraction
- **Database Indexing** on key columns for faster queries
- **Native SQL Queries** for optimized data retrieval
- **Connection pooling** for efficient database connections

### Documentation
- **Spring Doc OpenAPI/Swagger** integration
- Interactive API documentation at `/swagger-ui.html`
- Complete API schema and request/response examples

### Testing
- **43+ Test Cases** with comprehensive coverage
- Unit tests for services and business logic
- Integration tests for REST endpoints
- Repository layer tests with H2 database

### Development Tools
- **Database Seeder** for generating test data
- H2 In-Memory Database for development
- H2 Console for database inspection
- Spring Boot DevTools for hot reload

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Database**: H2 In-Memory Database
- **ORM**: Spring Data JPA
- **Migration**: Liquibase
- **Mapping**: MapStruct
- **Documentation**: Spring Doc OpenAPI
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito

## 📁 Project Structure

```
src/main/java/com/datagear/DataGearInterview/
├── config/
│   ├── CorsConfig.java          # Cors configuration
│   ├── CacheConfig.java          # Caching configuration
│   ├── DatabaseSeeder.java        # Test data seeder
│   └── OpenApiConfig.java         # Swagger configuration
├── controller/
│   └── TransactionController.java # REST endpoints
├── dto/
│   ├── request/                   # Request DTOs
│   ├── response/                  # Response DTOs
│   └── projection/               # Projection interfaces
├── entity/
│   └── Transaction.java           # JPA entity
├── enums/
│   └── TransactionType.java       # Transaction type enum
├── exception/
│   └── GlobalExceptionHandler.java # Exception handling
├── mapper/
│   └── TransactionMapper.java     # MapStruct mapper
├── repository/
│   ├── TransactionRepository.java # JPA repository
│   └── TransactionSpecifications.java # Query specifications
└── service/
    └── TransactionService.java    # Business logic
```

## 🏃 Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Using Maven (Development)

```bash
# Navigate to backend directory
cd DataGearBackend

# Run the application
mvn spring-boot:run

```

### Using Docker

```bash
# Build the image
docker build -t datagear-backend .

# Run the container
docker run -p 8080:8080 datagear-backend
```

### Using Docker Compose (Recommended)

From the root directory:

```bash
docker-compose up backend
```

## 🌐 Access Points

- **API Base URL**: `http://localhost:8080/api/v1`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (leave empty)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/v3/api-docs`

## 🧪 Running Tests

```bash
# Run all tests
mvn test
```

## 📝 API Examples

### Get Transactions with Filtering

```bash
GET /api/v1/transactions?page=0&size=10&type=Credit&sort=date,desc
```

### Create Transaction

```bash
POST /api/v1/transactions
Content-Type: application/json

{
  "amount": 1500.00,
  "type": "Credit",
  "note": "Salary payment",
}
```

### Get Statistics

```bash
GET /api/v1/transactions/totals
```

## 🎯 Performance Features

- **Database Indexing**: Automatic indexing on frequently queried columns
- **Query Optimization**: Native SQL queries for complex aggregations
- **Response Caching**: Intelligent caching to reduce database load
- **Connection Pooling**: Efficient resource management

## 🔧 Configuration

Key configuration files:
- `application.properties` - Main configuration
- `application-test.properties` - Test configuration
- `db/liquibase-changelog.yaml` - Database migrations

## 📦 Build & Deploy

```bash
# Build JAR
mvn clean package

# Build with Docker
docker build -t datagear-backend .

# Run JAR
java -jar target/DataGearBackend-0.0.1-SNAPSHOT.jar
```

## 🐛 Troubleshooting

### H2 Console Access Issues
Ensure `spring.h2.console.enabled=true` in application.properties

### Port Already in Use
Change the port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Issues
Verify H2 is configured correctly in `application.properties`

## 📄 License

This project is part of the DataGear Interview System.

