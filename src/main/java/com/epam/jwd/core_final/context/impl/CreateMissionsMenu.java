package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeAssignedException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceMapServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceShipServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public enum CreateMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(CreateMissionsMenu.class);
    private final Scanner scanner = new Scanner(System.in);
    private final MissionService missionService = MissionServiceImpl.INSTANCE;
    private final CrewService crewService = CrewServiceImpl.INSTANCE;
    private final SpacemapService spaceMapService = SpaceMapServiceImpl.INSTANCE;
    private final SpaceshipService spaceshipService = SpaceShipServiceImpl.INSTANCE;
    private final EntityFactory<FlightMission> missionFactory = FlightMissionFactory.INSTANCE;

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.print("How many flight missions do you want to create >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("User input {} is being handed", userInput);
        int amountOffFlightMissionsToCreate = Integer.parseInt(userInput);
        int amountOfCreatedFlightMissions = createAllFlightMissions(amountOffFlightMissionsToCreate);
        assignAllSpaceShips();
        assignAllCrewMembers();
        System.out.println("Actual amount of created flight missions: " + amountOfCreatedFlightMissions);
        logger.trace("User input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }

    private void assignAllCrewMembers() {
        List<CrewMember> crewMembers = crewService.findAllCrewMembers();
        for (CrewMember crewMember : crewMembers) {
            try {
                crewService.assignCrewMemberOnMission(crewMember);
            } catch (NotAbleToBeAssignedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private int createAllFlightMissions(int amountToCreate) {
        int amountOfCreatedFlightMissions = 0;
        for (int i = 0; i < amountToCreate; i++) {
            try {
                Planet fromPlanet = spaceMapService.getRandomPlanet();
                Planet toPlanet = spaceMapService.getRandomPlanet();
                FlightMission newMission = missionFactory.create(fromPlanet.getName() + "->" + toPlanet.getName(), fromPlanet, toPlanet);
                FlightMission createdMission = missionService.createMission(newMission);
                logger.info("Flight mission {} was created", createdMission);
                System.out.println("Mission " + newMission.getMissionsName() + " was created");
                amountOfCreatedFlightMissions++;
            } catch (NotAbleToBeCreatedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return amountOfCreatedFlightMissions;
    }

    private void assignAllSpaceShips() {
        List<Spaceship> spaceships = spaceshipService.findAllSpaceships();
        for (Spaceship spaceship : spaceships) {
            try {
                spaceshipService.assignSpaceshipOnMission(spaceship);
            } catch (NotAbleToBeAssignedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
