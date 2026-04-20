# Data Model and Schema

## 1. Overview
The Pharmacy Prescription Manager utilizes a structured object-oriented data model to manage the lifecycle of patient prescriptions. The model is centered around the `Prescription` entity, managed by a central system controller using an in-memory key-value mapping.

## 2. Entities and Attributes
The primary data entity in the system is the **Prescription**.

### Entity: Prescription
This entity represents a medication order. It contains patient demographics, medication details, and status flags.

| Attribute | Data Type | Description | Constraints |
| :--- | :--- | :--- | :--- |
| `prescriptionId` | `String` | Unique identifier for the prescription. | Non-empty, unique |
| `patientName` | `String` | Full name of the patient. | Non-empty |
| `patientAge` | `Integer` | The age of the patient. | Range: 0–120 |
| `patientWeightKg` | `Double` | The weight of the patient in kilograms. | Range: > 0 to 300 |
| `drugName` | `String` | The name of the prescribed medication. | Non-empty |
| `dosageMg` | `Double` | The computed safe dose in milligrams. | Range: 10.0 to 500.0 |
| `refillsRemaining`| `Integer` | Number of times the RX can be filled. | Range: 0 to 12 |
| `paid` | `Boolean` | Indicates if the order has been settled. | Default: `false` |

## 3. Relationships
The system follows a central management pattern:

* **PharmacySystem ↔ Prescription (One-to-Many):** The `PharmacySystem` stores `Prescription` objects in a `HashMap<String, Prescription>`. One system instance manages many prescriptions.
* **PharmacyController ↔ PharmacySystem (One-to-One):** The controller holds a single instance of the system to process incoming web requests.

## 4. Schema Representation (JSON)
Following standard documentation styles (e.g., MongoDB), the logical schema for a prescription record is:

```json
{
  "prescriptionId": "RX100",
  "patientName": "Aneri Shastri",
  "patientAge": 28,
  "patientWeightKg": 65.0,
  "drugName": "Amoxicillin",
  "dosageMg": 250.0,
  "refillsRemaining": 3,
  "paid": false
}