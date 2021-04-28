package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Planet.Point;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.storage.AbstractBaseEntityStorage;
import com.epam.jwd.core_final.storage.impl.SimpleAbstractBaseEntityStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public enum SpaceMapServiceImpl implements SpacemapService {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SpaceMapServiceImpl.class);
    ApplicationContext context = NassaContext.INSTANCE;
    private final AbstractBaseEntityStorage<Planet> storage = new SimpleAbstractBaseEntityStorage<>();

    @Override
    public Planet getRandomPlanet() {
        Random randGenerator = new Random();
        List<Planet> planets = storage.getAll();
        return planets.get(randGenerator.nextInt(planets.size()));
    }

    @Override
    public List<Planet> getAllPlanets() {
        return storage.getAll();
    }


    @Override
    public Planet createPlanet(Planet planet) throws NotAbleToBeCreatedException {
        if (isExistsInStorage(planet)) {
            logger.error("Planet {} is already in storage", planet.getName());
            throw new NotAbleToBeCreatedException("Planet with name " + planet.getName() + " is already in storage");
        }
        logger.info("Planet {} was created", planet.getName());
        return storage.create(planet);
    }

    private boolean isExistsInStorage(Planet planet) {
        Optional<Planet> optionalCrewMember = storage.getAll().stream()
                .filter(crewMemberInStorage -> crewMemberInStorage.getId().equals(planet.getId())).findAny();
        return optionalCrewMember.isPresent();
    }


    @Override
    public int getDistanceBetweenPlanets(Planet first, Planet second) {
        Point firstPlanetPoint = first.getLocation();
        Point secondPlanetPoint = second.getLocation();
        int dx = firstPlanetPoint.getX() - secondPlanetPoint.getX();
        int dy = firstPlanetPoint.getY() - secondPlanetPoint.getY();
        int firstX = firstPlanetPoint.getX();
        int firstY = firstPlanetPoint.getY();
        int secondX = secondPlanetPoint.getX();
        int secondY = secondPlanetPoint.getY();
        double distance = 0.0;
        if (dx < 0 && dy > 0) {
            distance = moveRightUp(firstX, firstY, secondX, secondY);
        }
        if (dx < 0 && dy < 0) {
            distance = moveRightDown(firstX, firstY, secondX, secondY);
        }
        if (dx > 0 && dy > 0) {
            distance = moveLeftUp(firstX, firstY, secondX, secondY);
        }
        if (dx > 0 && dy < 0) {
            distance = moveLeftDown(firstX, firstY, secondX, secondY);
        }
        return (int) distance;
    }

    private double moveRightUp(int firstX, int firstY, int secondX, int secondY) {
        double distance = 0.0;
        double result;
        do {
            result = move(firstX, firstY, secondX, secondY);
            if (result == 1) {
                if (firstX == secondX) {
                    firstY -= 1;
                } else
                if (firstY == secondY) {
                    firstX += 1;
                }
            }
            if (result == Math.sqrt(2)) {
                firstX += 1;
                firstY -= 1;
            }
            distance += result;
        } while (result != 0.0);
        return distance;
    }

    private double moveRightDown(int firstX, int firstY, int secondX, int secondY) {
        double distance = 0.0;
        double result;
        do {
            result = move(firstX, firstY, secondX, secondY);
            if (result == 1) {
                if (firstX == secondX) {
                    firstY += 1;
                } else
                if (firstY == secondY) {
                    firstX += 1;
                }
            }
            if (result == Math.sqrt(2)) {
                firstX += 1;
                firstY += 1;
            }
            distance += result;
        } while (result != 0.0);
        return distance;
    }

    private double moveLeftUp(int firstX, int firstY, int secondX, int secondY) {
        double distance = 0.0;
        double result;
        do {
            result = move(firstX, firstY, secondX, secondY);
            if (result == 1) {
                if (firstX == secondX) {
                    firstY -= 1;
                } else
                if (firstY == secondY) {
                    firstX -= 1;
                }
            }
            if (result == Math.sqrt(2)) {
                firstX -= 1;
                firstY -= 1;
            }
            distance += result;
        } while (result != 0.0);
        return distance;
    }

    private double moveLeftDown(int firstX, int firstY, int secondX, int secondY) {
        double distance = 0.0;
        double result;
        do {
            result = move(firstX, firstY, secondX, secondY);
            if (result == 1) {
                if (firstX == secondX) {
                    firstY += 1;
                } else
                if (firstY == secondY) {
                    firstX -= 1;
                }
            }
            if (result == Math.sqrt(2)) {
                firstX -= 1;
                firstY += 1;
            }
            distance += result;
        } while (result != 0.0);
        return distance;
    }


    private double move(int firstX, int firstY, int secondX, int secondY) {
        if (firstX == secondX && firstY == secondY) {
            return 0.0;
        }
        if (firstX == secondX || firstY == secondY) {
            return 1;
        } else {
            return Math.sqrt(2);
        }
    }
}
