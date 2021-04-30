package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

class CrewServiceImplTest {

    @Mock
    private SimpleAbstractBaseEntityStorage<CrewMember> storage;
    @Mock
    private MissionService missionsService = MissionServiceImpl.INSTANCE;

    private final CrewServiceImpl service;

    public CrewServiceImplTest() {
        MockitoAnnotations.initMocks(this);
        service = CrewServiceImpl.INSTANCE;
    }

    @Before
    public void setUp() {
        service.findAllCrewMembers().clear();
    }

    @After
    public void tearDown() {
        service.findAllCrewMembers().clear();
    }

    @Test
    void findAllCrewMembers_ShouldReturnAllCrewMembers() throws NotAbleToBeCreatedException {
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

    @Test
    public void findAllCrewMembers_WillReturnEmptyList_whenNoCrewMembersInStorage() {
        List<CrewMember> crewMembers = service.findAllCrewMembers();
        Assertions.assertTrue(crewMembers.isEmpty());
    }

    @Test
    public void findAllCrewMembersByCriteria_WillReturnRightCrewMemberByCriteria() throws NotAbleToBeCreatedException {
        CrewMemberCriteria criteria = new CrewMemberCriteria.Builder().roleIs(Role.COMMANDER).build();
        CrewMember testCommander = new CrewMember("test crew member", Role.COMMANDER, Rank.CAPTAIN);
        CrewMember testOtherCommander = new CrewMember("test other member", Role.COMMANDER, Rank.TRAINEE);
        CrewMember testThird = new CrewMember("test third", Role.MISSION_SPECIALIST, Rank.FIRST_OFFICER);
        testCommander = service.createCrewMember(testCommander);
        testOtherCommander = service.createCrewMember(testOtherCommander);
        testThird = service.createCrewMember(testThird);
        List<CrewMember> commanders = Arrays.asList(testCommander, testOtherCommander);
        List<CrewMember> allCrewMembersByCriteria = service.findAllCrewMembersByCriteria(criteria);
        service.findAllCrewMembers().clear();
        Assertions.assertEquals(2, allCrewMembersByCriteria.size());
        Assertions.assertEquals(commanders, allCrewMembersByCriteria);
    }

    @Test
    public void findAllCrewMembersByCriteria_willReturnAllMembers_whenEmptyCriteriaPassed() throws NotAbleToBeCreatedException {
        CrewMemberCriteria criteria = new CrewMemberCriteria.Builder().build();
        CrewMember testCommander = new CrewMember("test crew member", Role.COMMANDER, Rank.CAPTAIN);
        CrewMember testOtherCommander = new CrewMember("test other member", Role.COMMANDER, Rank.TRAINEE);
        CrewMember testThird = new CrewMember("test third", Role.MISSION_SPECIALIST, Rank.FIRST_OFFICER);
        service.createCrewMember(testCommander);
        service.createCrewMember(testOtherCommander);
        service.createCrewMember(testThird);

        List<CrewMember> crewMembers = service.findAllCrewMembersByCriteria(criteria);
        service.findAllCrewMembers().clear();
        Assertions.assertFalse(crewMembers.isEmpty());
    }

    @Test
    public void updateCrewMemberDetails_willUpdateCrewMemberInStorage() throws NotAbleToBeCreatedException {
        CrewMember crewMember = new CrewMember("test crew member", Role.COMMANDER, Rank.FIRST_OFFICER);
        crewMember = service.createCrewMember(crewMember);
        CrewMember updatedData = new CrewMember("test crew member", Role.PILOT, Rank.SECOND_OFFICER);
        service.updateCrewMemberDetails(updatedData);
        service.findAllCrewMembers().clear();
        Assertions.assertSame(crewMember.getRole(), Role.PILOT, "Updated member role must change");
        Assertions.assertSame(crewMember.getRank(), Rank.SECOND_OFFICER, "Updated member rank must change");
    }

    @Test
    public void updateCrewMemberDetails_willNotUpdatedCrewMemerWithWrongName() throws NotAbleToBeCreatedException {
        CrewMember crewMember = new CrewMember("test crew member", Role.COMMANDER, Rank.FIRST_OFFICER);
        crewMember = service.createCrewMember(crewMember);
        CrewMember updatedData = new CrewMember("Unknown crew member", Role.PILOT, Rank.SECOND_OFFICER);
        service.updateCrewMemberDetails(updatedData);
        service.findAllCrewMembers().clear();
        Assertions.assertSame(crewMember.getRole(), Role.COMMANDER, "Updated member role must not change");
        Assertions.assertSame(crewMember.getRank(), Rank.FIRST_OFFICER, "Updated member rank must not change");
    }

    @Test
    public void createCrewMember_willCreateCrewMemberAndAssignIdToCrewMember() throws NotAbleToBeCreatedException {
        CrewMember crewMember = new CrewMember("test crew member", Role.COMMANDER, Rank.SECOND_OFFICER);
        crewMember = service.createCrewMember(crewMember);
        service.findAllCrewMembers().clear();
        Assertions.assertNotNull(crewMember.getId());
    }

    @Test
    public void createCrewMember_willThrowException_whenCrewMemberIsAlreadyCreated() throws NotAbleToBeCreatedException {
        CrewMember crewMember = new CrewMember("test crew member", Role.COMMANDER, Rank.TRAINEE);
        crewMember = service.createCrewMember(crewMember);

        CrewMember finalCrewMember = crewMember;
        Assertions.assertThrows(NotAbleToBeCreatedException.class, () -> service.createCrewMember(finalCrewMember));
        service.findAllCrewMembers().clear();
    }
}