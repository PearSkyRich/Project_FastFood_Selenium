# FastFood Management System – Testing & Quality Assurance

## Overview

This repository contains my Software Testing work for the **FastFood Management System**, a web-based restaurant management application developed using **Spring Boot** and **React**.

Although the application was originally developed by a project team, my primary responsibility focused on designing, implementing, and automating software testing to improve system reliability and software quality.

---

# My Contributions

## Unit Testing

- Developed **199 Unit Test cases** using **JUnit 5** and **Mockito**.
- Tested:
  - Controllers
  - Service Layer
  - Mapper Classes
- Covered normal cases, boundary cases, and exception handling.
- Generated code coverage reports using **JaCoCo**.

---

## Integration Testing

Implemented **21 Integration Tests** using:

- Spring Boot Test
- MockMvc
- MySQL Test Database

Verified:

- REST API endpoints
- Business logic
- Database transactions
- Repository interactions
- Controller-Service-Repository integration

---

## End-to-End Testing

Built automated UI tests using:

- Selenium WebDriver
- Java
- TestNG
- Page Object Model (POM)

Implemented automated scenarios:

- User Login
- Add Food to Cart
- Multi-user Ordering
- Order Processing

---

## Performance Testing

Designed JMeter test plans to evaluate system performance.

Test scenarios include:

- Concurrent user login
- Multiple users placing orders simultaneously
- API response time
- Throughput
- Error rate
- Stress and Load Testing

---

## Continuous Integration & Code Quality

Configured automated testing pipeline using:

- Maven
- GitHub Actions
- JaCoCo
- SonarQube

Pipeline automatically:

- Build project
- Execute Unit Tests
- Execute Integration Tests
- Generate Code Coverage Report
- Perform Static Code Analysis

---

# Technologies

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL

## Frontend

- React
- Vite
- Ant Design

## Testing

- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- Selenium WebDriver
- TestNG
- Apache JMeter
- JaCoCo

## DevOps

- Maven
- GitHub Actions
- SonarQube

---

# Testing Summary

| Test Type | Quantity |
|------------|---------:|
| Unit Tests | 199 |
| Integration Tests | 21 |
| Selenium End-to-End Tests | 4 |
| Performance Tests | Multiple JMeter Scenarios |

---

# Project Structure

```
backend/
│
├── src/main
├── src/test
│   ├── unit
│   ├── integration
│   └── resources
│
frontend/
│
automation/
│   ├── pages
│   ├── tests
│   ├── data
│   └── reports
│
docs/
```

---

# Key Testing Features

✔ Unit Testing

✔ Integration Testing

✔ REST API Testing

✔ UI Automation Testing

✔ Performance Testing

✔ Code Coverage Analysis

✔ Static Code Analysis

✔ Continuous Integration

---

# Skills Demonstrated

- Software Testing
- Test Automation
- Unit Testing
- Integration Testing
- End-to-End Testing
- Performance Testing
- REST API Testing
- Selenium WebDriver
- JUnit 5
- Mockito
- MockMvc
- Apache JMeter
- GitHub Actions
- SonarQube
- Maven

---

# Notes

This repository highlights my contributions in **Software Testing and Quality Assurance** for the FastFood Management System project. My work focused on designing automated tests, validating application functionality, evaluating performance, and integrating testing into the CI/CD workflow to improve software quality.

## Documentation

[[Software Quality Assurance Report](https://pearskyrich.github.io/Project_FastFood_Selenium/Project_FastFoodShop_Testing.docx)]


## Acknowledgements

The Fast Food Management System reviewed in this repository was developed by another project team.
This repository contains only the Software Quality Assurance (SQA) review conducted as part of the Software Quality Assurance course.
Original Project:
https://github.com/username/fast-food-management-system
