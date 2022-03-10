package com.msr;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test that uses MockMvc to test rest services
 */
@SpringBootTest
@AutoConfigureMockMvc
class SitesControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    /**
     * Tests the get all sites controller
     */
    @Test
    @SneakyThrows
    void testGetAllSites() {
        mockMvc.perform(get("/sites/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$..site").exists())
                .andExpect(jsonPath("$..site.[?(@.id==1)]").exists())
                .andExpect(jsonPath("$..site.[?(@.id==1 && @.total_size==13000)]").exists())
        ;
    }

    /**
     * Tests the controller's get site by ID functionality.
     */
    @Test
    @SneakyThrows
    void testGetSiteById() {
        mockMvc.perform(get("/sites/site/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.site").exists())
                .andExpect(jsonPath("$.site.[?(@.id==1)]").exists())
                .andExpect(jsonPath("$.site.[?(@.id==1 && @.total_size==13000)]").exists())
        ;
    }

    /**
     * Tests the controller's get list of sites by state search
     */
    @Test
    @SneakyThrows
    void testGetSitesByState() {
        mockMvc.perform(get("/sites/?state=CA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$..site").exists())
                .andExpect(jsonPath("$..site.[?(@.state=='CA')]", hasSize(5)))
        ;
    }
}