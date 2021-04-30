package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;

class SpaceShipServiceImplTest {

    @Mock
    private SimpleAbstractBaseEntityStorage<Spaceship> storage;

    private final SpaceShipServiceImpl service;

    public SpaceShipServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        service = SpaceShipServiceImpl.INSTANCE;
    }

    @Test
    void findAllSpaceships_ShouldReturnAllSpaceships() throws NotAbleToBeCreatedException {
        List<Spaceship> spaceships = new ArrayList<>();
        Map<Role, Short> roleShortMap = new HashMap<>();
        roleShortMap.put(Role.COMMANDER, (short) 2);
        Spaceship first = new Spaceship("First", roleShortMap, 212L);
        Spaceship second = new Spaceship("Second", roleShortMap, 1212L);
        spaceships.add(first);
        spaceships.add(second);
        service.createSpaceship(first);
        service.createSpaceship(second);
        spaceships.get(0).setId((long) 0);
        spaceships.get(1).setId((long) 1);
        given(storage.getAll()).willReturn(spaceships);
        List<Spaceship> allCrewMembers = service.findAllSpaceships();
        Assertions.assertEquals(spaceships, allCrewMembers);
        service.findAllSpaceships().clear();
    }

    @Test
    public void findAllSpaceships_willReturnEmptyList() {
        List<Spaceship> allSpaceships = service.findAllSpaceships();

        Assertions.assertTrue(allSpaceships.isEmpty());
    }

    @Test
    public void findAllSpaceshipsByCriteria_willReturnSpaceshipsWithDistanceThousand() throws NotAbleToBeCreatedException {
        Spaceship firstSpaceship = new Spaceship("first spaceship", Collections.emptyMap(), 1000L);
        Spaceship secondSpaceship = new Spaceship("second spaceship", Collections.emptyMap(), 1000L);
        Spaceship thirdSpaceship = new Spaceship("second spaceship", Collections.emptyMap(), 1221L);
        service.createSpaceship(firstSpaceship);
        service.createSpaceship(secondSpaceship);
        service.createSpaceship(thirdSpaceship);
        SpaceshipCriteria criteria = new SpaceshipCriteria.Builder().flightDistanceEquals(1000L).build();
        List<Spaceship> allSpaceshipsByCriteria = service.findAllSpaceshipsByCriteria(criteria);

        Assertions.assertFalse(allSpaceshipsByCriteria.isEmpty());
        Assertions.assertEquals(2, allSpaceshipsByCriteria.size());
        Assertions.assertEquals(firstSpaceship, allSpaceshipsByCriteria.get(0));
        Assertions.assertEquals(secondSpaceship, allSpaceshipsByCriteria.get(1));

        service.findAllSpaceships().clear();
    }

    @Test
    public void findAllSpacehipsByCriteria_willReturnAllSpaceships_whenEmptyCriteriaPassed() throws NotAbleToBeCreatedException {
        Spaceship firstSpaceship = new Spaceship("first spaceship", Collections.emptyMap(), 1000L);
        Spaceship secondSpaceship = new Spaceship("second spaceship", Collections.emptyMap(), 1000L);
        Spaceship thirdSpaceship = new Spaceship("second spaceship", Collections.emptyMap(), 1221L);
        service.createSpaceship(firstSpaceship);
        service.createSpaceship(secondSpaceship);
        service.createSpaceship(thirdSpaceship);
        SpaceshipCriteria criteria = new SpaceshipCriteria.Builder().build();
        List<Spaceship> allSpaceshipsByCriteria = service.findAllSpaceshipsByCriteria(criteria);

        Assertions.assertFalse(allSpaceshipsByCriteria.isEmpty());
        Assertions.assertEquals(3, allSpaceshipsByCriteria.size());
        Assertions.assertEquals(firstSpaceship, allSpaceshipsByCriteria.get(0));
        Assertions.assertEquals(secondSpaceship, allSpaceshipsByCriteria.get(1));
        Assertions.assertEquals(thirdSpaceship, allSpaceshipsByCriteria.get(2));

        service.findAllSpaceships().clear();
    }

    @Test
    public void updateSpaceshipsDetails_willUpdateSpaceshipInStorage() throws NotAbleToBeCreatedException {
        Spaceship testSpaceship = new Spaceship("Zero", Collections.emptyMap(), 1000L);
        service.createSpaceship(testSpaceship);
        Spaceship updatedData = new Spaceship("Zero", Collections.emptyMap(), 5000L);
        service.updateSpaceshipDetails(updatedData);

        Assertions.assertEquals(5000L, testSpaceship.getFlightDistance());

        service.findAllSpaceships().clear();
    }

    @Test
    public void updateSpaceshipsDetails_willNotUpdateSpaceship_whenItIsNotInStorage() throws NotAbleToBeCreatedException {
        Spaceship testSpaceship = new Spaceship("Zero", Collections.emptyMap(), 1000L);
        service.createSpaceship(testSpaceship);
        Spaceship updatedDate = new Spaceship("Double", Collections.emptyMap(), 7000L);
        service.updateSpaceshipDetails(updatedDate);

        Assertions.assertEquals(1000L, testSpaceship.getFlightDistance());

        service.findAllSpaceships().clear();
    }

    @Test
    public void createSpaceship_willAssignIdToSpaceshipAndSave() throws NotAbleToBeCreatedException {
        Spaceship testSpaceship = new Spaceship("Zero", Collections.emptyMap(), 1000L);
        service.createSpaceship(testSpaceship);

        Assertions.assertNotNull(testSpaceship.getId());

        service.findAllSpaceships().clear();
    }

    @Test
    public void creatSpaceship_willThrowException_whenSpaceshipIsAlreadyInStorage() throws NotAbleToBeCreatedException {
        Spaceship testSpaceship = new Spaceship("Zero", Collections.emptyMap(), 1000L);
        service.createSpaceship(testSpaceship);

        Assertions.assertThrows(NotAbleToBeCreatedException.class, () -> service.createSpaceship(testSpaceship));

        service.findAllSpaceships().clear();
    }
}