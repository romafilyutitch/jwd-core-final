package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
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
    private final MissionService missionService = MissionServiceImpl.INSTANCE;
    private final Scanner scanner = new Scanner(System.in);

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
        if(!checkIfNumber(userInput)) {
            System.out.println("Invalid number. Try again.");
            return MainMenu.INSTANCE;
        }
        logger.trace("user input {} is being handled", userInput);
        long years = Long.parseLong(userInput);
        List<FlightMission> allMissions = missionService.findAllMissions();
        if (allMissions.isEmpty()) {
            System.out.println("There is no flight mission in storage. Try to create and the start missions");
            return MainMenu.INSTANCE;
        }
        allMissions.forEach(mission -> {
            MissionResult startOfSimulationResult = mission.getMissionResult();
            MissionResult endOfSimulationResult = missionService.simulate(mission, years).getMissionResult();
            if (startOfSimulationResult == MissionResult.IN_PROGRESS && endOfSimulationResult == MissionResult.COMPLETED) {
                System.out.println("mission " + mission.getMissionsName() + " has reached destination planet ");
            } else if (startOfSimulationResult == MissionResult.IN_PROGRESS && endOfSimulationResult == MissionResult.FAILED) {
                System.out.println("mission " + mission.getMissionsName() + "has faild");
            }
        });
        logger.trace("user input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }

    private boolean checkIfNumber(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
