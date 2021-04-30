package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.service.impl.SpaceMapServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum PlanetsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(PlanetsMenu.class);
    private final Scanner scanner = new Scanner(System.in);
    private final List<String> commands = new ArrayList<>();
    private final SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;

    PlanetsMenu() {
        commands.add("printAll");
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.println("List of options with planet service");
        System.out.println(commands);
        System.out.print("Enter command >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled", userInput);
        if (userInput.equals("printAll")) {
            printAllPlanets();
        } else {
            System.out.println("There is no such command. Try again");
            return this;
        }
        logger.trace("user input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }

    private void printAllPlanets() {
        List<Planet> planets = spacemapService.getAllPlanets();
        if (planets.isEmpty()) {
            System.out.println("No planets was found");
        } else {
            planets.forEach(System.out::println);
            System.out.println(planets.size() + " planets was found");
        }
    }
}
