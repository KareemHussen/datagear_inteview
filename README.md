# DataGear Transaction Management System

A full-stack application for managing financial transactions with a Spring Boot backend and Angular frontend.

## 📋 Table of Contents

- [AI-Assisted Development](#-ai-assisted-development)
- [Overview](#overview)
- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [Running the Application](#running-the-application)
- [Development Guide](#development-guide)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Features](#features)
- [Additional Resources](#additional-resources)

## 🤖 AI-Assisted Development

This project was developed with the assistance of Cursor AI. You can explore the development process and technical discussions in the following documentation files:

- **[Creating Frontend with Angular](Cursor%20Documentation/cursor_create_frontend_angualr.md)** - Building the Angular frontend with modern components and PrimeNG
- **[Creating RESTful API for Transactions](Cursor%20Documentation/cursor_create_restful_api_for_transaction.md)** - Implementing the Spring Boot REST API for transaction management
- **[Creating Standardized API Response](Cursor%20Documentation/cursor_create_standardized_api_response.md)** - Designing consistent API responses and error handling
- **[Dockerizing Spring Boot and Angular](Cursor%20Documentation/cursor_dockerize_spring_boot_and_angular.md)** - Containerizing the full-stack application with Docker

> **Note**: These files contain detailed conversations about architecture decisions, implementation strategies, problem-solving approaches, and best practices used throughout the development process.

## 🎯 Overview

DataGear is a comprehensive transaction management system built with modern technologies and best practices. It provides a complete solution for managing financial transactions with a beautiful, responsive UI and a robust, performant backend API.

### Backend Highlights

- **H2 In-Memory Database** for fast development and testing
- **3 REST Endpoints** for complete transaction management
- **Advanced Filtering & Sorting** by date, type, and amount
- **Database Seeder** for generating test data
- **Performance Optimization** via caching, indexing, and native SQL
- **Spring Doc OpenAPI** with Swagger UI
- **Liquibase** for database migrations
- **CORS Configuration** to allow secure access from Angular frontend and Swagger UI
- **Clean Architecture** (Controller, Service, Repository)
- **MapStruct** for efficient DTO-to-Entity mapping
- **43+ Test Cases** with comprehensive coverage

### Frontend Highlights

- **3 Interactive Pages** for complete transaction management
- **Modern UI** with PrimeNG components
- **HTTP Client** for seamless API communication
- **Angular Signals** for reactive state management
- **Advanced Filtering & Sorting** with real-time updates
- **Comprehensive Testing** suite
- **Environment-based Configuration**

## ⚡ Quick Start

### Using Docker Compose (Recommended)

The easiest way to run the entire application:

```bash
# Clone the repository
git clone <repository-url>
cd DataGear

# Start all services (frontend + backend)
docker-compose up

# Or run only one service
# docker-compose up backend    # Backend only
# docker-compose up frontend   # Frontend only

# Access the application
# Frontend: http://localhost:4200
# Backend API: http://localhost:8080/api/v1
```

## 🏗 Architecture

- **Backend**: Spring Boot REST API (Port 8080)
- **Frontend**: Angular application (Port 4200)
- **Database**: H2 In-Memory Database

## 🚀 Running the Application

### Option 1: Docker Compose (Recommended)

Run the entire stack or individual services with Docker Compose:

```bash
# Start all services (frontend + backend)
docker-compose up

# Start in detached mode (background)
docker-compose up -d

# Stop all services
docker-compose down

# Rebuild and start
docker-compose up --build

# View logs
docker-compose logs -f

# Run only the backend service
docker-compose up backend

# Run only the frontend service
docker-compose up frontend
```

### Option 2: Manual Setup

#### Backend Setup

```bash
# Navigate to backend
cd DataGearBackend

# Run with Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/DataGearBackend-0.0.1-SNAPSHOT.jar
```

#### Frontend Setup

```bash
# Navigate to frontend
cd DataGearFrontEnd

# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm run build
```

### Option 3: Docker (Individual Services)

#### Backend Only

```bash
cd DataGearBackend
docker build -t datagear-backend .
docker run -p 8080:8080 datagear-backend
```

#### Frontend Only

```bash
cd DataGearFrontEnd
docker build -t datagear-frontend .
docker run -p 4200:80 datagear-frontend
```

## 🔧 Development Guide

For detailed development instructions, please refer to the individual project READMEs:

- **[Backend README](DataGearBackend/README.md)** - Spring Boot setup, API documentation, testing
- **[Frontend README](DataGearFrontEnd/README.md)** - Angular setup, components, deployment

## 📡 API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Endpoints

- **GET /transactions** - Get all transactions with filtering, sorting, and pagination

  - Query params: `page`, `size`, `type`, `fromDate`, `toDate`, `minAmount`, `maxAmount`, `sort`
- **POST /transactions** - Create a new transaction

  - Body: `{ "amount": number, "type": "Credit"|"Debit", "note": string }`
- **GET /transactions/totals** - Get transaction statistics

  - Response: Total credits, debits, and counts

### Interactive Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/v3/api-docs

## 📁 Project Structure

```
DataGear/
├── DataGearBackend/           # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/datagear/DataGearInterview/
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── enums/           # Enumerations
│   │   │   │   ├── exception/       # Exception handlers
│   │   │   │   ├── mapper/          # MapStruct mappers
│   │   │   │   ├── repository/      # Data repositories
│   │   │   │   └── service/          # Business logic
│   │   │   └── resources/
│   │   │       ├── db/              # Liquibase migrations
│   │   │       └── application.properties
│   │   └── test/                    # Test suites
│   ├── Dockerfile
│   ├── pom.xml
│   └── README.md
├── DataGearFrontEnd/           # Angular Frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/          # Angular components
│   │   │   ├── models/              # TypeScript models
│   │   │   ├── services/            # API services
│   │   │   ├── environments/        # Environment configs
│   │   │   └── app.routes.ts
│   │   ├── assets/                  # Static assets
│   │   └── index.html
│   ├── Dockerfile
│   ├── package.json
│   ├── angular.json
│   └── README.md
├── Cursor Documentation/       # Development Documentation
│   ├── cursor_create_frontend_angualr.md
│   ├── cursor_create_restful_api_for_transaction.md
│   ├── cursor_create_standardized_api_response.md
│   └── cursor_dockerize_spring_boot_and_angular.md
├── docker-compose.yaml         # Docker Compose configuration
└── README.md                   # This file
```

## ✨ Features

### Backend Features

- ✅ RESTful API with 3 endpoints
- ✅ Advanced filtering (type, date range, amount range)
- ✅ Dynamic sorting by any field
- ✅ Server-side pagination
- ✅ Database seeder for test data
- ✅ Clean Architecture pattern
- ✅ MapStruct for DTO mapping
- ✅ Liquibase migrations
- ✅ CORS Configuration
- ✅ Spring Cache for performance
- ✅ Database indexing
- ✅ Native SQL optimization
- ✅ Swagger/OpenAPI documentation
- ✅ 43+ comprehensive test cases
- ✅ Global exception handling

### Frontend Features

- ✅ 3 interactive pages
- ✅ Modern UI with PrimeNG
- ✅ HTTP Client with caching
- ✅ Angular Signals for state
- ✅ Advanced filtering & sorting
- ✅ Responsive design
- ✅ Environment configuration
- ✅ Comprehensive testing

## 🌐 Access Points

Once running, access the application at:

- **Frontend**: http://localhost:4200
- **Backend API Base URL**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: *(empty)*

## 📚 Additional Resources

### Tech Stack

**Backend**

- Spring Boot 3.5.7
- Java 17
- H2 Database
- MapStruct 1.6.3
- Liquibase
- Spring Doc OpenAPI
- Faker for fake Data (Seeder)

**Frontend**

- Angular 20.3.7
- TypeScript 5.8.2
- PrimeNG 20.2.0
- RxJS
- Nginx (for Docker)

## 🧪 Testing

### Backend Tests

```bash
cd DataGearBackend
./mvn test
```