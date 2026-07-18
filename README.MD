#  Smart Grid Monitoring System

A **Spring Boot Microservices-based Smart Grid Monitoring System** designed to monitor power transformers, collect real-time sensor readings, generate alerts, schedule maintenance, and route requests through an API Gateway.

This project demonstrates the implementation of a distributed microservices architecture using Spring Boot, Spring Data JPA, REST APIs, RestTemplate, and Spring Cloud Gateway.

---

##  Project Overview

The Smart Grid Monitoring System enables real-time monitoring of electrical transformers by collecting sensor data such as voltage, current, temperature, and load percentage.

Based on the collected sensor readings, the system automatically:

- Validates transformer information
- Detects abnormal conditions
- Generates alerts
- Schedules emergency maintenance
- Resolves alerts after maintenance completion

All requests are routed through a centralized **API Gateway**.



##  System Architecture



## Workflow

```
Sensor Reading
      │
      ▼
Validate Transformer
      │
      ▼
Store Reading
      │
      ▼
Threshold Check
      │
      ▼
Generate Alert
      │
      ▼
Schedule Emergency Maintenance
      │
      ▼
Complete Maintenance
      │
      ▼
Resolve Alert
```



## Technology Stack

- Java 21 –> Core programming language
- Spring Boot –> Microservices framework
- Spring Data JPA –> Database access layer
- Hibernate –> Object-Relational Mapping (ORM)
- MySQL –> Relational database
- Maven –> Build and dependency management
- RestTemplate –> Inter-service communication
- Spring Cloud Gateway –> API Gateway and request routing
- Bean Validation –> Input validation
- Global Exception Handling –> Centralized error handling
- Lombok –> Reduces boilerplate code
- Postman –> REST API testing



## Microservices

## 1️ Transformer Service

### Responsibilities

- Manage transformer details
- CRUD operations
- Validate duplicate serial numbers
- Maintain transformer status

### Endpoints

- POST /transformers – Add a new transformer
- GET /transformers – Retrieve all transformers
- GET /transformers/{id} – Retrieve a transformer by ID
- PUT /transformers/{id} – Update transformer details
- DELETE /transformers/{id} – Delete a transformer
---

##  Sensor Service

### Responsibilities

- Record sensor readings
- Validate transformer existence
- Monitor voltage
- Monitor current
- Monitor temperature
- Monitor load percentage
- Automatically generate alerts

### Endpoints

- POST /sensors/readings – Add a new sensor reading
- GET /sensors/readings – Retrieve all sensor readings
- GET /sensors/readings/{id} – Retrieve a sensor reading by ID
- PUT /sensors/readings/{id} – Update a sensor reading
- DELETE /sensors/readings/{id} – Delete a sensor reading
- GET /sensors/readings/transformer/{transformerId} – Retrieve readings by transformer ID

---

## Alert Service

### Responsibilities

- Create alerts
- Categorize alert priority
- Track alert status
- Acknowledge alerts
- Resolve alerts
- Automatically create emergency maintenance

### Endpoints

- POST /alerts – Create a new alert
- GET /alerts – Retrieve all alerts
- GET /alerts/{id} – Retrieve an alert by ID
- PUT /alerts/{id} – Update an alert
- DELETE /alerts/{id} – Delete an alert
- PUT /alerts/{id}/acknowledge – Acknowledge an alert
- PUT /alerts/{id}/resolve – Resolve an alert
- GET /transformer/alert/{transformerId} – Retrieve alerts by transformer ID

---

## Maintenance Service

### Responsibilities

- Schedule maintenance
- Track maintenance progress
- Complete maintenance
- Resolve associated alerts

### Endpoints

- POST /maintenance – Schedule maintenance
- GET /maintenance – Retrieve all maintenance records
- GET /maintenance/{id} – Retrieve maintenance by ID
- PUT /maintenance/{id} – Update maintenance details
- DELETE /maintenance/{id} – Delete maintenance record
- PUT /maintenance/{id}/start – Start maintenance
- PUT /maintenance/{id}/complete – Complete maintenance
- GET /maintenance/transformer/{transformerId} – Retrieve maintenance history by transformer ID

---

##  API Gateway

Acts as the single entry point for all client requests.

Routes:

```
/transformers/**
/sensors/**
/alerts/**
/maintenance/**
```

---

#  Inter-Service Communication

The project uses **RestTemplate** for communication between services.

### Implemented Communication

1] Sensor → Transformer

- Validate transformer before storing readings

---

2] Sensor → Alert

- Automatically generate alerts based on threshold violations

---

3] Alert → Maintenance

- Automatically schedule emergency maintenance

---

4] Maintenance → Alert

- Resolve pending alerts after maintenance completion

---

#  Alert Generation Rules

| Condition | Alert Type | Priority |
|------------|------------|----------|
| Load > 85% | OVERLOAD | HIGH |
| Temperature > 90°C | OVERHEATING | HIGH |
| Voltage < 180V or > 260V | FAULT | HIGH |

---

##  Project Structure

```
smart_grid_monitoring_system
│
├── TransformerService
│
├── SensorService
│
├── AlertService
│
├── MaintenanceService
│
├── api-gateway
│
└── README.md
```

---

##  Database

Each microservice maintains its own database.

- Transformer Database
- Sensor Database
- Alert Database
- Maintenance Database

This follows the **Database per Microservice** design pattern.

---

## Running the Project

Start the services in the following order:

1. Transformer Service
2. Sensor Service
3. Alert Service
4. Maintenance Service
5. API Gateway

Access all APIs through:

```
http://localhost:8080
```

Example:

```
GET http://localhost:8080/transformers
```

---

# Testing

The APIs were tested using **Postman**.

Example workflows tested:

- Add Transformer
- Add Sensor Reading
- Automatic Alert Generation
- Automatic Maintenance Scheduling
- Maintenance Completion
- Alert Resolution

---

## Features Implemented

- REST APIs
- CRUD Operations
- Bean Validation
- Global Exception Handling
- DTO Pattern
- Layered Architecture
- Spring Data JPA
- MySQL Integration
- RestTemplate Communication
- API Gateway Routing
- Automatic Alert Generation
- Automatic Maintenance Scheduling
- Alert Resolution
- Postman Testing


## Author

**Prabhanjan B**

B.E. Computer Science and Engineering

Spring Boot | Java | Microservices | Backend Development

---
