package com.epam.jwd.core_final.storage.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleAbstractBaseEntityStorageTest {

    @Test
    public void testCreate_ShouldAssignIdToCrewMember() {
        SimpleAbstractBaseEntityStorage<CrewMember> storage = new SimpleAbstractBaseEntityStorage<>();
        CrewMember crewMember = new CrewMember("Test Member", Role.PILOT, Rank.CAPTAIN);
        CrewMember createdCrewMember = storage.create(crewMember);

        Assertions.assertNotNull(crewMember.getId());
        Assertions.assertEquals(crewMember.getName(), createdCrewMember.getName());
        Assertions.assertEquals(crewMember.getRole(), createdCrewMember.getRole());
        Assertions.assertEquals(crewMember.getRank(), createdCrewMember.getRank());
    }

    @Test
    public void testGetAll_ShouldReturnAllCrewMembers() {
        SimpleAbstractBaseEntityStorage<CrewMember> storage = new SimpleAbstractBaseEntityStorage<>();
        List<CrewMember> members = new ArrayList<>();
        CrewMember crewMember = new CrewMember("Test Member", Role.PILOT, Rank.CAPTAIN);
        CrewMember secondCrewMember = new CrewMember("Second test Member", Role.COMMANDER, Rank.SECOND_OFFICER);
        members.add(crewMember);
        members.add(secondCrewMember);
        storage.create(crewMember);
        storage.create(secondCrewMember);
        List<CrewMember> listOfMembers = storage.getAll();
        members.get(0).setId((long) 0);
        members.get(1).setId((long) 1);

        Assertions.assertEquals(members, listOfMembers);
    }

    @Test
    public void testCreate_ShouldReturnEmptyList_IfStorageIsEmpty() {
        SimpleAbstractBaseEntityStorage<CrewMember> storage = new SimpleAbstractBaseEntityStorage<>();
        List<CrewMember> emptyList = storage.getAll();
        Assertions.assertTrue(emptyList.isEmpty());
    }
}