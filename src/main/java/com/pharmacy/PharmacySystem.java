package com.pharmacy;

import java.util.HashMap;
import java.util.Map;

public class PharmacySystem {

private final Map<String, Prescription> prescriptions = new HashMap<>();

    // FUNCTION 1: Register a new prescription
    public void registerPrescription(String id, String patientName, int patientAge, double weightKg, String drugName, double dosageMg,
                                    int refills) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Prescription ID cannot be empty");
        if (patientName == null || patientName.trim().isEmpty())
            throw new IllegalArgumentException("Patient name cannot be empty");
        if (patientAge < 0 || patientAge > 120)
            throw new IllegalArgumentException("Patient age must be between 0 and 120");
        if (weightKg <= 0 || weightKg > 300)
            throw new IllegalArgumentException("Weight must be between 0 and 300 kg");
        if (drugName == null || drugName.trim().isEmpty())
            throw new IllegalArgumentException("Drug name cannot be empty");
        if (dosageMg <= 0)
            throw new IllegalArgumentException("Dosage must be greater than 0");
        if (refills < 0 || refills > 12)
            throw new IllegalArgumentException("Refills must be between 0 and 12");
        if (prescriptions.containsKey(id))
            throw new IllegalArgumentException("Prescription ID already exists");

        prescriptions.put(id, new Prescription(id, patientName, patientAge,
                                            weightKg, drugName, dosageMg, refills));
    }

    // FUNCTION 2: Calculate dosage based on weight
    // Rule: 5mg per kg for adults (18-64)
    //       3mg per kg for children (0-17)
    //       4mg per kg for elderly (65-120)
    // Min dose: 10mg, Max dose: 500mg
    public double calculateDosage(int patientAge, double weightKg) {
        if (patientAge < 0 || patientAge > 120)
            throw new IllegalArgumentException("Age must be between 0 and 120");
        if (weightKg <= 0 || weightKg > 300)
            throw new IllegalArgumentException("Weight must be between 0 and 300 kg");

        double mgPerKg;
        if (patientAge <= 17) {
            mgPerKg = 3.0;       // pediatric
        } else if (patientAge <= 64) {
            mgPerKg = 5.0;       // adult
        } else {
            mgPerKg = 4.0;       // elderly
        }

        double dosage = mgPerKg * weightKg;

        if (dosage < 10.0)  dosage = 10.0;   // minimum safe dose
        if (dosage > 500.0) dosage = 500.0;  // maximum safe dose

        return dosage;
    }

    // FUNCTION 3: Validate a prescription
    // Returns true only if all conditions are met
    public boolean validatePrescription(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Prescription ID cannot be empty");
        if (!prescriptions.containsKey(id))
            throw new IllegalArgumentException("Prescription not found: " + id);

        Prescription p = prescriptions.get(id);

        // Check refills available
        if (p.getRefillsRemaining() <= 0)
            return false;

        // Check dosage is within safe bounds
        if (p.getDosageMg() < 10.0 || p.getDosageMg() > 500.0)
            return false;

        // Check patient age is valid for dispensing
        if (p.getPatientAge() < 1) {
            return false;
        }

        return true;
    }

    // FUNCTION 4: Dispense (use one refill)
    public String dispenseMedication(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Prescription ID cannot be empty");
        if (!prescriptions.containsKey(id))
            throw new IllegalArgumentException("Prescription not found: " + id);

        if (!validatePrescription(id))
            throw new IllegalStateException("Prescription is not valid for dispensing");

        Prescription p = prescriptions.get(id);
        p.setRefillsRemaining(p.getRefillsRemaining() - 1);

        return "Dispensed " + p.getDosageMg() + "mg of " + p.getDrugName()
                + " to " + p.getPatientName()
                + ". Refills left: " + p.getRefillsRemaining();
    }

    
    // FUNCTION 5: Calculate prescription cost
    // Base cost: $20
    // + $0.50 per mg of dosage
    // Pediatric surcharge (age < 18): +$15
    // Elderly discount (age >= 65): -$10
    // Minimum total: $20
    public double calculateCost(int patientAge, double dosageMg) {
        if (patientAge < 0 || patientAge > 120)
            throw new IllegalArgumentException("Age must be between 0 and 120");
        if (dosageMg <= 0 || dosageMg > 500)
            throw new IllegalArgumentException("Dosage must be between 0 and 500 mg");

        double cost = 20.0 + (dosageMg * 0.50);

        if (patientAge < 18)       cost += 15.0;  // pediatric surcharge
        else if (patientAge >= 65) cost -= 10.0;  // elderly discount

        if (cost < 20.0) cost = 20.0;  // minimum cost floor

        return Math.round(cost * 100.0) / 100.0;
    }

    // Utility getter for tests
    public Prescription getPrescription(String id) {
        return prescriptions.get(id);
    }
}