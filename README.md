
# **bTailor Microservices Workspace**

Welcome to the **bTailor Microservices Workspace**!  
This project is a modular, microservices-based backend system built using **Spring Boot**, **Spring Cloud**, **Spring WebFlux**, and **gRPC**.

---

## **Project Structure**

This workspace uses a **multi-module Maven project** structure. The main modules are:

- **common-library**: Shared utilities and components across services.
- **api-gateway**: Entry point for all client requests, handling routing, authentication, and load balancing.
- **user-auth-service**: Handles user registration, login, JWT authentication, and authorization (Spring MVC).
- **product-service**: Manages product catalogs, product details, and product-related operations (Spring MVC).
- **inventory-service**: Manages inventory stock and availability (Spring MVC).
- **cart-service**: Reactive cart management using **Spring WebFlux** with **Redis** as the backend store.
- **order-service**: Reactive order management using **Spring WebFlux** and **MongoDB** as the database.
- **notification-service**: Reactive notifications handling using **Spring WebFlux**.
- **payment-service**: Manages payment processing and transaction tracking (Spring MVC) with **gRPC** integration for communication with the **order-service**.

> **Note:** The `common-library` module appears twice in the `pom.xml`. Please correct it to appear only once.

---

## **Technologies Used**

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Cloud 2022.0.4**
- **Spring Cloud Consul** for service discovery
- **Spring WebFlux** (Reactive) for **Notification**, **Order**, and **Cart** services
- **Redis** (for cart data storage)
- **MongoDB** (for order data storage)
- **Maven** (for project management)
- **Microservice architecture**
- **RESTful APIs** (Reactive and Non-Reactive)
- **gRPC** for efficient service communication (order-service to payment-service)
- **Docker/Kubernetes** (future deployment plans)
- **JWT Authentication** (user-auth-service)
- **API Gateway** (Spring Cloud Gateway)

---

## **Service Communication and Microservices Architecture**

### **1. Service-to-Service Communication**

Services communicate **internally** via:

- **OpenFeign Clients** (for blocking MVC-based services)
- **WebClient** (for reactive WebFlux-based services)
- **gRPC** (for communication between **order-service** and **payment-service**)

Example for using **WebClient** in WebFlux:

```java
@Autowired
private WebClient.Builder webClientBuilder;

public Mono<ProductResponse> getProductById(String id) {
    return webClientBuilder.build()
        .get()
        .uri("http://product-service/api/products/" + id)
        .retrieve()
        .bodyToMono(ProductResponse.class);
}
```

gRPC communication example (Order to Payment):

```java
@Service
public class PaymentServiceGrpcClient {
    
    private final PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceBlockingStub;

    public PaymentServiceGrpcClient(ManagedChannel channel) {
        this.paymentServiceBlockingStub = PaymentServiceGrpc.newBlockingStub(channel);
    }

    public PaymentResult processPayment(String orderId, double amount, String method, long userId) {
        PaymentRequest paymentRequest = PaymentRequest.newBuilder()
            .setOrderId(orderId)
            .setAmount(amount)
            .setMethod(method)
            .setUserId(userId)
            .build();
        
        return paymentServiceBlockingStub.processPayment(paymentRequest);
    }
}
```

- **Load Balancing** via Consul for service instances.

---

### **2. API Gateway**

The **API Gateway**:

- Single entry point for client requests.
- Handles authentication (validates JWT tokens).
- Routes to appropriate microservices dynamically using service discovery.

Routing example:

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: user-auth-service
          uri: lb://user-auth-service
          predicates:
            - Path=/api/auth/**
        - id: product-service
          uri: lb://product-service
          predicates:
            - Path=/api/products/**
```

---

### **3. Service Discovery (Consul)**

All services register themselves with **Consul**.

Minimal config:

```yaml
spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
```

---

### **4. Database and Storage**

| Service           | Technology        |
|-------------------|-------------------|
| **cart-service**   | Redis (Reactive)  |
| **order-service**  | MongoDB (Reactive)|
| **other services** | Relational DB (MySQL/PostgreSQL, etc.) |

---

### **5. Security**

- **JWT Authentication** handled by `user-auth-service`.
- **API Gateway** validates tokens before routing.
- **Role-Based Access Control** based on JWT claims.

---

## **Getting Started**

### **Prerequisites**

- **Java 17+**
- **Maven 3.8+**
- **Docker** (optional for containerization)
- **Redis** server (for cart-service)
- **MongoDB** server (for order-service)
- **PostgreSQL** server (for other-services)

### **Build the Project**

To build the project and install the required dependencies, run:

```bash
mvn clean install
```

### **Running a Service**

To run any specific service, navigate to the service directory and run:

```bash
cd <module-name>
mvn spring-boot:run
```

Example:

```bash
cd user-auth-service
mvn spring-boot:run
```

---

## **Development Guidelines**

- Follow **clean code** practices.
- Use **DTOs** for external communication.
- Implement **global exception handling**.


---

## **Future Enhancements**

- [x] Reactive microservices with WebFlux
- [x] Redis for cart management
- [x] MongoDB for order storage
- [x] **gRPC** for efficient communication between **order-service** and **payment-service**
- [ ] Centralized service discovery with **Consul**
- [ ] Distributed tracing (Sleuth + Zipkin)
- [ ] Centralized logging (ELK stack)
- [ ] Containerization (Docker/Kubernetes)
- [ ] CI/CD pipelines (GitHub Actions, Jenkins)

---


