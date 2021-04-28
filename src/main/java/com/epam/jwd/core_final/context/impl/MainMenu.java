package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public enum MainMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(MainMenu.class);
    private final List<String> availableOptions = new ArrayList<>();
    private final Map<String, ApplicationMenu> commandMenuMap = new LinkedHashMap<>();

    MainMenu() {
        commandMenuMap.put("exit", null);
        commandMenuMap.put("members", MembersMenu.INSTANCE);
        commandMenuMap.put("spaceships", SpaceshipsMenu.INSTANCE);
        commandMenuMap.put("missions", FlightMissionsMenu.INSTANCE);
        commandMenuMap.put("planets", PlanetsMenu.INSTANCE);
        commandMenuMap.put("createMissions", CreateMissionsMenu.INSTANCE);
        commandMenuMap.put("startAll", StartMissionsMenu.INSTANCE);
        commandMenuMap.put("simulateAll", SimulateMissionsMenu.INSTANCE);
        commandMenuMap.put("saveAll", SaveMissionsMenu.INSTANCE);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.println("Spaceship missions simulator main menu");
        Scanner scanner = new Scanner(System.in);
        System.out.println(commandMenuMap.keySet());
        System.out.print("Enter command >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled", userInput);
        if (commandMenuMap.containsKey(userInput)) {
            return commandMenuMap.get(userInput);
        } else {
            System.out.println("There is no such command. Try again");
            logger.trace("user input {} was handled", userInput);
            return this;
        }
    }
}
