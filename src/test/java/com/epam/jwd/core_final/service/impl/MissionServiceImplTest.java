package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.exception.NotAbleToBeStartedException;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    void findAllFlightMissions_ShouldReturnAllFlightMissions() throws NotAbleToBeCreatedException {
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

    @Test
    public void findAllFlightMissions_willReturnEmptyList() {
        List<FlightMission> allMissions = service.findAllMissions();
        Assertions.assertTrue(allMissions.isEmpty());
    }

    @Test
    public void findAllFlightMissionsByCriteria_willReturnAllMissionsWithPlanetFromEarth() throws NotAbleToBeCreatedException {
        FlightMission firstMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 3, 3));
        FlightMission secondMission = new FlightMission("flight mission", "Mars->Venus", new Planet("Earth", 3, 3), new Planet("Venus", 5, 5));
        service.createMission(firstMission);
        service.createMission(secondMission);
        FlightMissionCriteria criteria = new FlightMissionCriteria.Builder().fromPlanetNameIs("Earth").build();
        List<FlightMission> allMissionsByCriteria = service.findAllMissionsByCriteria(criteria);

        Assertions.assertEquals(2, allMissionsByCriteria.size());
        Assertions.assertEquals(firstMission, allMissionsByCriteria.get(0));
        Assertions.assertEquals(secondMission, allMissionsByCriteria.get(1));

        service.findAllMissions().clear();
    }

    @Test
    public void findAllFlightMissionsByCriteria_willReturnAllMissions_whenEmptyCriteriaPassed() throws NotAbleToBeCreatedException {
        FlightMission firstMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        FlightMission secondMission = new FlightMission("flight mission", "Earth->Venus", new Planet("Earth", 1, 1), new Planet("Venus", 2, 2));
        service.createMission(firstMission);
        service.createMission(secondMission);
        FlightMissionCriteria criteria = new FlightMissionCriteria.Builder().build();
        List<FlightMission> allMissionsByCriteria = service.findAllMissionsByCriteria(criteria);

        Assertions.assertFalse(allMissionsByCriteria.isEmpty());
        Assertions.assertEquals(2, allMissionsByCriteria.size());

        service.findAllMissions().clear();
    }

    @Test
    public void updateFlightMissionDetails_willUpdateFlightMissionInStorage() throws NotAbleToBeCreatedException {
        FlightMission firstMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        service.createMission(firstMission);
        FlightMission updatedData = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Venus", 2, 2));
        service.updateFlightMissionDetails(updatedData);

        Assertions.assertEquals(firstMission.getFrom(), new Planet("Earth", 1, 1));
        Assertions.assertEquals(firstMission.getTo(), new Planet("Venus", 2, 2));
        service.findAllMissions().clear();
    }

    @Test
    public void createFlightMission_willCreateMissionAndAssignId() throws NotAbleToBeCreatedException {
        FlightMission firstMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        FlightMission mission = service.createMission(firstMission);
        Assertions.assertNotNull(mission.getId());
        service.findAllMissions().clear();
    }

    @Test
    public void createFlightMission_willThrowException_whenFlightMissionIsAlreadyInStorage() throws NotAbleToBeCreatedException {
        FlightMission flightMission = new FlightMission("first mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        flightMission = service.createMission(flightMission);
        FlightMission finalFlightMission = flightMission;
        Assertions.assertThrows(NotAbleToBeCreatedException.class, () -> service.createMission(finalFlightMission));
        service.findAllMissions().clear();
    }

    @Test
    public void startMission_willStartedFlightMissionAndAssignedMissionResultInProgress() throws NotAbleToBeStartedException, NotAbleToBeCreatedException {
        FlightMission flightMission = new FlightMission("first mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        Spaceship spaceship = new Spaceship("Zero", Collections.emptyMap(), 12123L);
        service.createMission(flightMission);
        flightMission.setAssignedSpaceShift(spaceship);
        service.startMission(flightMission);

        Assertions.assertSame(flightMission.getMissionResult(), MissionResult.IN_PROGRESS);

        service.findAllMissions().clear();
    }

    @Test
    public void startMission_willThrowException_whenFlightMissionNoSpaceship() throws NotAbleToBeCreatedException {
        FlightMission flightMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));
        service.createMission(flightMission);

        Assertions.assertThrows(NotAbleToBeStartedException.class, () -> service.startMission(flightMission));

        service.findAllMissions().clear();
    }

    @Test
    public void startMission_willThrowException_whenFlightMissionIsNotInStorage() {
        FlightMission flightMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 2, 2));

        Assertions.assertThrows(NotAbleToBeStartedException.class, () -> service.startMission(flightMission));
    }

    @Test
    public void simulate_willCalculateCompletedResult() throws NotAbleToBeCreatedException, NotAbleToBeStartedException {
        FlightMission flightMission = new FlightMission("flight mission", "Earth->Mars", new Planet("Earth", 1, 1), new Planet("Mars", 5, 5));
        service.createMission(flightMission);
        Spaceship spaceship = new Spaceship("Zero", Collections.emptyMap(), 12123L);
        flightMission.setAssignedSpaceShift(spaceship);
        flightMission = service.startMission(flightMission);
        flightMission.setStartDate(LocalDate.of(2002, 2, 1));
        flightMission.setEndDate(LocalDate.of(2010, 2, 4));
        service.simulate(flightMission, 100);

        Assertions.assertSame(flightMission.getMissionResult(), MissionResult.COMPLETED);

        service.findAllMissions().clear();
    }
}