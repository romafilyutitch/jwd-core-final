package com.epam.jwd.core_final.service.impl;

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

class SpaceMapServiceImplTest {

    @Mock
    private SimpleAbstractBaseEntityStorage<Planet> storage;

    private final SpaceMapServiceImpl service;

    public SpaceMapServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        service = SpaceMapServiceImpl.INSTANCE;
    }

    @Test
    void findAllPlanets_ShouldReturnAllPlanets() throws NotAbleToBeCreatedException {
        List<Planet> planets = new ArrayList<>();
        Planet first = new Planet("First", 1, 2);
        Planet second = new Planet("Second", 3, 1);
        planets.add(first);
        planets.add(second);
        service.createPlanet(first);
        service.createPlanet(second);
        planets.get(0).setId((long) 0);
        planets.get(1).setId((long) 1);
        given(storage.getAll()).willReturn(planets);
        List<Planet> allCrewMembers = service.getAllPlanets();
        Assertions.assertEquals(planets, allCrewMembers);
    }

    @Test
    public void findAllPlanets_willReturnEmptyList() {
        List<Planet> allPlanets = service.getAllPlanets();

        Assertions.assertTrue(allPlanets.isEmpty());
    }

    @Test
    public void getRandomPlanet_willReturnRandomPlanet() throws NotAbleToBeCreatedException {
        Planet testPlanet = new Planet("Random planet", 1, 1);
        service.createPlanet(testPlanet);
        Planet randomPlanet = service.getRandomPlanet();

        Assertions.assertNotNull(randomPlanet);
        Assertions.assertEquals(testPlanet, randomPlanet);

        service.getAllPlanets().clear();
    }

    @Test
    public void createPlanet_willSavePlanetAssignIdToPlanet() throws NotAbleToBeCreatedException {
        Planet testPlanet = new Planet("Random planet", 1, 1);
        service.createPlanet(testPlanet);

        Assertions.assertNotNull(testPlanet.getId());

        service.getAllPlanets().clear();
    }

    @Test
    public void createPlanet_willThrowException_whenPlanetIsAlreadyInStorage() throws NotAbleToBeCreatedException {
        Planet testPlanet = new Planet("Random planet", 1, 1);
        service.createPlanet(testPlanet);

        Assertions.assertThrows(NotAbleToBeCreatedException.class, () -> service.createPlanet(testPlanet));

        service.getAllPlanets().clear();
    }
}