package com.pharmacy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PharmacyController {

    private final PharmacySystem pharmacy = new PharmacySystem();

    // Show home page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Register a prescription
    @PostMapping("/register")
    public String register(
            @RequestParam String id,
            @RequestParam String patientName,
            @RequestParam int patientAge,
            @RequestParam double weightKg,
            @RequestParam String drugName,
            @RequestParam double dosageMg,
            @RequestParam int refills,
            Model model) {
        try {
            pharmacy.registerPrescription(id, patientName, patientAge,
                    weightKg, drugName, dosageMg, refills);
            model.addAttribute("action", "Register Prescription");
            model.addAttribute("success", true);
            model.addAttribute("message",
                "Prescription registered successfully for " + patientName);
            model.addAttribute("details", new String[]{
                "Prescription ID: " + id,
                "Patient: " + patientName,
                "Age: " + patientAge,
                "Weight: " + weightKg + " kg",
                "Drug: " + drugName,
                "Dosage: " + dosageMg + " mg",
                "Refills: " + refills
            });
        } catch (Exception e) {
            model.addAttribute("action", "Register Prescription");
            model.addAttribute("success", false);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }

    // Calculate dosage
    @PostMapping("/dosage")
    public String dosage(
            @RequestParam int age,
            @RequestParam double weight,
            Model model) {
        try {
            double dosage = pharmacy.calculateDosage(age, weight);
            String category = age <= 17 ? "Pediatric" : age <= 64 ? "Adult" : "Elderly";
            model.addAttribute("action", "Calculate Dosage");
            model.addAttribute("success", true);
            model.addAttribute("message",
                "Recommended dosage: " + dosage + " mg");
            model.addAttribute("details", new String[]{
                "Patient age: " + age + " (" + category + ")",
                
                "Patient weight: " + weight + " kg",
                "Calculated dosage: " + dosage + " mg"
            });
        } catch (Exception e) {
            model.addAttribute("action", "Calculate Dosage");
            model.addAttribute("success", false);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }

    // Dispense medication
    @PostMapping("/dispense")
    public String dispense(
            @RequestParam String id,
            Model model) {
        try {
            String result = pharmacy.dispenseMedication(id);
            model.addAttribute("action", "Dispense Medication");
            model.addAttribute("success", true);
            model.addAttribute("message", result);
            model.addAttribute("details", new String[]{
                "Prescription ID: " + id,
                "Status: Dispensed successfully",
                "Refills remaining: " +
                    pharmacy.getPrescription(id).getRefillsRemaining()
            });
        } catch (Exception e) {
            model.addAttribute("action", "Dispense Medication");
            model.addAttribute("success", false);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }

    // Calculate cost
    @PostMapping("/cost")
    public String cost(
            @RequestParam int age,
            @RequestParam double dosage,
            Model model) {
        try {
            double cost = pharmacy.calculateCost(age, dosage);
            String category = age < 18 ? "Pediatric (+$15 surcharge)"
                            : age >= 65 ? "Elderly (-$10 discount)"
                            : "Adult (standard rate)";
            model.addAttribute("action", "Calculate Cost");
            model.addAttribute("success", true);
            model.addAttribute("message",
                "Total prescription cost: $" + cost);
            model.addAttribute("details", new String[]{
                "Patient age: " + age,
                "Dosage: " + dosage + " mg",
                "Pricing category: " + category,
                "Base cost: $" + String.format("%.2f", 20.0 + dosage * 0.50),
                "Final cost: $" + cost
            });
        } catch (Exception e) {
            model.addAttribute("action", "Calculate Cost");
            model.addAttribute("success", false);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }

    // Validate prescription
    @PostMapping("/validate")
    public String validate(
            @RequestParam String id,
            Model model) {
        try {
            boolean valid = pharmacy.validatePrescription(id);
            model.addAttribute("action", "Validate Prescription");
            model.addAttribute("success", valid);
            model.addAttribute("message",
                valid ? "Prescription " + id + " is VALID for dispensing"
                      : "Prescription " + id + " is INVALID");
            model.addAttribute("details", new String[]{
                "Prescription ID: " + id,
                "Valid: " + valid
            });
        } catch (Exception e) {
            model.addAttribute("action", "Validate Prescription");
            model.addAttribute("success", false);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "result";
    }
}