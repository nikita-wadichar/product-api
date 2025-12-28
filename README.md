# Product Management REST API

## About
Spring Boot REST API for product CRUD operations with authentication, validation, and PostgreSQL database.

## Features
- Create, Read, Update, Delete products
- API Key authentication
- Input validation with custom errors
- Swagger documentation
- PostgreSQL database
- Global exception handling

## Tech Stack
- Java 21
- Spring Boot 3.2.12
- PostgreSQL 14+
- Spring Security
- Swagger/OpenAPI

## Quick Start

### 1. Clone & Setup
```bash
git clone https://github.com/yourusername/product-api.git
cd product-api
```

### 2. Start Database
```bash
docker run --name postgres-product \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=productdb \
  -p 5432:5432 \
  -d postgres:14
```

### 3. Configure Application
Create `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/productdb
    username: postgres
    password: password
  jpa:
    hibernate.ddl-auto: update

server:
  port: 8080

security:
  api:
    key: secure-api-key-12345
```

### 4. Run Application
```bash
mvn clean spring-boot:run
```

## API Endpoints

### Products
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/{id}` - Get product by ID
- `POST /api/v1/products` - Create product
- `PUT /api/v1/products/{id}` - Update product
- `DELETE /api/v1/products/{id}` - Delete product

### Authentication
- `POST /api/v1/auth/apikey` - Get API key

## Authentication
Add header to all requests:
```
X-API-Key: secure-api-key-12345
```

## API Usage

### Create Product
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -H "X-API-Key: secure-api-key-12345" \
  -d '{
    "name": "Laptop",
    "description": "High performance laptop",
    "price": 1299.99
  }'
```

### Get All Products
```bash
curl -X GET http://localhost:8080/api/v1/products \
  -H "X-API-Key: secure-api-key-12345"
```

### Get Product by ID
```bash
curl -X GET http://localhost:8080/api/v1/products/1 \
  -H "X-API-Key: secure-api-key-12345"
```

### Update Product
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -H "X-API-Key: secure-api-key-12345" \
  -d '{
    "name": "Updated Laptop",
    "description": "Updated description",
    "price": 1499.99
  }'
```

### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/v1/products/1 \
  -H "X-API-Key: secure-api-key-12345"
```

## Validation Rules
- Name: 2-100 characters, required
- Description: Max 500 characters, optional
- Price: Min 0.01, max 999999.99, required
- Product names must be unique

## Error Examples
### Validation Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Price must be greater than 0",
  "path": "/api/v1/products"
}
```

### Authentication Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid API key",
  "path": "/api/v1/products"
}
```

## Testing
```bash
# Run tests
mvn test

# Build JAR
mvn clean package -DskipTests

# Run with coverage
mvn clean test jacoco:report
```

## API Documentation
Access after running:
```
http://localhost:8080/swagger-ui.html
```

## Project Structure
```
product-api/
├── src/main/java/com/example/productapi/
│   ├── config/
│   │   └── SwaggerConfig.java
│   ├── controller/
│   │   ├── ProductController.java
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── ProductRequest.java
│   │   ├── ProductResponse.java
│   │   └── ErrorResponse.java
│   ├── entity/
│   │   └── Product.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── ValidationException.java
│   ├── repository/
│   │   └── ProductRepository.java
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   └── ApiKeyAuthFilter.java
│   └── service/
│       ├── ProductService.java
│       └── ProductServiceImpl.java
├── src/main/resources/
│   └── application.yml
├── pom.xml
└── README.md
```

## Database Schema
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Troubleshooting
### Common Issues
1. **Port 8080 already in use**
   ```bash
   # Find and kill process using port 8080
   netstat -ano | findstr :8080
   ```

2. **Database connection failed**
   - Check PostgreSQL is running
   - Verify credentials in application.yml

3. **Lombok not working**
   - Enable annotation processing in IDE
   - Install Lombok plugin

### Check Application Health
```bash
curl http://localhost:8080/actuator/health
```

## Deployment
### Build Production JAR
```bash
mvn clean package -DskipTests
java -jar target/product-api-1.0.0.jar
```

### Docker Deployment
```bash
docker build -t product-api .
docker run -p 8080:8080 product-api
```