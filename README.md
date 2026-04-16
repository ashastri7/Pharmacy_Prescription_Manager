# Pharmacy Prescription Manager

A Java application that manages pharmacy prescriptions with full automated testing.

## Functions

| Function | Input | Output | Purpose |
|---|---|---|---|
| registerPrescription(...) | id, name, age, weight, drug, dosage, refills | void | Register new prescription |
| calculateDosage(age, weight) | int age, double weight | double (mg) | Compute safe dosage |
| validatePrescription(id) | String id | boolean | Check if valid to dispense |
| dispenseMedication(id) | String id | String message | Dispense and reduce refill count |
| calculateCost(age, dosage) | int age, double dosage | double ($) | Compute prescription cost |

## Data Model

- **Input:** patient age (int, 0–120), weight (double, 0–300kg), dosage (double, 0–500mg), refills (int, 0–12)
- **Output:** dosage in mg (double), cost in USD (double), pass/fail validation (boolean)
- **Internal structure:** `HashMap<String, Prescription>` storing all registered prescriptions

## Dosage Rules
- Pediatric (age 0–17): 3mg/kg
- Adult (age 18–64): 5mg/kg
- Elderly (age 65–120): 4mg/kg
- Minimum dose: 10mg | Maximum dose: 500mg

## Cost Rules
- Base: $20.00 + $0.50 per mg
- Pediatric surcharge (age < 18): +$15.00
- Elderly discount (age ≥ 65): -$10.00

## How to Run Tests
```bash
mvn test
```

## How to View Coverage Report
```bash
mvn test
open target/site/jacoco/index.html
```