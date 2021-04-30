package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.service.impl.SpaceShipServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum SpaceshipsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SpaceshipsMenu.class);
    private final SpaceshipService spaceshipService = SpaceShipServiceImpl.INSTANCE;
    private final List<String> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private SpaceshipCriteria.Builder builder = new SpaceshipCriteria.Builder();

    SpaceshipsMenu() {
        commands.add("back");
        commands.add("printAll");
        commands.add("printAllCriteria");
        commands.add("printAllCriteriaRandom");
        commands.add("printOneCriteria");
        commands.add("update");
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.println("List of options with spaceship service");
        System.out.println(commands);
        System.out.print("Enter command >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled", userInput);
        switch (userInput) {
            case "back":
                return MainMenu.INSTANCE;
            case "printAll":
                printAllSpaceships();
                break;
            case "printAllCriteria":
                printAllSpaceshipsByCriteria();
                break;
            case "printAllCriteriaRandom":
                printAllSpaceshipsByRandomCriteria();
                break;
            case "printOneCriteria":
                printOneSpaceshipByCriteria();
                break;
            case "update":
                update();
                break;
            default:
                System.out.println("There is no such command. Try again");
                return this;
        }
        logger.trace("user input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }

    private void printAllSpaceshipsByRandomCriteria() {
        SpaceshipCriteria randomCriteria = builder.setRandomFindCriteria().build();
        builder = new SpaceshipCriteria.Builder();
        List<Spaceship> allSpaceshipsByCriteria = spaceshipService.findAllSpaceshipsByCriteria(randomCriteria);
        System.out.println("All spaceships found by random criteria " + randomCriteria);
        if (allSpaceshipsByCriteria.isEmpty()) {
            System.out.println("No spaceships was found");
        } else {
            allSpaceshipsByCriteria.forEach(System.out::println);
            System.out.println(allSpaceshipsByCriteria.size() + " spaceships was found");
        }
    }

    private void update() {
        Spaceship spaceship = makeUpdateObject();
        Spaceship updatedSpaceship = spaceshipService.updateSpaceshipDetails(spaceship);
        System.out.println("Spaceship " + updatedSpaceship.getName() + " was updated");
    }

    private Spaceship makeUpdateObject() {
        System.out.print("Enter name of spaceship to update >>");
        String name = scanner.useDelimiter("\n").next();
        System.out.print("Enter distance of spaceship to update >>");
        long distance = scanner.nextInt();
        EntityFactory<Spaceship> factory = SpaceshipFactory.INSTANCE;
        return factory.create(name, distance, "");
    }

    private void printAllSpaceships() {
        System.out.println("All spaceships");
        spaceshipService.findAllSpaceships().forEach(System.out::println);
        System.out.println(spaceshipService.findAllSpaceships().size() + " spaceships was found");
    }

    private void printAllSpaceshipsByCriteria() {
        List<Spaceship> allSpaceshipsByCriteria = getAllSpaceshipsByCriteria();
        if (allSpaceshipsByCriteria.isEmpty()) {
            System.out.println("No spaceships was found");
        } else {
            allSpaceshipsByCriteria.forEach(System.out::println);
            System.out.println(allSpaceshipsByCriteria.size() + " spaceships was found");
        }
    }

    private void printOneSpaceshipByCriteria() {
        System.out.println("One of spaceships found by criteria");
        getAllSpaceshipsByCriteria().stream()
                .findAny()
                .ifPresentOrElse(System.out::println, () -> System.out.println("There is no such spaceship in storage"));
    }

    private List<Spaceship> getAllSpaceshipsByCriteria() {
        System.out.println("List of criteria names");
        System.out.println(builder.getListOfCriteriaNames());
        System.out.print("Enter one of criteria >>");
        String criteria = scanner.next();
        switch (criteria) {
            case "nameEquals":
                nameEquals(builder);
                break;
            case "idEquals":
                idEquals(builder);
                break;
            case "flightDistanceEquals":
                flightDistanceEquals(builder);
                break;
            default:
                System.out.println("There is no such criteria. Result without criteria will be printed");
        }
        SpaceshipCriteria criteriaResult = builder.build();
        builder = new SpaceshipCriteria.Builder();
        return spaceshipService.findAllSpaceshipsByCriteria(criteriaResult);
    }

    private void nameEquals(SpaceshipCriteria.Builder builder) {
        System.out.print("Enter name >>");
        String input = scanner.next();
        builder.nameEquals(input);
    }

    private void idEquals(SpaceshipCriteria.Builder builder) {
        System.out.print("Enter id equals >>");
        if (scanner.hasNextInt()) {
            long input = scanner.nextInt();
            builder.idEquals(input);
        } else {
            System.out.println("Invalid id input. Result without criteria will be printed");
        }
    }

    private void flightDistanceEquals(SpaceshipCriteria.Builder builder) {
        System.out.print("Enter flight distance equals >>");
        if (scanner.hasNextInt()) {
            long input = scanner.nextInt();
            builder.flightDistanceEquals(input);
        } else {
            System.out.println("Invalid flight distance input. Result without criteria will be printed");
        }
    }
}
