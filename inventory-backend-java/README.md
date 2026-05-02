# Stock Pro+ — Java Backend

REST API for the **Stock Pro+** Inventory Management System, built with **Spring Boot 3**, **JPA/Hibernate**, and **H2 / MySQL**.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2 |
| Language | Java 17 |
| ORM | Spring Data JPA + Hibernate |
| Dev DB | H2 (in-memory) |
| Prod DB | MySQL 8 |
| Build | Maven |
| Tests | JUnit 5 + Mockito |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- MySQL 8 *(prod only)*

### Run in Dev (H2 in-memory)

```bash
git clone https://github.com/<your-username>/stock-pro-backend-java.git
cd stock-pro-backend-java

mvn spring-boot:run
```

Server starts at **http://localhost:8080**  
H2 Console at **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:inventorydb`)

### Run in Prod (MySQL)

1. Create the database:
```sql
CREATE DATABASE inventorydb;
```

2. Set environment variables:
```bash
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
```

3. Run with prod profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Build JAR

```bash
mvn clean package
java -jar target/inventory-backend-1.0.0.jar
```

---

## API Reference

Base URL: `http://localhost:8080/api`

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/products` | List all products |
| `GET` | `/products?search=mouse` | Search by name or description |
| `GET` | `/products/{id}` | Get single product |
| `POST` | `/products` | Create product |
| `PUT` | `/products/{id}` | Full update |
| `PATCH` | `/products/{id}` | Partial update |
| `DELETE` | `/products/{id}` | Delete product |
| `GET` | `/products/summary` | Dashboard summary stats |
| `GET` | `/products/export/csv` | Download inventory as CSV |

### Request Body (POST / PUT)

```json
{
  "name": "Wireless Mouse",
  "desc": "Ergonomic 2.4GHz wireless",
  "price": 29.99,
  "qty": 150
}
```

### Response Format

```json
{
  "success": true,
  "count": 5,
  "data": [ ... ],
  "timestamp": "2024-05-02T12:00:00"
}
```

### Summary Response

```json
{
  "success": true,
  "data": {
    "total": 5,
    "lowStock": 2,
    "totalValue": 12499.50
  }
}
```

---

## Project Structure

```
src/main/java/com/stockpro/
├── InventoryApplication.java   # Entry point
├── config/
│   ├── WebConfig.java          # CORS setup
│   └── DataSeeder.java         # Dev sample data
├── controller/
│   └── ProductController.java  # REST endpoints
├── service/
│   └── ProductService.java     # Business logic
├── repository/
│   └── ProductRepository.java  # DB queries (JPA)
├── model/
│   └── Product.java            # JPA entity
├── dto/
│   ├── ProductDTO.java         # Request/Response/Summary DTOs
│   └── ApiResponse.java        # Generic response wrapper
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## Connecting the Frontend

Update the frontend's fetch calls from `localStorage` to hit this API:

```js
// Replace localStorage reads/writes like this:
const BASE = "http://localhost:8080/api";

// Get all products
const res = await fetch(`${BASE}/products`);
const { data } = await res.json();

// Create product
await fetch(`${BASE}/products`, {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({ name, desc, price, qty })
});
```

---

## Running Tests

```bash
mvn test
```
