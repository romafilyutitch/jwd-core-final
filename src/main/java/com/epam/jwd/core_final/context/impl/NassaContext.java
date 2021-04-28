package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.PlanetFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceShipServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceMapServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

// todo
public enum NassaContext implements ApplicationContext {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(NassaContext.class);

    // no getters/setters for them
    private Collection<CrewMember> crewMembers = new ArrayList<>();
    private Collection<Spaceship> spaceships = new ArrayList<>();
    private Collection<Planet> planetMap = new ArrayList<>();

    private CrewService crewService = CrewServiceImpl.INSTANCE;
    private SpaceshipService spaceshipService = SpaceShipServiceImpl.INSTANCE;
    private MissionService missionService = MissionServiceImpl.INSTANCE;
    private SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        String name = tClass.getSimpleName();
        switch (name) {
            case "CrewMember" : return (Collection<T>) crewMembers;
            case "Spaceship" : return (Collection<T>) spaceships;
            case "Planet" : return (Collection<T>) planetMap;
            default: throw new IllegalArgumentException("There is no such entity list");
        }
    }

    /**
     * You have to read input files, populate collections
     * @throws InvalidStateException
     */
    @Override
    public void init() throws InvalidStateException {
        logger.trace("Enter in init method");
        try {
            populateCrewMembers();
            populateSpaceShips();
            populatePlanet();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new InvalidStateException(e);
        }
        logger.info("population is completed");
        logger.info("creation of entities in storage");
        createCrewMembers();
        createSpaceships();
        createPlanets();
        logger.info("creation of entities is completed");
        logger.trace("init is completed");
    }

    private void createPlanets() {
        for (Planet planet : planetMap) {
            try {
                spacemapService.createPlanet(planet);
            } catch (NotAbleToBeCreatedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void createCrewMembers() {
        for (CrewMember crewMember : crewMembers) {
            try {
                crewService.createCrewMember(crewMember);
            } catch (NotAbleToBeCreatedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void createSpaceships() {
        for (Spaceship spaceship : spaceships) {
            try {
                spaceshipService.createSpaceship(spaceship);
            } catch (NotAbleToBeCreatedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void populateCrewMembers() throws IOException {
        CrewMemberFactory crewMemberFactory = new CrewMemberFactory();
        ApplicationProperties properties = ApplicationProperties.INSTANCE;
        String crewFilePath = properties.getInputRootDir() + File.separator + properties.getCrewFileName();
        try (BufferedReader reader = new BufferedReader(new FileReader(crewFilePath))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                if (nextLine.startsWith("#")) {
                    continue;
                }
                String[] entries = nextLine.split(";");
                for (String entry : entries) {
                    Scanner scanner = new Scanner(entry);
                    scanner.useDelimiter(",");
                    String role = scanner.next();
                    String name = scanner.next();
                    String rank = scanner.next();
                    CrewMember createdInstnace = crewMemberFactory.create(role, name, rank);
                    crewMembers.add(createdInstnace);
                }
            }
        }
    }

    private void populateSpaceShips() throws IOException {
        SpaceshipFactory spaceshipFactory = new SpaceshipFactory();
        ApplicationProperties properties = ApplicationProperties.INSTANCE;
        String spaceshipsFileName = properties.getInputRootDir() + File.separator + properties.getSpaceshipsFileName();
        try (BufferedReader reader = new BufferedReader(new FileReader(spaceshipsFileName))) {
            String nextLine;
            while((nextLine = reader.readLine()) != null) {
                if (nextLine.startsWith("#")) {
                    continue;
                }
                String[] splitSpaceshipInputData = nextLine.split(";");
                String spaceName = splitSpaceshipInputData[0];
                String distance = splitSpaceshipInputData[1];
                String roleMap = splitSpaceshipInputData[2];
                roleMap = roleMap.replace("{","");
                roleMap = roleMap.replace("}","");
                Spaceship createdInstance = spaceshipFactory.create(spaceName, distance, roleMap);
                spaceships.add(createdInstance);
            }
        }
    }

    private void populatePlanet() throws IOException {
        PlanetFactory planetFactory = new PlanetFactory();
        ApplicationProperties properties = ApplicationProperties.INSTANCE;
        String planetFileName = properties.getInputRootDir() + File.separator + properties.getSpaceMapFileName();
        try (BufferedReader reader = new BufferedReader(new FileReader(planetFileName))) {
            String nextLine = reader.readLine();
            for (int i = 1; nextLine != null; nextLine = reader.readLine(), i++) {
                String[] splitPlanetMap = nextLine.split(",");
                for (int j = 0; j < splitPlanetMap.length; j++) {
                    if (!splitPlanetMap[j].equals("null")) {
                        Planet newInstance = planetFactory.create(splitPlanetMap[j], j + 1, i);
                        planetMap.add(newInstance);
                        break;
                    }
                }
            }
        }
    }
}
