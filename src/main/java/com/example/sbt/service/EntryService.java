package com.example.sbt.service;

import com.example.sbt.exception.ValidationException;
import com.example.sbt.model.Entry;
import com.example.sbt.validator.ModelValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntryService {

    @Autowired
    private final ModelValidator<Entry> validator;

    private final boolean validationEnabled;

    public EntryService(ModelValidator<Entry> validator, @Value("${feature.validation.enabled}") boolean validationEnabled) {
        this.validator = validator;
        this.validationEnabled = validationEnabled;
    }

    private final String[] HEADERS = {"UUID", "ID", "Name", "Likes", "Transport", "Avg Speed", "Top Speed"};

    public List<Entry> createEntries(MultipartFile file) throws IOException {
        return parse(file.getInputStream());
    }

    private List<Entry> parse(InputStream inputStream) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter('|')
                .setHeader(HEADERS).get();

        CSVParser csvParser = CSVParser.parse(inputStream, null, csvFormat);

        return csvParser.stream()
                .map(csvRecord -> {
                            var entry = toDomain(csvRecord);

                            if (validationEnabled) {
                                validator.validateObject(entry, "Model validation failed for UUID: " + entry.uuid());
                            }

                            return entry;
                        }
                )
                .collect(Collectors.toList());
    }

    private Entry toDomain(CSVRecord record) {
        final var uuid = record.get("UUID");

        return new Entry(uuid,
                record.get("ID"),
                record.get("Name"),
                record.get("Likes"),
                record.get("Transport"),
                toDouble(record, uuid, "Avg Speed"),
                toDouble(record, uuid, "Top Speed"));
    }

    private Double toDouble(CSVRecord record, String uuid, String column) {
        var speed = record.get(column);
        if (speed.isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(speed);
        } catch (NumberFormatException e) {
            if (validationEnabled) {
                throw new ValidationException(
                        "Model validation failed for UUID: " + uuid,
                        List.of("Failed to parse %s {%s} to Double".formatted(column, speed))
                );
            }
            return null;
        }
    }
}
