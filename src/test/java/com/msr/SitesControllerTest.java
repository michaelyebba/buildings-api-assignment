package com.msr;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test that uses MockMvc to test rest services
 *
 * TODO If we decide to remove "site" root element then we will need to update jsonPaths
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
                .andExpect(jsonPath("$..[?(@.id==1)]").exists())
                .andExpect(jsonPath("$..[?(@.id==1 && @.total_size==13000)]").exists())
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
                //.andExpect(jsonPath("$.").exists())
                .andExpect(jsonPath("$..[?(@.id==1)]").exists())
                .andExpect(jsonPath("$..[?(@.id==1 && @.total_size==13000)]").exists())
        ;
    }

    /**
     * Tests the controller's get list of sites by state search
     */
    @Test
    @SneakyThrows
    void testGetSitesByState() {
        mockMvc.perform(get("/sites/search?state=CA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)))
                //.andExpect(jsonPath("$..site").exists())
                .andExpect(jsonPath("$..[?(@.state=='CA')]", hasSize(5)))
        ;
    }

    /**
     * Use this JSON doc in swagger - TODO Consider removing "site" root element
     {
         "site": {
             "name": "New Site",
             "address": "123 fake st",
             "city": "Boston",
             "state": "MA",
             "zipcode": "02149"
        }
     }
     */
    @Test
    @SneakyThrows
    void testSaveSite_newSite() {
        String requestJson = "" +
                "{\n" +
                "         \"site\": {\n" +
                "             \"name\": \"New Site\",\n" +
                "             \"address\": \"123 fake st\",\n" +
                "             \"city\": \"Boston\",\n" +
                "             \"state\": \"MA\",\n" +
                "             \"zipcode\": \"02149\"\n" +
                "        }\n" +
                "     }";

        mockMvc.perform(post("/sites/").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson)).andExpect(status().isOk());

    }

    @Test
    @SneakyThrows
    void testSaveSite_existingSite() {
        String requestJson = "" +
                "{\n" +
                "         \"site\": {\n" +
                "             \"id\": \"1\",\n" +
                "             \"name\": \"MHQ - Modified\",\n" +
                "             \"address\": \"707 Broadway Suite 1000\",\n" +
                "             \"city\": \"San Diego\",\n" +
                "             \"state\": \"CA\",\n" +
                "             \"zipcode\": \"92121\"\n" +
                "        }\n" +
                "     }";

        mockMvc.perform(post("/sites/").contentType(MediaType.APPLICATION_JSON_VALUE).content(requestJson)).andExpect(status().isOk());

    }
}