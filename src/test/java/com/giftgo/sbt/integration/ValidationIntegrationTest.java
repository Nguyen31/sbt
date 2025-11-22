package com.giftgo.sbt.integration;

import com.giftgo.sbt.model.Entry;
import com.giftgo.sbt.validator.ModelValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String BASE_URL = "/api/outcomes";

    @Test
    public void validOutcomes() throws Exception {
        var file = new MockMultipartFile("file",
                "EntryFile.txt",
                "text/plain",
                """
                        18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
                        3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                        1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides A Scooter|8.5|15.3
                        """.getBytes());

        mockMvc.perform(multipart(BASE_URL).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].transport").value("Rides A Bike"))
                .andExpect(jsonPath("$[0].topSpeed").value(12.1))
                .andExpect(jsonPath("$[2].name").value("Jenny Walters"))
                .andExpect(jsonPath("$[2].transport").value("Rides A Scooter"))
                .andExpect(jsonPath("$[2].topSpeed").value(15.3));
    }

    @Test
    public void invalidOutcomes() throws Exception {
        var file = new MockMultipartFile("file",
                "InvalidFile.txt",
                "text/plain",
                """
                        18148426-89e1-51ee-b9d1-0242ac120002|1Z1D14||||-6.2|-12.1
                        """.getBytes());

        mockMvc.perform(multipart(BASE_URL).file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Model validation failed for UUID: 18148426-89e1-51ee-b9d1-0242ac120002"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors", hasItem("name must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("likes must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("transport must not be blank")))
                .andExpect(jsonPath("$.errors", hasItem("avgSpeed must be >= 0")))
                .andExpect(jsonPath("$.errors", hasItem("topSpeed must be >= 0")))
                .andExpect(jsonPath("$.errors", hasItem("UUID must valid v4 or lower")))
                .andExpect(jsonPath("$.errors", hasItem("Must follow regex format [0-9]X[0-9]D[0-9][0-9]. e.g - 1X1D14")));
    }
}
