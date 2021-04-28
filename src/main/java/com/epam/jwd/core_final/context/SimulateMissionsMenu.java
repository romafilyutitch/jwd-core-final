package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public enum SimulateMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SimulateMissionsMenu.class);

    MissionService missionService = MissionServiceImpl.INSTANCE;
    Scanner scanner = new Scanner(System.in);

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.print("Enter amount of years to simulate flightMissions >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled", userInput);
        long years = Long.parseLong(userInput);
        List<FlightMission> allMissions = missionService.findAllMissions();
        allMissions.forEach(mission -> {
            MissionResult startOfSimulationResult = mission.getMissionResult();
            MissionResult endOfSimulationResult = missionService.simulate(mission, years).getMissionResult();
            if (startOfSimulationResult == MissionResult.IN_PROGRESS && endOfSimulationResult == MissionResult.COMPLETED) {
                System.out.println("mission " + mission.getMissionsName() + " has reached destination planet ");
            }
        });
        logger.trace("user input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }
}
