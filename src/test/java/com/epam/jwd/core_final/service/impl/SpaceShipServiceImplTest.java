package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
    void testFindAllSpaceships_ShouldReturnAllSpaceships() throws NotAbleToBeCreatedException {
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
    }
}