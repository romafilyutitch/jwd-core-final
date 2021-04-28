package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

class CrewServiceImplTest {

    @Mock
    private SimpleAbstractBaseEntityStorage<CrewMember> storage;

    private final CrewServiceImpl service;

    public CrewServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        service = CrewServiceImpl.INSTANCE;
    }

    @Test
    void testFindAllCrewMembers_ShouldReturnAllCrewMembers() throws NotAbleToBeCreatedException {
        List<CrewMember> crewMembers = new ArrayList<>();
        CrewMember first = new CrewMember("First", Role.PILOT, Rank.CAPTAIN);
        CrewMember second = new CrewMember("Second", Role.COMMANDER, Rank.CAPTAIN);
        crewMembers.add(first);
        crewMembers.add(second);
        service.createCrewMember(first);
        service.createCrewMember(second);
        crewMembers.get(0).setId((long) 0);
        crewMembers.get(1).setId((long) 1);
        given(storage.getAll()).willReturn(crewMembers);
        List<CrewMember> allCrewMembers = service.findAllCrewMembers();
        Assertions.assertEquals(crewMembers, allCrewMembers);
    }
}