package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanetFactory implements EntityFactory<Planet> {
    private static final Logger logger = LoggerFactory.getLogger(PlanetFactory.class);
    @Override
    public Planet create(Object... args) {
        logger.trace("Creation of Planet objects");
        if (args.length != 3) {
            logger.error("passed illegal array of objects");
            throw new IllegalArgumentException("args length must be 3");
        }
        String planetName = String.valueOf(args[0]);
        int x = (int) args[1];
        int y = (int) args[2];
        logger.trace("creation was completed");
        return new Planet(planetName, x, y);
    }
}
