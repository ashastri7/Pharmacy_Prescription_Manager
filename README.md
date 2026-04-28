# 💊 Pharmacy Prescription Manager

> A Java Spring Boot web application for managing patient prescriptions with full automated testing, CI/CD pipeline, and cloud deployment.

**Developer:** Aneri Shastri  
**Live App:** https://pharmacyprescriptionmanager-production.up.railway.app  
**GitHub:** https://github.com/ashastri7/Pharmacy_Prescription_Manager  
**Course:** Software Automation — Option 2 (CI/CD + Automated Testing)

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Application Functions](#application-functions)
5. [Data Model & Schema](#data-model-schema)
6. [How to Run Locally](#how-to-run-locally)
7. [Testing](#testing)
8. [Coverage Report](#coverage-report)
9. [CI/CD Pipeline](#cicd-pipeline)
10. [Deployment](#deployment)
11. [Version Control](#version-control)

---

## Project Overview

The Pharmacy Prescription Manager simulates a real pharmacy backend system. A pharmacist can:

- Register new patient prescriptions with full input validation
- Calculate safe medication dosage based on patient age and weight
- Validate whether a prescription is eligible for dispensing
- Dispense medication and automatically track refill counts
- Calculate prescription cost with age-based pricing rules

The project demonstrates full software testing practices including BVA, EPC, Statement Coverage, Branch Coverage, Condition Coverage, and Path Coverage — all automated via a CI/CD pipeline.

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 (LTS) | Core programming language |
| Spring Boot | 3.2.4 | Web framework |
| Thymeleaf | 3.x | HTML template engine |
| JUnit 5 | 5.10.0 | Unit testing framework |
| JaCoCo | 0.8.10 | Code coverage reporting |
| Maven | 3.9+ | Build and dependency management |
| GitHub Actions | — | CI/CD pipeline |
| Railway | — | Cloud deployment platform |

---

## Project Structure

```
Pharmacy_Prescription_Manager/
├── .github/
│   └── workflows/
│       └── ci.yml                      ← CI/CD pipeline
├── src/
│   ├── main/java/com/pharmacy/
│   │   ├── PharmacyApplication.java    ← Spring Boot entry point
│   │   ├── PharmacyController.java     ← Web layer (handles HTTP requests)
│   │   ├── PharmacySystem.java         ← Business logic (5 core functions)
│   │   └── Prescription.java          ← Data model entity
│   ├── test/java/com/pharmacy/
│   │   ├── PharmacySystemTest.java     ← Unit tests (82 tests)
│   │   └── PharmacyControllerTest.java ← Integration tests (16 tests)
│   └── resources/
│       ├── templates/
│       │   ├── index.html              ← Dashboard web page
│       │   └── result.html            ← Result display page
│       └── application.properties     ← App configuration
├── docs/
│   └── index.md                       ← Additional documentation
├── Procfile                           ← Railway deployment config
├── system.properties                  ← Java version for Railway
└── pom.xml                           ← Maven build configuration
```

---

## Application Functions

### Function 1: `registerPrescription()`

Registers a new prescription into the system after validating all inputs.

**Input Parameters:**

| Parameter | Type | Valid Range | Description |
|---|---|---|---|
| id | String | Not empty, unique | Prescription ID e.g. RX001 |
| patientName | String | Not empty | Patient full name |
| patientAge | int | 0 – 120 | Age in years |
| weightKg | double | 0 – 300 | Weight in kilograms |
| drugName | String | Not empty | Medication name |
| dosageMg | double | > 0 | Dosage in milligrams |
| refills | int | 0 – 12 | Number of refills allowed |

**Output:** void (stores Prescription object in HashMap)  
**Throws:** `IllegalArgumentException` for any invalid input

---

### Function 2: `calculateDosage(age, weightKg)`

Calculates the recommended safe dosage based on patient age category and body weight.

**Dosage Rules:**

| Age Category | Age Range | mg per kg | Example (70kg) |
|---|---|---|---|
| Pediatric | 0 – 17 | 3.0 mg/kg | 210 mg |
| Adult | 18 – 64 | 5.0 mg/kg | 350 mg |
| Elderly | 65 – 120 | 4.0 mg/kg | 280 mg |

**Safety Clamps:** Minimum 10mg | Maximum 500mg

**Input:** `int age` (0–120), `double weightKg` (0–300)  
**Output:** `double` — recommended dosage in mg  
**Throws:** `IllegalArgumentException` for invalid age or weight

---

### Function 3: `validatePrescription(id)`

Checks whether a prescription is valid and eligible for dispensing.

**Validation Rules:**
- `refillsRemaining` must be > 0
- `dosageMg` must be between 10.0 and 500.0
- `patientAge` must be >= 1

**Input:** `String id` — prescription identifier  
**Output:** `boolean` — `true` if valid, `false` if invalid  
**Throws:** `IllegalArgumentException` if ID not found

---

### Function 4: `dispenseMedication(id)`

Dispenses medication for a valid prescription and reduces refill count by 1.

**Process:**
1. Validates ID exists
2. Calls `validatePrescription()` — throws if invalid
3. Reduces `refillsRemaining` by 1
4. Returns confirmation message

**Input:** `String id` — prescription identifier  
**Output:** `String` — e.g. `"Dispensed 250.0mg of Amoxicillin to Alice Smith. Refills left: 2"`  
**Throws:** `IllegalStateException` if prescription fails validation

---

### Function 5: `calculateCost(age, dosageMg)`

Calculates the total prescription cost with age-based pricing adjustments.

**Pricing Formula:**

```
Base cost    = $20.00 + ($0.50 × dosageMg)
Pediatric    = base + $15.00  (age < 18)
Elderly      = base - $10.00  (age >= 65)
Adult        = base           (age 18–64)
Minimum cost = $20.00
```

**Examples:**

| Patient | Age | Dosage | Calculation | Total |
|---|---|---|---|---|
| Adult | 30 | 100mg | $20 + $50 | $70.00 |
| Pediatric | 10 | 50mg | $20 + $25 + $15 | $60.00 |
| Elderly | 70 | 50mg | $20 + $25 - $10 | $35.00 |

**Input:** `int age` (0–120), `double dosageMg` (0–500)  
**Output:** `double` — cost in USD  
**Throws:** `IllegalArgumentException` for invalid inputs

---

## Data Model & Schema

### Entity: Prescription

Represents a single patient's medication prescription record.

| Attribute | Type | Constraints | Description |
|---|---|---|---|
| prescriptionId | String | Required, Unique, Not empty | Unique ID e.g. RX001 |
| patientName | String | Required, Not empty | Patient full name |
| patientAge | int | 0 – 120 | Age in years |
| patientWeightKg | double | 0 – 300 (exclusive) | Weight in kilograms |
| drugName | String | Required, Not empty | Prescribed medication |
| dosageMg | double | 10 – 500 | Dosage in milligrams |
| refillsRemaining | int | 0 – 12 | Refills left |
| paid | boolean | true / false | Payment status |

### Internal Storage

```
HashMap<String, Prescription>
  Key   → prescriptionId (String, unique)
  Value → Prescription object
```

Similar to a MongoDB document:

```json
{
  "prescriptionId":   "RX001",
  "patientName":      "Alice Smith",
  "patientAge":       30,
  "patientWeightKg":  70.0,
  "drugName":         "Amoxicillin",
  "dosageMg":         250.0,
  "refillsRemaining": 3,
  "paid":             false
}
```

### Relationships

```
Patient (1) ──────────→ (many) Prescriptions
One patient can have multiple prescriptions,
each uniquely identified by prescriptionId.
```

### API Endpoint Schema

| Endpoint | Input | Output |
|---|---|---|
| `POST /register` | id, name, age, weight, drug, dosage, refills | success boolean + message |
| `POST /dosage` | age, weight | dosage in mg (double) |
| `POST /validate` | id | true/false (boolean) |
| `POST /dispense` | id | confirmation string |
| `POST /cost` | age, dosage | cost in USD (double) |
| `GET /` | — | index.html dashboard |

### Validation Rules Summary

| Field | Valid Range | Error Message |
|---|---|---|
| prescriptionId | Non-empty, unique | "Prescription ID cannot be empty" |
| patientName | Non-empty | "Patient name cannot be empty" |
| patientAge | 0 to 120 | "Patient age must be between 0 and 120" |
| patientWeightKg | 0 to 300 (exclusive) | "Weight must be between 0 and 300 kg" |
| drugName | Non-empty | "Drug name cannot be empty" |
| dosageMg | Greater than 0 | "Dosage must be greater than 0" |
| refillsRemaining | 0 to 12 | "Refills must be between 0 and 12" |

---

## How to Run Locally

### Prerequisites
- Java 21
- Maven 3.9+

### Steps

```bash
# Clone the repository
git clone https://github.com/ashastri7/Pharmacy_Prescription_Manager.git
cd Pharmacy_Prescription_Manager

# Run the application
mvn spring-boot:run

# Open in browser
open http://localhost:8080
```

---

## Testing

### Testing Techniques Used

#### Blackbox Testing

**1. BVA — Boundary Value Analysis**

Tests values at the exact edges of every valid range. Bugs most commonly occur at boundaries.

Example for `calculateDosage(age)`:

| Test ID | Input (Age) | Expected | Boundary Reason |
|---|---|---|---|
| BVA-01 | -1 | Exception | Just below minimum |
| BVA-02 | 0 | 10mg (clamped) | Minimum boundary |
| BVA-03 | 17 | 3mg/kg × weight | Last pediatric age |
| BVA-04 | 18 | 5mg/kg × weight | First adult age ← KEY BOUNDARY |
| BVA-05 | 64 | 5mg/kg × weight | Last adult age |
| BVA-06 | 65 | 4mg/kg × weight | First elderly age ← KEY BOUNDARY |
| BVA-07 | 120 | 4mg/kg × weight | Maximum boundary |
| BVA-08 | 121 | Exception | Just above maximum |

**2. EPC — Equivalence Partitioning Classes**

Divides all inputs into groups where every value in the group behaves identically. Tests one value per group.

Example for `calculateDosage(age)`:

| Partition | Range | Test Value | Expected |
|---|---|---|---|
| Invalid below | age < 0 | -5 | Exception |
| Pediatric | 0 – 17 | 10 | 3mg/kg |
| Adult | 18 – 64 | 30 | 5mg/kg |
| Elderly | 65 – 120 | 70 | 4mg/kg |
| Invalid above | age > 120 | 130 | Exception |

#### Whitebox Testing

**3. Statement Coverage (96%)** — Every line of code executed at least once during tests.

**4. Branch Coverage (91%)** — Every `if/else` decision tested from both YES and NO sides.

**5. Condition Coverage** — Every boolean sub-expression in compound conditions tested as both true and false.

Example: `if (age < 0 || age > 120)` requires:
- `age < 0` = true (age = -1)
- `age < 0` = false, `age > 120` = true (age = 121)
- Both = false (age = 30)

**6. Path Coverage** — Every possible route from START to END through a function, defined using CFG diagrams.

#### Integration Testing

**7. MockMvc Controller Tests** — Simulates HTTP requests without a real browser. Tests the complete web request → Controller → PharmacySystem → response flow.

---

### Test Results Summary

```
Tests run: 98, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

| Test File | Tests | Result |
|---|---|---|
| PharmacySystemTest.java | 82 | ✅ All Pass |
| PharmacyControllerTest.java | 16 | ✅ All Pass |
| **Total** | **98** | **✅ All Pass** |

### Test Breakdown by Function

| Function | BVA | EPC | Other | Total |
|---|---|---|---|---|
| registerPrescription | 8 | 4 | 4 | 16 |
| calculateDosage | 17 | 5 | 5 | 27 |
| validatePrescription | 4 | 3 | 1 | 8 |
| dispenseMedication | 5 | 2 | 3 | 10 |
| calculateCost | 11 | 3 | 0 | 14 |
| PharmacyController | — | — | 16 | 16 |
| **Total** | **45** | **17** | **29** | **98** |

### How to Run Tests

```bash
mvn test
```

---

## Coverage Report

### How to View

```bash
mvn test
open target/site/jacoco/index.html
```

### Results

| File | Instruction Coverage | Branch Coverage |
|---|---|---|
| PharmacySystem.java | 96% | 91% |
| PharmacyController.java | 85%+ | 80%+ |
| Prescription.java | 77% | n/a |
| **Overall** | **~96%** | **~91%** |

> Note: The remaining uncovered code is primarily the Spring Boot `main()` method in `PharmacyApplication.java`. This is a framework entry point that cannot be invoked during unit tests — standard practice in all Spring Boot projects.

---

## CI/CD Pipeline

Every `git push` to `main` automatically triggers the following pipeline defined in `.github/workflows/ci.yml`:

```
git push → GitHub Actions triggered
              ↓
        Checkout code
              ↓
        Setup Java 21
              ↓
        mvn compile (syntax check)
              ↓
        mvn test (run all 98 tests)
              ↓
        Generate JaCoCo coverage report
              ↓
        Upload coverage artifact
              ↓
        Railway deploys new version live
```

### Pipeline File: `.github/workflows/ci.yml`

```yaml
name: Pharmacy CI Pipeline

on:
  push:
    branches: [ main ]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
      - run: mvn --batch-mode compile
      - run: mvn --batch-mode test
      - uses: actions/upload-artifact@v4
        with:
          name: jacoco-coverage-report
          path: target/site/jacoco/
```

**Result:** Green checkmark ✅ on every successful push. Red X ❌ if any test fails — nothing gets deployed.

---

## Deployment

The application is deployed on **Railway** cloud platform.

- **Live URL:** https://pharmacyprescriptionmanager-production.up.railway.app
- **Platform:** Railway (free tier)
- **Trigger:** Automatic on successful CI/CD pipeline run
- **Config files:** `Procfile`, `system.properties`

### Deployment Config

**Procfile:**
```
web: java -jar target/pharmacy-prescription-manager-1.0.0.jar
```

**system.properties:**
```
java.runtime.version=21
```

---

## Version Control

All changes tracked via Git and GitHub. Full commit history available at the repository.

| Commit | Description |
|---|---|
| Initial setup | Maven project, folder structure, pom.xml |
| Core functions | 5 Java functions in PharmacySystem.java |
| Unit tests | BVA, EPC, whitebox coverage tests |
| Spring Boot web app | Controller, HTML templates, routing |
| CI/CD pipeline | GitHub Actions workflow |
| Cloud deployment | Railway deployment configuration |
| Controller tests | MockMvc integration tests |
| Final submission | 98 tests passing, 96% coverage |


## Mutation Testing

Mutation testing is performed using **PITest** to verify the strength 
of the test suite.

### What is Mutation Testing?
PITest automatically introduces small bugs (mutations) into the source 
code and verifies that the existing test suite detects them.

### Mutation Types Used
| Mutator | Description |
|---|---|
| CONDITIONALS_BOUNDARY | Changes < to <=, > to >= |
| NEGATE_CONDITIONALS | Flips boolean conditions |
| MATH | Changes arithmetic operators |
| RETURN_VALS | Changes return values |

### How to Run
```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
open target/pit-reports/index.html
```

