package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeAssignedException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public enum SpaceShipServiceImpl implements SpaceshipService {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SpaceShipServiceImpl.class);
    private final AbstractBaseEntityStorage<Spaceship> storage = new SimpleAbstractBaseEntityStorage<>();
    private CrewService crewService = CrewServiceImpl.INSTANCE;
    private MissionService missionService = MissionServiceImpl.INSTANCE;

    @Override
    public List<Spaceship> findAllSpaceships() {
        return storage.getAll();
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(SpaceshipCriteria criteria) {
        return storage.getAll().stream().filter(criteria::matches).collect(Collectors.toList());
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(SpaceshipCriteria criteria) {
        return findAllSpaceshipsByCriteria(criteria).stream().findAny();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship spaceship) {
        Optional<Spaceship> optionalSpaceshipInStorage = findSpaceshipByCriteria(new SpaceshipCriteria.Builder().nameEquals(spaceship.getName()).build());
        if (optionalSpaceshipInStorage.isPresent()) {
            Spaceship spaceshipInStorage = optionalSpaceshipInStorage.get();
            update(spaceshipInStorage, spaceship);
            logger.info("Spaceship {} was updated", spaceship.getName());
        }
        return spaceship;
    }

    @Override
    public void assignSpaceshipOnMission(Spaceship spaceship) throws NotAbleToBeAssignedException {
        Random random = new Random();
        List<FlightMission> flightMissions = missionService.findAllMissions();
        FlightMission randomFlightMission = flightMissions.get(random.nextInt(flightMissions.size()));
        checkForAssign(spaceship, randomFlightMission);
        spaceship.setAssignedForFlightMission(true);
        randomFlightMission.setAssignedSpaceShift(spaceship);
        logger.info("Spaceship {} was assigned to flight mission {}", spaceship.getName(), randomFlightMission.getMissionsName());
    }

    @Override
    public Spaceship createSpaceship(Spaceship spaceship) throws NotAbleToBeCreatedException {
        if (isExistsInStorage(spaceship)) {
            logger.error("Spaceship {} is already in storage", spaceship.getName());
            throw new NotAbleToBeCreatedException("Spaceship is already in storage");
        }
        return storage.create(spaceship);
    }

    private void checkForAssign(Spaceship spaceship, FlightMission flightMission) throws NotAbleToBeAssignedException {
        if (!isExistsInStorage(spaceship)) {
            logger.error("Spaceship {}, doesn't exist in storage", spaceship.getName());
            throw new NotAbleToBeAssignedException("Spaceship doesn't exist in storage");
        }
        if (spaceship.getAssignedForFlightMission()) {
            logger.error("Spaceship {} is already assigned to flight mission {}", spaceship.getName(), flightMission.getMissionsName());
            throw new NotAbleToBeAssignedException("Spaceship is already assigned to flight mission");
        }
        if (flightMission.getAssignedSpaceShift() != null) {
            logger.error("Flight mission {} already has assigned spaceship", flightMission.getMissionsName());
            throw new NotAbleToBeAssignedException("Flight missions already has assigned spaceship");
        }
    }

    private boolean isExistsInStorage(Spaceship spaceship) {
        Optional<Spaceship> optionalCrewMember = storage.getAll().stream()
                .filter(crewMemberInStorage -> crewMemberInStorage.getId().equals(spaceship.getId())).findAny();
        return optionalCrewMember.isPresent();
    }

    private void update(Spaceship updatedSpaceship, Spaceship newData) {
        updatedSpaceship.setFlightDistance(newData.getFlightDistance() == null ? updatedSpaceship.getFlightDistance() : newData.getFlightDistance());
        updatedSpaceship.setReadyForNextMissions(newData.getReadyForNextMissions() == null ? updatedSpaceship.getReadyForNextMissions() : newData.getReadyForNextMissions());
        updatedSpaceship.setAssignedForFlightMission(newData.getAssignedForFlightMission() == null ? updatedSpaceship.getAssignedForFlightMission() : newData.getAssignedForFlightMission());
    }
}
