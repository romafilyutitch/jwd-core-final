package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

class MissionServiceImplTest {

    @Mock
    private SimpleAbstractBaseEntityStorage<FlightMission> storage;

    private final MissionServiceImpl service;

    public MissionServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        service = MissionServiceImpl.INSTANCE;
    }

    @Test
    void testFindAllFlightMissions_ShouldReturnAllFlightMissions() throws NotAbleToBeCreatedException {
        List<FlightMission> missions = new ArrayList<>();
        FlightMission first = new FlightMission("Flight mission", "A->B", new Planet("APlanet", 1, 2), new Planet("BPlanet", 2, 3));
        FlightMission second = new FlightMission("Second mission", "R-Z", new Planet("1planet", 2, 2), new Planet("2planet", 3, 3));
        missions.add(first);
        missions.add(second);
        service.createMission(first);
        service.createMission(second);
        missions.get(0).setId((long) 0);
        missions.get(1).setId((long) 1);
        given(storage.getAll()).willReturn(missions);
        List<FlightMission> allMissions = service.findAllMissions();
        Assertions.assertEquals(missions, allMissions);
    }
}