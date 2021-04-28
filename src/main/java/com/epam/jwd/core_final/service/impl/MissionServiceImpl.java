package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.exception.NotAbleToBeStartedException;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public enum MissionServiceImpl implements MissionService {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(MissionServiceImpl.class);
    private final AbstractBaseEntityStorage<FlightMission> storage = new SimpleAbstractBaseEntityStorage<>();
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public List<FlightMission> findAllMissions() {
        return storage.getAll();
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(FlightMissionCriteria criteria) {
        return storage.getAll().stream().filter(criteria::matches).collect(Collectors.toList());
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(FlightMissionCriteria criteria) {
        return findAllMissionsByCriteria(criteria).stream().findAny();
    }

    @Override
    public FlightMission updateFlightMissionDetails(FlightMission flightMission) {
        Optional<FlightMission> optionalFlightMissionInStorage = findMissionByCriteria(new FlightMissionCriteria.Builder().missionsNameEquals(flightMission.getMissionsName()).build());
        if (optionalFlightMissionInStorage.isPresent()) {
            FlightMission flightMissionInStorage = optionalFlightMissionInStorage.get();
            update(flightMissionInStorage, flightMission);
            logger.info("Flight mission in storage was updated");
        }
        return flightMission;
    }

    @Override
    public FlightMission createMission(FlightMission flightMission) throws NotAbleToBeCreatedException {
        if (isExistsInStorage(flightMission)) {
            logger.error("Flight mission {} already exists int storage", flightMission.getMissionsName());
            throw new NotAbleToBeCreatedException("Flight Mission " + flightMission.getMissionsName() + " already exists in storage");
        }
        logger.info("Flight missions {} was created", flightMission.getMissionsName());
        return storage.create(flightMission);
    }

    @Override
    public FlightMission startMission(FlightMission flightMission) throws NotAbleToBeStartedException {
        if (!isExistsInStorage(flightMission)) {
            logger.error("Flight mission {} is not in storage", flightMission.getMissionsName());
            throw new NotAbleToBeStartedException("Flight mission " + flightMission.getMissionsName() + " not in storage");
        }
        if (flightMission.getAssignedSpaceShift() == null) {
            logger.error("Flight mission {} hasn't assigned spaceship", flightMission.getMissionsName());
            throw new NotAbleToBeStartedException("Flight missions " + flightMission.getMissionsName() + " hasn't spaceship");
        }
        Random random = new Random();
        SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;
        long distance = spacemapService.getDistanceBetweenPlanets(flightMission.getFrom(), flightMission.getTo());
        logger.info("For flight mission {} was calculated distance : {}", flightMission.getMissionsName(), distance);
        flightMission.setDistance(distance);
        int year = random.nextInt(20) + 2000;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        LocalDate startDate = LocalDate.of(year, month, day);
        flightMission.setStartDate(startDate);
        LocalDate endDate = startDate.plusYears(distance);
        flightMission.setEndDate(endDate);
        logger.info("For flight mission {} was calculated end date: {}", flightMission.getMissionsName(), endDate);
        flightMission.setMissionResult(MissionResult.IN_PROGRESS);
        logger.info("Flight mission {} was started", flightMission.getMissionsName());
        return flightMission;
    }

    @Override
    public FlightMission simulate(FlightMission flightMission, long years) {
        MissionResult missionResult = flightMission.getMissionResult();
        LocalDate calculatedDate = flightMission.getStartDate().plusYears(years);
        if (missionResult == MissionResult.IN_PROGRESS) {
            if (calculatedDate.isAfter(flightMission.getEndDate())) {
                if (flightMission.getAssignedSpaceShift().getFlightDistance() > flightMission.getDistance()) {
                    flightMission.setMissionResult(MissionResult.COMPLETED);
                    logger.info("Flight mission {} has reached destination planet {}", flightMission.getMissionsName(), flightMission.getTo());
                } else {
                    logger.info("Flight mission {} was failed", flightMission.getMissionsName());
                    flightMission.setMissionResult(MissionResult.FAILED);
                }
            }
        }
        return flightMission;
    }

    private boolean isExistsInStorage(FlightMission flightMission) {
        Optional<FlightMission> optionalFlightMission = storage.getAll().stream()
                .filter(flightMissionInStorage -> flightMissionInStorage.getId().equals(flightMission.getId()))
                .findAny();
        return optionalFlightMission.isPresent();
    }

    private void update(FlightMission updatedFlightMission, FlightMission newData) {
        updatedFlightMission.setFrom(newData.getFrom() == null ? updatedFlightMission.getFrom() : newData.getFrom());
        updatedFlightMission.setTo(newData.getTo() == null ? updatedFlightMission.getTo() : newData.getTo());
        String newMissionName = updatedFlightMission.getFrom().getName() + "->" + updatedFlightMission.getTo().getName();
        updatedFlightMission.setMissionsName(newMissionName);
    }
}
