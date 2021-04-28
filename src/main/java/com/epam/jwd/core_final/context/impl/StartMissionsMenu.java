package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.NotAbleToBeStartedException;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public enum StartMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(StartMissionsMenu.class);
    private final MissionService missionService = MissionServiceImpl.INSTANCE;

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        return null;
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled", userInput);
        List<FlightMission> allMissions = missionService.findAllMissions();
        if (allMissions.isEmpty()) {
            System.out.println("There is no flight missions in storage. Try to create flight missions at first");
            return MainMenu.INSTANCE;
        }
        for (FlightMission mission : allMissions) {
            try {
                FlightMission startedMission = missionService.startMission(mission);
                System.out.println(startedMission.getName() + " " + startedMission.getMissionsName() + " was started");
            } catch (NotAbleToBeStartedException e) {
                logger.error(e.getMessage(), e);
                System.out.println(mission.getMissionsName() + "was not started");
            }
        }
        logger.trace("user input {} was started", userInput);
        return MainMenu.INSTANCE;
    }
}
