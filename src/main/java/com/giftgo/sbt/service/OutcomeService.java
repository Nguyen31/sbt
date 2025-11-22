package com.giftgo.sbt.service;

import com.giftgo.sbt.model.Entry;
import com.giftgo.sbt.model.Outcome;
import com.giftgo.sbt.validator.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutcomeService {

    @Autowired
    private final ModelValidator<Outcome> validator;

    private boolean validationEnabled;

    public OutcomeService(ModelValidator<Outcome> validator, @Value("${feature.validation.enabled}") boolean validationEnabled) {
        this.validator = validator;
        this.validationEnabled = validationEnabled;
    }

    public List<Outcome> createOutcomes(List<Entry> entries) {
        return entries.stream()
                .map(entry -> {
                    final var outcome = toDomain(entry);

                    if (validationEnabled) {
                        validator.validateObject(outcome, "Model validation failed for outcome: " + outcome.name());
                    }

                    return outcome;
                })
                .toList();
    }

    private Outcome toDomain(Entry entry) {
        return new Outcome(entry.name(), entry.transport(), entry.topSpeed());
    }
}
