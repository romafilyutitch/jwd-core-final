package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightMissionFactory implements EntityFactory<FlightMission> {
    private static final Logger logger = LoggerFactory.getLogger(FlightMissionFactory.class);
    @Override
    public FlightMission create(Object... args) {
        logger.trace("creation of FlightMission object ");
        if (args.length != 3) {
            logger.error("passed illegal array of objects");
            throw new IllegalArgumentException("args length must be 3");
        }
        String missionsName = String.valueOf(args[0]);
        Planet fromPlanet = (Planet) args[1];
        Planet toPlanet = (Planet) args[2];
        logger.trace("creation was completed");
        return new FlightMission("Flight mission", missionsName, fromPlanet, toPlanet);
    }
}
