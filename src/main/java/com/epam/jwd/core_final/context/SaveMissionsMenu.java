package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public enum SaveMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SaveMissionsMenu.class);
    MissionService missionService = MissionServiceImpl.INSTANCE;
    ApplicationProperties properties = ApplicationProperties.INSTANCE;
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.println("All existing space mission will be saved in file mission's file");
        return null;
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("saving state of all flight missions in JSON file");
        File outputDir = new File(properties.getOutputRootDir());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        String filePath = properties.getOutputRootDir() + File.separator + properties.getMissionsFileName();
        List<FlightMission> allMissions = missionService.findAllMissions();
        StringBuilder builder = new StringBuilder();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            for (FlightMission mission : allMissions) {
                builder.append(mapper.writeValueAsString(mission)).append("\n");
            }
            writer.println(builder.toString());
            logger.info("all missions was saved");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.out.println("There was exception in process of saving missions");
            return MainMenu.INSTANCE;
        }
        logger.trace("saving was completed");
        System.out.println("Missions was saved in JSON file");
        return MainMenu.INSTANCE;
    }
}
