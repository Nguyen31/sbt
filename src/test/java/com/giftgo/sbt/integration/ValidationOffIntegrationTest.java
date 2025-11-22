package com.giftgo.sbt.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(value = "validation-off")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationOffIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/api/outcomes";

    @Test
    public void invalidOutcomesIsValid() throws Exception {
        var file = new MockMultipartFile("file",
                "InvalidFile.txt",
                "text/plain",
                """
                        18148426-89e1-51ee-b9d1-0242ac120002|1Z1D14||||-6.2|-12.1
                        """.getBytes());

        mockMvc.perform(multipart(BASE_URL).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(""))
                .andExpect(jsonPath("$[0].transport").value(""))
                .andExpect(jsonPath("$[0].topSpeed").value(-12.1));
    }
}
