package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.NotAbleToBeAssignedException;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
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

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public enum CreateMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(CreateMissionsMenu.class);
    Scanner scanner = new Scanner(System.in);
    MissionService missionService = MissionServiceImpl.INSTANCE;
    CrewService crewService = CrewServiceImpl.INSTANCE;
    SpacemapService spaceMapService = SpaceMapServiceImpl.INSTANCE;
    SpaceshipService spaceshipService = SpaceShipServiceImpl.INSTANCE;
    EntityFactory<FlightMission> missionFactory = new FlightMissionFactory();
    EntityFactory<CrewMember> crewMemberFactory = new CrewMemberFactory();
    EntityFactory<Spaceship> spaceshipFactory = new SpaceshipFactory();

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
                missionService.createMission(newMission);
                System.out.println("Mission " + newMission.getMissionsName() + " was created");
                amountOfCreatedFlightMissions++;
            } catch (NotAbleToBeCreatedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return amountOfCreatedFlightMissions;
    }

    private void assignAllSpaceShips() {
        Random random = new Random();
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
