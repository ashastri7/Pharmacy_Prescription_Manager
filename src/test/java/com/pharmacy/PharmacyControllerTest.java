package com.pharmacy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class PharmacyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test void homePage_loads() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
    }

    @Test void register_valid_success() throws Exception {
        mockMvc.perform(post("/register")
               .param("id", "RX100")
               .param("patientName", "Alice")
               .param("patientAge", "30")
               .param("weightKg", "70")
               .param("drugName", "Amoxicillin")
               .param("dosageMg", "250")
               .param("refills", "3"))
               .andExpect(status().isOk())
               .andExpect(view().name("result"))
               .andExpect(model().attribute("success", true));
    }

    @Test void register_invalid_emptyId_fails() throws Exception {
        mockMvc.perform(post("/register")
               .param("id", "")
               .param("patientName", "Alice")
               .param("patientAge", "30")
               .param("weightKg", "70")
               .param("drugName", "Amoxicillin")
               .param("dosageMg", "250")
               .param("refills", "3"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("success", false));
    }

    @Test void dosage_adult_valid() throws Exception {
        mockMvc.perform(post("/dosage")
               .param("age", "30")
               .param("weight", "60"))
               .andExpect(status().isOk())
               .andExpect(view().name("result"))
               .andExpect(model().attribute("success", true));
    }

    @Test void dosage_pediatric_valid() throws Exception {
        mockMvc.perform(post("/dosage")
               .param("age", "10")
               .param("weight", "30"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("success", true));
    }

    @Test void dosage_elderly_valid() throws Exception {
        mockMvc.perform(post("/dosage")
               .param("age", "70")
               .param("weight", "60"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("success", true));
    }

    @Test void dosage_invalidAge_fails() throws Exception {
        mockMvc.perform(post("/dosage")
               .param("age", "-1")
               .param("weight", "60"))
               .andExpect(model().attribute("success", false));
    }

    @Test void dispense_valid_success() throws Exception {
        mockMvc.perform(post("/register")
               .param("id", "RX300")
               .param("patientName", "Carol")
               .param("patientAge", "40")
               .param("weightKg", "65")
               .param("drugName", "Ibuprofen")
               .param("dosageMg", "200")
               .param("refills", "2"));

        mockMvc.perform(post("/dispense")
               .param("id", "RX300"))
               .andExpect(status().isOk())
               .andExpect(view().name("result"))
               .andExpect(model().attribute("success", true));
    }

    @Test void dispense_notFound_fails() throws Exception {
        mockMvc.perform(post("/dispense")
               .param("id", "NOTEXIST"))
               .andExpect(model().attribute("success", false));
    }

    @Test void cost_adult_valid() throws Exception {
        mockMvc.perform(post("/cost")
               .param("age", "30")
               .param("dosage", "100"))
               .andExpect(status().isOk())
               .andExpect(view().name("result"))
               .andExpect(model().attribute("success", true));
    }

    @Test void cost_pediatric_valid() throws Exception {
        mockMvc.perform(post("/cost")
               .param("age", "10")
               .param("dosage", "50"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("success", true));
    }

    @Test void cost_elderly_valid() throws Exception {
        mockMvc.perform(post("/cost")
               .param("age", "70")
               .param("dosage", "50"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("success", true));
    }

    @Test void cost_invalidAge_fails() throws Exception {
        mockMvc.perform(post("/cost")
               .param("age", "-5")
               .param("dosage", "100"))
               .andExpect(model().attribute("success", false));
    }

    @Test void validate_valid_success() throws Exception {
        mockMvc.perform(post("/register")
               .param("id", "RX500")
               .param("patientName", "Eve")
               .param("patientAge", "25")
               .param("weightKg", "60")
               .param("drugName", "Paracetamol")
               .param("dosageMg", "150")
               .param("refills", "2"));

        mockMvc.perform(post("/validate")
               .param("id", "RX500"))
               .andExpect(status().isOk())
               .andExpect(view().name("result"))
               .andExpect(model().attribute("success", true));
    }

    @Test void validate_notFound_fails() throws Exception {
        mockMvc.perform(post("/validate")
               .param("id", "GHOST"))
               .andExpect(model().attribute("success", false));
    }
    @Test void validate_invalid_zeroRefills() throws Exception {
    mockMvc.perform(post("/register")
           .param("id", "RX700")
           .param("patientName", "Test")
           .param("patientAge", "30")
           .param("weightKg", "70")
           .param("drugName", "Drug")
           .param("dosageMg", "100")
           .param("refills", "0"));

    mockMvc.perform(post("/validate")
           .param("id", "RX700"))
           .andExpect(status().isOk())
           .andExpect(model().attribute("success", false));
}
}