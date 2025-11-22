package com.giftgo.sbt.controller;

import com.giftgo.sbt.exception.BadRequestException;
import com.giftgo.sbt.model.Outcome;
import com.giftgo.sbt.service.EntryService;
import com.giftgo.sbt.service.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/api")
public class OutcomeController {

    @Autowired
    private OutcomeService outcomeService;

    @Autowired
    private EntryService entryService;

    @PostMapping(path = "/outcomes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Outcome> createOutcomes(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty.");
        }

        if (!Objects.equals(file.getContentType(), MediaType.TEXT_PLAIN_VALUE)) {
            throw new BadRequestException("Invalid file type. Expected .txt");
        }

        var entries = entryService.createEntries(file);
        return outcomeService.createOutcomes(entries);
    }
}
