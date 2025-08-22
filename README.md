# E-Commerce Platform Backend - Project Documentation

# 1. Project Overview

This is a Spring Boot-based backend application designed for a multi-role e-commerce platform. Its design is based on a layered architecture, where the responsibility of each component is clearly defined. This project serves as an MVP (Minimum Viable Product) that covers all the essential features of an e-commerce system, such as user management, product catalog, order processing, and payments.

# 2. Core Features & Scope

The application supports three primary roles: Customer, Seller, and Owner/Admin.

## Customer

* Browse and search for products.
* Add, update, and remove products from the shopping cart.
* Place an order from the cart.
* View the status of their orders.
* Write reviews for products.

## Seller

* Register on the platform (subject to approval).
* Manage (create, read, update, delete) their own products.
* View orders placed for their products.
* Update the status of orders (PENDING -> PROCESSING -> SHIPPED ->(DELIVERED/CANCELLED) ).

## Owner (Admin)

* APPROVE or REJECT new seller registrations.
* Manage platform-wide categories.
* View all users, products, and orders on the platform.

# 3. Design & Domain Modeling

## 3.1. Layered Architecture

We have utilized a classic 3-tier architecture:

* Controller (Presentation Layer): Handles the REST API endpoints.
* Service (Business Logic Layer): Implements all business rules, validation, and logic.
* Repository (Data Access Layer): Responsible for communicating with the database.

## 3.2. Key Design Decisions

* DTOs (Data Transfer Objects): The DTO pattern is used to ensure a clean separation between the API layer and the database entities. This allows us to send only the necessary data to the client and avoid exposing sensitive information (like password hashes).

* Transactional Services: All methods in the service layer (@Service) are marked as @Transactional to maintain data integrity. If any operation fails, the entire transaction is rolled back.

* Global Exception Handling: A centralized exception handler using @RestControllerAdvice has been implemented to send consistent JSON responses for all errors.

* Logging with AOP: We use Spring AOP (Aspect-Oriented Programming) to implement logging as a cross-cutting concern. A loggingAspect  intercepts method calls across different layers (Controllers, Services, Repositories) to log entry, exit, and execution time without cluttering the business logic.

## 3.3. Core Entities

* User
* Role
* Seller
* Product
* Categories
* Order & OrderItem
* Payment
* Review
* Cart & CartItem
* ShippingAddress
* Wishlist & WishlistItem
* Commission

# 4. Technical Architecture & Implementation

## 4.1. Technology Stack

* Framework: Spring Boot
* Language: Java
* Database: PostgreSQL (Production)
* Data Access: Spring Data JPA / Hibernate
* Security: Spring Security
* Build Tool: Maven
* Utilities: Lombok

## 4.2. Project Package Layout
```
app.ecom
├── config          // SecurityConfig, etc.
├── controller      // REST API Controllers
├── dto             // Data Transfer Objects (request/response)
│   ├── mappers
│   ├── request_dto
│   └── response_dto
├── entities        // JPA Entities
├── exceptions      // Custom exceptions and global handler
├── logging         // AOP Logging Aspect
├── services        // Business logic
└── repositories    // Spring Data JPA Repositories
```
## 4.3. REST API Endpoints (CRUD Examples)

## Product Domain:

* GET /api/products: Retrieves a list of all products. Can be filtered, e.g., ?category=ELECTRONICS.
* GET /api/products/{id}: Retrieves details for a specific product.
* POST /api/products: Creates a new product (accessible to SELLER role only).
* PUT /api/products/{id}: Updates an existing product (accessible to SELLER role only).
  
## Order Domain:

* POST /api/orders: Creates a new order (accessible to CUSTOMER role only).
* GET /api/orders/{id}: Retrieves details for a specific order.
* PUT /api/orders/{id}/seller-by-user/{userId}/status: Updates the order status (accessible to the authentic SELLER of that order only).

# 5. Testing Strategy

* The project focuses on primary type of testing:
Unit Tests: The business logic within service classes like UserService and OrderService is tested in isolation. This is achieved using JUnit and Mockito. Mockito allows us to "mock" the repository layer, eliminating the need for a real database.

# 6. Future Enhancements & Roadmap

While the current version serves as a functional MVP, the following features are planned for future releases:

* ** Payment Gateway Integration ** :
  Integrate with a real payment gateway like Stripe or Razorpay to handle actual transactions.

* JWT Authentication: Implement JSON Web Tokens (JWT) for stateless and more secure API authentication.

* AI-Powered Virtual Try-On: Implement an AI/ML feature allowing users to upload their photo and visualize how fashion products (e.g., clothing, accessories) would look on them, enhancing the shopping experience.

* AR/VR Virtual Try-On: Implement an Augmented/Virtual Reality feature that allows users to visualize products (like apparel or furniture) in their own space in real-time using their device's camera.

* Admin Dashboard: Develop a simple UI for the Owner/Admin to manage users, approve sellers, and view platform analytics.

* Discount & Coupon System: Add functionality for creating and applying discount codes to orders.

* Product Recommendations: Implement a basic recommendation engine to suggest products to users based on their browsing history or past purchases.

* Deployment: Containerize the application using Docker and deploy it to a cloud platform like AWS or Heroku.

