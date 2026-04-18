package com.pharmacy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PharmacySystemTest {

    PharmacySystem pharmacy;

    @BeforeEach
    void setUp() {
        pharmacy = new PharmacySystem();
    }

    // Helper to register a default valid prescription
    void registerDefault(String id) {
        pharmacy.registerPrescription(id, "Alice Smith", 30, 70.0,
                "Amoxicillin", 250.0, 3);
    }

    // ══════════════════════════════════════════════
    // FUNCTION 1: registerPrescription — BVA + EPC
    // ══════════════════════════════════════════════

    @Test void reg_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription(null, "Alice", 30, 70, "Drug", 100, 2));
    }
    @Test void reg_emptyId_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("", "Alice", 30, 70, "Drug", 100, 2));
    }
    @Test void reg_nullName_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", null, 30, 70, "Drug", 100, 2));
    }
    @Test void reg_ageMinus1_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", -1, 70, "Drug", 100, 2));
    }
    @Test void reg_age0_valid() {
        assertDoesNotThrow(() ->
            pharmacy.registerPrescription("P001", "Alice", 0, 10, "Drug", 100, 2));
    }
    @Test void reg_age120_valid() {
        assertDoesNotThrow(() ->
            pharmacy.registerPrescription("P001", "Alice", 120, 50, "Drug", 100, 2));
    }
    @Test void reg_age121_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", 121, 70, "Drug", 100, 2));
    }
    @Test void reg_weight0_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", 30, 0, "Drug", 100, 2));
    }
    @Test void reg_weight300_valid() {
        assertDoesNotThrow(() ->
            pharmacy.registerPrescription("P001", "Alice", 30, 300, "Drug", 100, 2));
    }
    @Test void reg_weight301_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", 30, 301, "Drug", 100, 2));
    }
    @Test void reg_refillsMinus1_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, -1));
    }
    @Test void reg_refills0_valid() {
        assertDoesNotThrow(() ->
            pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 0));
    }
    @Test void reg_refills12_valid() {
        assertDoesNotThrow(() ->
            pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 12));
    }
    @Test void reg_refills13_throws() {
        assertThrows(IllegalArgumentException.class, () ->
            pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 13));
    }
    @Test void reg_duplicateId_throws() {
        registerDefault("P001");
        assertThrows(IllegalArgumentException.class, () -> registerDefault("P001"));
    }
    @Test void reg_valid_storesCorrectly() {
        registerDefault("P001");
        Prescription p = pharmacy.getPrescription("P001");
        assertNotNull(p);
        assertEquals("Alice Smith", p.getPatientName());
        assertEquals(3, p.getRefillsRemaining());
    }

    // ══════════════════════════════════════════════
    // FUNCTION 2: calculateDosage — BVA
    // ══════════════════════════════════════════════

    @Test void dosage_ageMinus1_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateDosage(-1, 60));
    }
    @Test void dosage_age0_clampedToMin() {
        assertEquals(10.0, pharmacy.calculateDosage(0, 1));
    }
    @Test void dosage_age17_pediatric() {
        assertEquals(180.0, pharmacy.calculateDosage(17, 60));
    }
    @Test void dosage_age18_adult() {
        assertEquals(300.0, pharmacy.calculateDosage(18, 60));
    }
    @Test void dosage_age64_adult() {
        assertEquals(300.0, pharmacy.calculateDosage(64, 60));
    }
    @Test void dosage_age65_elderly() {
        assertEquals(240.0, pharmacy.calculateDosage(65, 60));
    }
    @Test void dosage_age120_valid() {
        assertEquals(240.0, pharmacy.calculateDosage(120, 60));
    }
    @Test void dosage_age121_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateDosage(121, 60));
    }
    @Test void dosage_weight0_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateDosage(30, 0));
    }
    @Test void dosage_weight1_clampedToMin() {
        assertEquals(10.0, pharmacy.calculateDosage(30, 1));
    }
    @Test void dosage_weight2_exactlyMin() {
        assertEquals(10.0, pharmacy.calculateDosage(30, 2));
    }
    @Test void dosage_weight3_aboveMin() {
        assertEquals(15.0, pharmacy.calculateDosage(30, 3));
    }
    @Test void dosage_weight100_maxDose() {
        assertEquals(500.0, pharmacy.calculateDosage(30, 100));
    }
    @Test void dosage_weight101_clamped() {
        assertEquals(500.0, pharmacy.calculateDosage(30, 101));
    }
    @Test void dosage_weight300_valid() {
        assertEquals(500.0, pharmacy.calculateDosage(30, 300));
    }
    @Test void dosage_weight301_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateDosage(30, 301));
    }

    // ══════════════════════════════════════════════
    // FUNCTION 2: calculateDosage — EPC
    // ══════════════════════════════════════════════

    @Test void dosage_epc_pediatric() {
        assertEquals(150.0, pharmacy.calculateDosage(10, 50));
    }
    @Test void dosage_epc_adult() {
        assertEquals(300.0, pharmacy.calculateDosage(30, 60));
    }
    @Test void dosage_epc_elderly() {
        assertEquals(240.0, pharmacy.calculateDosage(70, 60));
    }
    @Test void dosage_epc_clampedLow() {
        assertEquals(10.0, pharmacy.calculateDosage(30, 1));
    }
    @Test void dosage_epc_clampedHigh() {
        assertEquals(500.0, pharmacy.calculateDosage(30, 200));
    }

    // ══════════════════════════════════════════════
    // FUNCTION 3: validatePrescription — BVA + EPC
    // ══════════════════════════════════════════════

    @Test void validate_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.validatePrescription(null));
    }
    @Test void validate_notFound_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.validatePrescription("GHOST"));
    }
    @Test void validate_valid_returnsTrue() {
        registerDefault("P001");
        assertTrue(pharmacy.validatePrescription("P001"));
    }
    @Test void validate_zeroRefills_returnsFalse() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 0);
        assertFalse(pharmacy.validatePrescription("P001"));
    }
    @Test void validate_dosageTooLow_returnsFalse() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 5.0, 2);
        assertFalse(pharmacy.validatePrescription("P001"));
    }
    @Test void validate_dosageTooHigh_returnsFalse() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 600.0, 2);
        assertFalse(pharmacy.validatePrescription("P001"));
    }
    @Test void validate_age0_returnsFalse() {
        pharmacy.registerPrescription("P001", "Alice", 0, 10, "Drug", 100, 2);
        assertFalse(pharmacy.validatePrescription("P001"));
    }
    @Test void validate_age1_returnsTrue() {
        pharmacy.registerPrescription("P001", "Alice", 1, 10, "Drug", 100, 2);
        assertTrue(pharmacy.validatePrescription("P001"));
    }

    // ══════════════════════════════════════════════
    // FUNCTION 4: dispenseMedication — BVA + EPC
    // ══════════════════════════════════════════════

    @Test void dispense_nullId_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.dispenseMedication(null));
    }
    @Test void dispense_notFound_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.dispenseMedication("GHOST"));
    }
    @Test void dispense_valid_decreasesRefills() {
        registerDefault("P001");
        pharmacy.dispenseMedication("P001");
        assertEquals(2, pharmacy.getPrescription("P001").getRefillsRemaining());
    }
    @Test void dispense_valid_returnsMessage() {
        registerDefault("P001");
        String result = pharmacy.dispenseMedication("P001");
        assertTrue(result.contains("Alice Smith"));
        assertTrue(result.contains("Amoxicillin"));
    }
    @Test void dispense_noRefills_throws() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 0);
        assertThrows(IllegalStateException.class, () -> pharmacy.dispenseMedication("P001"));
    }
    @Test void dispense_lastRefill_leavesZero() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 1);
        pharmacy.dispenseMedication("P001");
        assertEquals(0, pharmacy.getPrescription("P001").getRefillsRemaining());
    }
    @Test void dispense_afterLastRefill_throws() {
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 100, 1);
        pharmacy.dispenseMedication("P001");
        assertThrows(IllegalStateException.class, () -> pharmacy.dispenseMedication("P001"));
    }

    // ══════════════════════════════════════════════
    // FUNCTION 5: calculateCost — BVA
    // ══════════════════════════════════════════════

    @Test void cost_dosageMinus1_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateCost(30, -1));
    }
    @Test void cost_dosage0_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateCost(30, 0));
    }
    @Test void cost_dosage1_adult() {
        assertEquals(20.50, pharmacy.calculateCost(30, 1));
    }
    @Test void cost_dosage500_adult() {
        assertEquals(270.00, pharmacy.calculateCost(30, 500));
    }
    @Test void cost_dosage501_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateCost(30, 501));
    }
    @Test void cost_age17_pediatricSurcharge() {
        assertEquals(85.00, pharmacy.calculateCost(17, 100));
    }
    @Test void cost_age18_noSurcharge() {
        assertEquals(70.00, pharmacy.calculateCost(18, 100));
    }
    @Test void cost_age64_noDiscount() {
        assertEquals(70.00, pharmacy.calculateCost(64, 100));
    }
    @Test void cost_age65_elderlyDiscount() {
        assertEquals(60.00, pharmacy.calculateCost(65, 100));
    }
    @Test void cost_ageMinus1_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateCost(-1, 100));
    }
    @Test void cost_age121_throws() {
        assertThrows(IllegalArgumentException.class, () -> pharmacy.calculateCost(121, 100));
    }

    // ══════════════════════════════════════════════
    // FUNCTION 5: calculateCost — EPC
    // ══════════════════════════════════════════════

    @Test void cost_epc_pediatric() {
        assertEquals(60.00, pharmacy.calculateCost(10, 500));
    }
    @Test void cost_epc_adult() {
        assertEquals(45.00, pharmacy.calculateCost(40, 50));
    }
    @Test void cost_epc_elderly() {
        assertEquals(35.00, pharmacy.calculateCost(70, 50));
    }

    // ══════════════════════════════════════════════
    // DATAFLOW TESTS
    // ══════════════════════════════════════════════

    @Test void df_dosage_mgPerKg_definedAndUsed_pediatric() {
        // mgPerKg defined as 3.0, flows to dosage = 3.0 * weight
        assertEquals(120.0, pharmacy.calculateDosage(10, 40));
    }
    @Test void df_dosage_mgPerKg_definedAndUsed_adult() {
        // mgPerKg defined as 5.0, flows to dosage = 5.0 * weight
        assertEquals(200.0, pharmacy.calculateDosage(30, 40));
    }
    @Test void df_dosage_mgPerKg_definedAndUsed_elderly() {
        // mgPerKg defined as 4.0, flows to dosage = 4.0 * weight
        assertEquals(160.0, pharmacy.calculateDosage(70, 40));
    }
    @Test void df_dosage_clampMin_defToReturn() {
        // dosage defined, clamped to 10.0, flows to return
        assertEquals(10.0, pharmacy.calculateDosage(30, 1));
    }
    @Test void df_dosage_clampMax_defToReturn() {
        // dosage defined, clamped to 500.0, flows to return
        assertEquals(500.0, pharmacy.calculateDosage(30, 200));
    }
    @Test void df_cost_costVar_flowsThroughSurcharge() {
        // cost defined as base, flows through pediatric surcharge, flows to return
        double result = pharmacy.calculateCost(10, 20);
        assertEquals(45.0, result); // 20 + 10 + 15 surcharge
    }
    @Test void df_cost_costVar_flowsThroughDiscount() {
        // cost defined as base, flows through elderly discount, flows to return
        double result = pharmacy.calculateCost(70, 20);
        assertEquals(20.0, result); // 20 + 10 - 10 discount
    }
    @Test void df_dispense_refillsUpdated_flowsToGetter() {
        // refillsRemaining defined in Prescription, updated in dispense, read by getter
        registerDefault("P001");
        pharmacy.dispenseMedication("P001");
        pharmacy.dispenseMedication("P001");
        assertEquals(1, pharmacy.getPrescription("P001").getRefillsRemaining());
    }

    // ══════════════════════════════════════════════
