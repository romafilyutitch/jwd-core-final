package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.exception.NotAbleToBeStartedException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MissionService {

    List<FlightMission> findAllMissions();

    List<FlightMission> findAllMissionsByCriteria(FlightMissionCriteria criteria);

    Optional<FlightMission> findMissionByCriteria(FlightMissionCriteria criteria);

    FlightMission updateFlightMissionDetails(FlightMission flightMission);

    FlightMission createMission(FlightMission flightMission) throws NotAbleToBeCreatedException;

    FlightMission startMission(FlightMission flightMission) throws NotAbleToBeStartedException;

    FlightMission simulate(FlightMission flightMission, long years);

}
