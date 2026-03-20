package com.crewmeister.cmcodingchallenge.controller.integTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrencies_returnsOkOrNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/currencies")).andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200, "Expected 200 OK, but got: " + status);
    }
}

