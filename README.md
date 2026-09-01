# Hospital Management System

A backend-focused Hospital Management System built with **Java and Spring Boot** to manage hospital records, patients, physicians, appointments, departments, medications, prescriptions, rooms, and other clinical information.

The application follows a **layered architecture** using Spring Boot, Spring Data JPA, and MySQL. It also includes deployment on **AWS EC2**, database hosting through **AWS RDS**, and an automated **GitHub Actions CI/CD workflow**.

---

## Tech Stack

| Category | Technologies |
|----------|--------------|
| Language | Java 21 |
| Framework | Spring Boot 3.2.5 |
| Backend | Spring Web, Spring Data JPA |
| ORM | Hibernate / JPA |
| Database | MySQL |
| Validation | Jakarta Validation |
| Build Tool | Maven |
| Cloud | AWS EC2, AWS RDS |
| CI/CD | GitHub Actions |
| Version Control | Git, GitHub |

---

## Features

- Patient management
- Physician management
- Appointment management
- Department management
- Medication management
- Prescription management
- Room management
- Hospital stay management
- Clinical record management
- RESTful CRUD APIs
- JSON-based request and response handling
- Input validation
- HTTP status-based API responses
- Relational data management using MySQL
- AWS EC2 deployment
- AWS RDS database integration
- Automated deployment using GitHub Actions

---

## Architecture

The application follows a layered backend architecture:

```text
                    Client
                      |
                      v
              +---------------+
              |   Controller  |
              |     Layer     |
              +---------------+
                      |
                      v
              +---------------+
              |    Service    |
              |     Layer     |
              +---------------+
                      |
                      v
              +---------------+
              |  Repository   |
              |     Layer     |
              +---------------+
                      |
                      v
              +---------------+
              |     MySQL     |
              |    Database   |
              +---------------+
