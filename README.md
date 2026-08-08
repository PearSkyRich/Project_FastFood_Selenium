# FastFood Management System – Software Testing

## Overview

A web-based **FastFood Management System** designed to support restaurant operations including food, order, kitchen, payment, inventory, and table management.

My main responsibility was **Software Testing & Quality Assurance**, including test case design, manual testing, automation testing, performance testing, code coverage analysis, and defect management. fileciteturn1file7L523-L559

## Tech Stack

- **Backend:** Java, Spring Boot
- **Frontend:** ReactJS, Vite
- **Database:** MySQL 8.x
- **Testing:** JUnit 5, Mockito, Spring Boot Test, MockMvc
- **Automation:** Selenium WebDriver, TestNG
- **Performance:** Apache JMeter
- **Code Coverage:** JaCoCo
- **Code Quality:** SonarQube / SonarCloud
- **CI/CD:** GitHub Actions
- **Build & Version Control:** Maven, Git, GitHub fileciteturn1file1L39-L64 fileciteturn1file1L85-L126

## Testing Scope

- Manual Functional Testing
- Unit Testing
- Integration Testing
- Automation Testing
- Performance Testing
- Code Coverage
- Defect Management

## Automation Testing

Implemented automated tests using **Selenium WebDriver + TestNG + Page Object Model**.

- Login
- Add to Cart
- Order

**Result: 4/4 Test Cases Passed (100%)** fileciteturn1file3L250-L259

## Performance Testing

Tested with **Apache JMeter**:

| Metric | Result |
|---|---:|
| Concurrent Users | 10 |
| Total Requests | 100 |
| Average Response Time | 13 ms |
| Maximum Response Time | 29 ms |
| Error Rate | 0% |
| Throughput | 97.8 req/s |

fileciteturn1file3L260-L292

## Code Coverage

| Metric | Coverage |
|---|---:|
| Instruction | 92% |
| Branch | 72% |
| Line | 92% |
| Method | 95% |
| Class | 96% |

fileciteturn1file3L225-L249

## Key Contributions

- Designed and executed manual test cases for major system modules.
- Developed automated test cases using Selenium and TestNG.
- Performed Unit and Integration Testing for Backend.
- Conducted Performance Testing using JMeter.
- Analyzed code coverage using JaCoCo.
- Identified and documented software defects.
- Evaluated system quality and proposed improvements.
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

## Documentation

[[Software Quality Assurance Report](https://pearskyrich.github.io/Project_FastFood_Selenium/Project_FastFoodShop_Testing.pdf)]
[[Unit&IntergrationTest](https://pearskyrich.github.io/Project_FastFood_Selenium/FastFood_Test_Case.xlsx)]
[[E2ETestbySelenium](https://pearskyrich.github.io/Project_FastFood_Selenium/automation_test_report.xlsx)]

## Acknowledgements

The Fast Food Management System reviewed in this repository was developed by another project team.
This repository contains only the Software Quality Assurance (SQA) review conducted as part of the Software Quality Assurance course.
Original Project:
[Github](https://github.com/duy110405/Project_FastFoodShop)
