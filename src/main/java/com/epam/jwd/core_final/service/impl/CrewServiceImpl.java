package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.NotAbleToBeAssignedException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public enum CrewServiceImpl implements CrewService {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(CrewServiceImpl.class);
    private final AbstractBaseEntityStorage<CrewMember> storage = new SimpleAbstractBaseEntityStorage<>();
    private SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;
    private MissionService missionService = MissionServiceImpl.INSTANCE;

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return storage.getAll();
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(CrewMemberCriteria criteria) {
        return storage.getAll().stream().filter(criteria::matches).collect(Collectors.toList());
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(CrewMemberCriteria criteria) {
        return storage.getAll().stream().filter(criteria::matches).findAny();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember) {
        Optional<CrewMember> optionalCrewMemberInStorage = findCrewMemberByCriteria(new CrewMemberCriteria.Builder().nameEquals(crewMember.getName()).build());
        if (optionalCrewMemberInStorage.isPresent()) {
            CrewMember crewMemberInStorage = optionalCrewMemberInStorage.get();
            update(crewMemberInStorage, crewMember);
            logger.info("Crew member in storage was updated");
        }
        return crewMember;
    }

    @Override
    public void assignCrewMemberOnMission(CrewMember crewMember) throws NotAbleToBeAssignedException {
        Random random = new Random();
        List<FlightMission> flightMissions = missionService.findAllMissions();
        FlightMission flightMission = flightMissions.get(random.nextInt(flightMissions.size()));
        checkForAssign(crewMember, flightMission);
        crewMember.setAssignedToFlightMission(true);
        flightMission.getAssignedCrew().add(crewMember);
        logger.info("Crew member {} was assigned to FlightMission {} ", crewMember, flightMission);
    }

    @Override
    public CrewMember createCrewMember(CrewMember crewMember) throws NotAbleToBeCreatedException {
        if (isExistsInStorage(crewMember)) {
            logger.error("Crew member {} wasn't created because crew member with this id is already in storage", crewMember);
            throw new NotAbleToBeCreatedException("Crew member with name " + crewMember.getName() + " is already in storage");
        }
        logger.info("Crew member {} was created", crewMember);
        return storage.create(crewMember);
    }

    private void checkForAssign(CrewMember crewMember, FlightMission flightMission) throws NotAbleToBeAssignedException {
        if (!isExistsInStorage(crewMember)) {
            logger.error("Crew member {} doesn't exist in storage", crewMember);
            throw new NotAbleToBeAssignedException("Crew member doesn't exist in storage");
        }
        if (flightMission.getAssignedSpaceShift() == null) {
            logger.error("FlightMission {} haven't spaceship to assign crew member", flightMission);
            throw new NotAbleToBeAssignedException("Flight mission doesn't has spaceship to assign");
        }
        if (isAlreadyInList(flightMission.getAssignedCrew(), crewMember)) {
            logger.error("Crew member {} is already exists in crew member list", crewMember);
            throw new NotAbleToBeAssignedException("Crew member is already in crew members list");
        }
        if (isRoleAmountOverTheLimit(crewMember.getRole(), flightMission)) {
            logger.error("Amount of crew members with the same role as crew member is over the limit");
            throw new NotAbleToBeAssignedException("Amount of crew members with this role is over the limit");
        }
    }

    private boolean isExistsInStorage(CrewMember crewMember) {
        Optional<CrewMember> optionalCrewMember = storage.getAll().stream()
                .filter(crewMemberInStorage -> crewMemberInStorage.getId().equals(crewMember.getId())).findAny();
        return optionalCrewMember.isPresent();
    }

    private boolean isAlreadyInList(List<CrewMember> crewMembers, CrewMember member) {
        Optional<CrewMember> optionalCrewMember = crewMembers.stream().
                filter(crewMember -> crewMember.getId().equals(member.getId()))
                .findAny();
        return optionalCrewMember.isPresent();
    }

    private boolean isRoleAmountOverTheLimit(Role role, FlightMission flightMission) {
        int limitOnRole = flightMission.getAssignedSpaceShift().getCrew().get(role);
        long actualMemberAmount = flightMission.getAssignedCrew().stream()
                .filter(crewMember -> crewMember.getRole() == role)
                .count();
        return limitOnRole == actualMemberAmount;
    }

    private void update(CrewMember updatedMember, CrewMember newData) {
        updatedMember.setRole(newData.getRole() == null ? updatedMember.getRole() : newData.getRole());
        updatedMember.setRank(newData.getRank() == null ? updatedMember.getRank() : newData.getRank());
    }
}
