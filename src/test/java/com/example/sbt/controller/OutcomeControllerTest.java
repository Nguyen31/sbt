package com.example.sbt.controller;

import com.example.sbt.model.Entry;
import com.example.sbt.model.Outcome;
import com.example.sbt.service.EntryService;
import com.example.sbt.service.OutcomeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OutcomeController.class)
public class OutcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EntryService entryService;

    @MockitoBean
    private OutcomeService outcomeService;

    private String BASE_URL = "/api/outcomes";

    @Test
    public void createOutcomes() throws Exception {
        final var mockFile = new MockMultipartFile(
                "file",
                "Input.txt",
                "text/plain",
                "Mock data".getBytes()
        );


        var entries = List.of(
                new Entry("18148426-89e1-11ee-b9d1-0242ac120002", "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", 6.2, 12.1),
                new Entry("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7", "2X2D24", "Mike Smith", "Likes Grape", "Drives an SUV", 35.0, 95.5),
                new Entry("1afb6f5d-a7c2-4311-a92d-974f3180ff5e", "3X3D35", "Jenny Walters", "Likes Avocados", "Rides A Scooter", 8.5, 15.3)
        );

        given(entryService.createEntries(any(MultipartFile.class)))
                .willReturn(entries);

        given(outcomeService.createOutcomes(anyList()))
                .willReturn(List.of(
                        new Outcome("John Smith", "Rides A Bike", 12.1),
                        new Outcome("Mike Smith", "Drives A SUV", 95.5),
                        new Outcome("Jenny Walters", "Rides A Scooter", 15.3)
                ));



        mockMvc.perform(multipart(BASE_URL).file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].transport").value("Rides A Bike"))
                .andExpect(jsonPath("$[0].topSpeed").value(12.1));

        verify(entryService, times(1)).createEntries(any(MultipartFile.class));
        verify(outcomeService, times(1)).createOutcomes(entries);
    }

    @Test
    public void emptyFile() throws Exception {
        final var mockFile = new MockMultipartFile("empty", new byte[0]);

        mockMvc.perform(multipart(BASE_URL).file(mockFile))
                .andExpect(status().isBadRequest());

        verify(entryService, times(0)).createEntries(any());
        verify(outcomeService, times(0)).createOutcomes(anyList());
    }

    @Test
    public void invalidFileType() throws Exception {
        final var mockFile = new MockMultipartFile("badType", "badType.pdf", "application/pdf", "badType".getBytes());

        mockMvc.perform(multipart(BASE_URL).file(mockFile))
                .andExpect(status().isBadRequest());

        verify(entryService, times(0)).createEntries(any());
        verify(outcomeService, times(0)).createOutcomes(anyList());
    }
}