// PRESCRIPTION GETTER COVERAGE TESTS
// ══════════════════════════════════════════════

@Test void prescription_getId() {
    registerDefault("P001");
    assertEquals("P001", pharmacy.getPrescription("P001").getPrescriptionId());
}

@Test void prescription_getWeight() {
    registerDefault("P001");
    assertEquals(70.0, pharmacy.getPrescription("P001").getPatientWeightKg());
}

@Test void prescription_isPaid_defaultFalse() {
    registerDefault("P001");
    assertFalse(pharmacy.getPrescription("P001").isPaid());
}

@Test void prescription_setPaid_true() {
    registerDefault("P001");
    pharmacy.getPrescription("P001").setPaid(true);
    assertTrue(pharmacy.getPrescription("P001").isPaid());
}
// ══════════════════════════════════════════════
// MISSING BRANCH COVERAGE FIXES
// ══════════════════════════════════════════════

@Test void reg_nullDrugName_throws() {
    assertThrows(IllegalArgumentException.class, () ->
        pharmacy.registerPrescription("P001", "Alice", 30, 70, null, 100, 2));
}

@Test void reg_emptyDrugName_throws() {
    assertThrows(IllegalArgumentException.class, () ->
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "", 100, 2));
}

@Test void reg_dosageZero_throws() {
    assertThrows(IllegalArgumentException.class, () ->
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", 0, 2));
}

@Test void reg_dosageNegative_throws() {
    assertThrows(IllegalArgumentException.class, () ->
        pharmacy.registerPrescription("P001", "Alice", 30, 70, "Drug", -5, 2));
}
}