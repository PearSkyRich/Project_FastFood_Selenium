# FastFood Management System – Testing & Quality Assurance

## Overview

This repository contains the Software Testing and Quality Assurance work for the **FastFood Management System**, a web-based restaurant management application.

The system supports key restaurant operations including **food management, order and cart management, kitchen management, payment, inventory, and table management**. fileciteturn1file7L523-L559

My main responsibility in this project focused on **software testing, test case design, test automation, code coverage analysis, performance testing, and defect identification**.

---

## Testing Scope

The project applies multiple levels and types of testing:

- **Manual Functional Testing**
- **Unit Testing**
- **Integration Testing**
- **Automation Testing**
- **Performance Testing**
- **Code Coverage Analysis**
- **Defect Management**

### Main Functional Areas

- Authentication & Authorization
- Food Management
- Order & Cart
- Kitchen Management
- Payment Management
- Inventory Management
- Table Management

---

## Testing Tools & Technologies

| Category | Tools |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | ReactJS, Vite |
| Database | MySQL 8.x |
| Unit Testing | JUnit 5, Mockito |
| Integration Testing | Spring Boot Test, MockMvc |
| Automation | Selenium WebDriver, TestNG |
| Performance Testing | Apache JMeter |
| Code Coverage | JaCoCo |
| Code Quality | SonarQube / SonarCloud |
| Build Tool | Maven |
| Version Control | Git, GitHub |
| CI/CD | GitHub Actions |

fileciteturn1file1L39-L64 fileciteturn1file1L85-L126

---

## Manual Testing

A total of **194 Manual Test Cases** were executed:

- Login: **18**
- Admin: **59**
- Order & Cart: **39**
- Kitchen: **38**
- Payment & Table: **40**

### Result

- **PASS:** 165
- **FAIL:** 22
- **SKIPPED:** 6
- **BLOCKED:** 1

Testing covered positive, negative, validation, boundary, role-based and business-rule scenarios.

---

## Unit & Integration Testing

### Unit Testing

Unit tests were implemented for backend components such as:

- Controller
- Service
- Mapper
- Business logic
- Exception handling

JUnit 5 and Mockito were used for isolated component testing.

### Integration Testing

Integration testing focused on interactions between:

- Controller
- Service
- Repository
- Database
- Frontend & Backend
- Authentication & Authorization
- Order & Kitchen
- Order, Payment & Table

Spring Boot Test and MockMvc were used for API and integration verification.

---

## Automation Testing

Automation testing was implemented using:

- **Java**
- **Selenium WebDriver**
- **TestNG**
- **Maven**
- **Page Object Model**

### Automated Test Cases

| Test Case | Description |
|---|---|
| Login Test 1 | Valid login |
| Login Test 2 | Invalid login |
| Add to Cart | Add food to cart |
| Order | Create an order |

### Result

**4/4 Test Cases Passed — 100%**

The automation suite focuses on important and repeatable business flows and can be extended for regression testing. fileciteturn1file3L250-L259

---

## Code Coverage

Backend code coverage was measured using **JaCoCo**.

| Metric | Coverage |
|---|---:|
| Instruction | 92% |
| Branch | 72% |
| Line | 92% |
| Method | 95% |
| Class | 96% |

The results show high overall code coverage, while branch coverage remains an area for further improvement. fileciteturn1file3L225-L249

---

## Performance Testing

Performance testing was conducted using **Apache JMeter**.

### Configuration

- Concurrent Users: **10**
- Loop Count: **5**
- Total Samples: **100**

### Results

| Metric | Result |
|---|---:|
| Average Response Time | 13 ms |
| Minimum Response Time | 4 ms |
| Maximum Response Time | 29 ms |
| Error Rate | 0% |
| Throughput | 97.8 requests/sec |

The system performed stably under the tested load. Further testing with higher concurrent users is recommended to identify the system's scalability limits. fileciteturn1file3L260-L292

---

## Defect Management

Testing identified several issues, particularly in:

- Input validation
- API error handling
- Data consistency
- Synchronization between modules
- Concurrent data processing

Performance testing also identified a **Duplicate Key / Primary Key conflict** under concurrent requests, related to the current ID generation mechanism. fileciteturn1file2L140-L142

---

## Test Environment

- OS: Windows 11
- CPU: Intel Core i5
- RAM: 16 GB
- Storage: 1 TB SSD
- JDK: 21
- MySQL: 8.x
- Maven: 3.x
- Chrome
- Selenium: 4.35.0
- TestNG: 7.11.0
- Apache JMeter: 5.x fileciteturn1file2L144-L198

---

## Project Structure

```text
Project_FastFoodShop_Testing/
│
├── backend/
│   └── Spring Boot application
│
├── frontend/
│   └── React + Vite application
│
├── automation/
│   └── Selenium + TestNG automation tests
│
├── test-cases/
│   └── Manual test cases and test data
│
├── performance/
│   └── JMeter test plans
│
└── README.md
```

---

## Key Results

- **194** Manual Test Cases executed
- **165** Manual Test Cases passed
- **4/4** Automation Test Cases passed
- **92%** Instruction Coverage
- **92%** Line Coverage
- **95%** Method Coverage
- **96%** Class Coverage
- **13 ms** Average Response Time
- **0%** Performance Test Error Rate
- **97.8 requests/sec** Throughput

---

## Future Improvements

- Expand Selenium automation coverage to Admin, Kitchen and Payment modules.
- Increase Unit and Integration Test coverage.
- Improve Branch Coverage.
- Perform Performance Testing with higher concurrent users.
- Expand cross-browser testing.
- Improve defect tracking and reporting.
- Integrate automated testing into the CI/CD pipeline. fileciteturn1file5L356-L359

---

## Conclusion

This project demonstrates a practical Software Testing and Quality Assurance process covering **Manual Testing, Unit Testing, Integration Testing, Automation Testing, Performance Testing, Code Coverage and Defect Management**.

The testing activities helped identify functional defects, evaluate backend code coverage, validate automated business flows, and assess system performance under concurrent requests.

# Notes

This repository highlights my contributions in **Software Testing and Quality Assurance** for the FastFood Management System project. My work focused on designing automated tests, validating application functionality, evaluating performance, and integrating testing into the CI/CD workflow to improve software quality.

## Documentation

[[Software Quality Assurance Report](https://pearskyrich.github.io/Project_FastFood_Selenium/Project_FastFoodShop_Testing.pdf)]
[[Unit&IntergrationTest](https://pearskyrich.github.io/Project_FastFood_Selenium/FastFood_Test_Case.xlsx)]
[[E2ETestbySelenium](https://pearskyrich.github.io/Project_FastFood_Selenium/automation_test_report.xlsx)]

## Acknowledgements

The Fast Food Management System reviewed in this repository was developed by another project team.
This repository contains only the Software Quality Assurance (SQA) review conducted as part of the Software Quality Assurance course.
Original Project:
[Github](https://github.com/duy110405/Project_FastFoodShop)
