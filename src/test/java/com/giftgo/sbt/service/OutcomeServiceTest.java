package com.giftgo.sbt.service;

import com.giftgo.sbt.model.Entry;
import com.giftgo.sbt.model.Outcome;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutcomeServiceTest {

    private static OutcomeService outcomeService;

    @BeforeAll
    public static void setup() {
        outcomeService = new OutcomeService(null, false);
    }

    @Test
    public void testService() {
        var entries = List.of(
                new Entry(
                        "18148426-89e1-11ee-b9d1-0242ac120002",
                        "1X1D14",
                        "John Smith",
                        "Likes Apricots",
                        "Rides A Bike",
                        6.2,
                        12.1
                ),
                new Entry(
                        "3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7",
                        "2X2D24",
                        "Mike Smith",
                        "Likes Grape",
                        "Drives an SUV",
                        35.0,
                        95.5
                ),
                new Entry(
                        "1afb6f5d-a7c2-4311-a92d-974f3180ff5e",
                        "3X3D35",
                        "Jenny Walters",
                        "Likes Avocados",
                        "Rides A Scooter",
                        8.5,
                        15.3
                )
        );

        var outcomes = outcomeService.createOutcomes(entries);

        assertEquals(3, outcomes.size());
        assertTrue(outcomes.contains(new Outcome("John Smith", "Rides A Bike", 12.1)));
        assertTrue(outcomes.contains(new Outcome("Mike Smith", "Drives an SUV", 95.5)));
        assertTrue(outcomes.contains(new Outcome("Jenny Walters", "Rides A Scooter", 15.3)));
    }

    @Test
    public void emptyList() {
        assertEquals(0, outcomeService.createOutcomes(List.of()).size());
    }
}
