package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceMapServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public enum FlightMissionsMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(FlightMissionsMenu.class);
    private final MissionService missionService = MissionServiceImpl.INSTANCE;
    private final SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;
    private final List<String> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private FlightMissionCriteria.Builder builder = new FlightMissionCriteria.Builder();

    FlightMissionsMenu() {
        commands.add("back");
        commands.add("printAll");
        commands.add("printAllCriteria");
        commands.add("printAllRandomCriteria");
        commands.add("printOneCriteria");
        commands.add("update");
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return NassaContext.INSTANCE;
    }

    @Override
    public String printAvailableOptions() {
        System.out.println("List of options with flight missions service");
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
                printAllFlightMissions();
                break;
            case "printAllCriteria":
                printAllFlightMissionsByCriteria();
                break;
            case "printAllRandomCriteria":
                printAllFlightMissionsByRandomCriteria();
                break;
            case "printOneCriteria":
                printOneFlightMissionByCriteria();
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

    private void printAllFlightMissionsByRandomCriteria() {
        FlightMissionCriteria randomCriteria = builder.setRandomFindCriteria().build();
        builder = new FlightMissionCriteria.Builder();
        List<FlightMission> allFlightMissionsByCriteria = missionService.findAllMissionsByCriteria(randomCriteria);
        System.out.println("All flight missions found by random criteria " + randomCriteria);
        if (allFlightMissionsByCriteria.isEmpty()) {
            System.out.println("No flight missions was found");
        } else {
            allFlightMissionsByCriteria.forEach(System.out::println);
            System.out.println(allFlightMissionsByCriteria.size() + " flight missions was found");
        }
    }

    private void update() {
        FlightMission flightMission = makeUpdateObject();
        FlightMission updateFlightMission = missionService.updateFlightMissionDetails(flightMission);
        System.out.println("Flight mission " + updateFlightMission.getMissionsName() + " was updated");
    }

    private FlightMission makeUpdateObject() {
        System.out.println("Unknown planet will be replaced random planet");
        System.out.print("Enter name of mission's name to update >>");
        String name = scanner.next();
        System.out.print("Enter from planet name >>");
        String fromPlanetName = scanner.next();
        Optional<Planet> planetFromOptional = NassaContext.INSTANCE.retrieveBaseEntityList(Planet.class).stream().filter(planet -> planet.getName().equals(fromPlanetName)).findAny();
        Planet fromPlanet = planetFromOptional.orElse(SpaceMapServiceImpl.INSTANCE.getRandomPlanet());
        System.out.print("Enter to planet name >>");
        String toPlanetName = scanner.next();
        Optional<Planet> planetToOptional = NassaContext.INSTANCE.retrieveBaseEntityList(Planet.class).stream().filter(planet -> planet.getName().equals(toPlanetName)).findAny();
        Planet toPlanet = planetToOptional.orElse(SpaceMapServiceImpl.INSTANCE.getRandomPlanet());
        EntityFactory<FlightMission> flightMissionFactory = FlightMissionFactory.INSTANCE;
        return flightMissionFactory.create(name, fromPlanet, toPlanet);
    }

    private void printAllFlightMissions() {
        System.out.println("All flight missions");
        List<FlightMission> allMissions = missionService.findAllMissions();
        if (allMissions.isEmpty()) {
            System.out.println("No flight missions was found");
        } else {
            allMissions.forEach(System.out::println);
            System.out.println(allMissions.size() + " flight missions was found");
        }
    }

    private void printAllFlightMissionsByCriteria() {
        List<FlightMission> allFlightMissionsByCriteria = getAllFlightMissionsByCriteria();
        if (allFlightMissionsByCriteria.isEmpty()) {
            System.out.println("No flight missions was found");
        } else {
            allFlightMissionsByCriteria.forEach(System.out::println);
            System.out.println(allFlightMissionsByCriteria.size() + " flight missions was found");
        }
    }

    private void printOneFlightMissionByCriteria() {
        getAllFlightMissionsByCriteria().stream()
                .findAny()
                .ifPresentOrElse(System.out::println, () -> System.out.println("There is no such flight mission in storage"));
    }

    private List<FlightMission> getAllFlightMissionsByCriteria() {
        System.out.println("List of criteria names");
        System.out.println(builder.getListOfCriteriaNames());
        System.out.print("Enter one of criteria >>");
        String criteria = scanner.next();
        switch (criteria) {
            case "idEquals":
                idEquals(builder);
                break;
            case "nameEquals":
                nameEquals(builder);
                break;
            case "missionsNameEquals":
                missionsNameEquals(builder);
                break;
            case "startDateEquals":
                startDateEquals(builder);
                break;
            case "endDateEquals":
                endDateEquals(builder);
                break;
            case "missionResultIs":
                missionResultIs(builder);
                break;
            case "toPlanetNameIs":
                toPlanetNameIs(builder);
                break;
            case "fromPlanetNameIs":
                fromPlanetNameIs(builder);
                break;
            default:
                System.out.println("There is no such criteria. Result without criteria will be printed");
        }
        FlightMissionCriteria criteriaResult = builder.build();
        builder = new FlightMissionCriteria.Builder();
        return missionService.findAllMissionsByCriteria(criteriaResult);
    }

    private void missionsNameEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter mission's name >>");
        String input = scanner.next();
        builder.missionsNameEquals(input);
    }

    private void idEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter id equals >>");
        if (scanner.hasNextInt()) {
            long input = scanner.nextInt();
            builder.idEquals(input);
        } else {
            System.out.println("Invalid id input. Result without criteria will be printed");
        }
    }

    private void nameEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter name >>");
        String input = scanner.next();
        builder.nameEquals(input);
    }

    private void startDateEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter start date (yyyy-mm-dd) >>");
        String[] startDateSplit = scanner.useDelimiter("\n").next().split("-");
        if (startDateSplit.length != 3) {
            System.out.println("Invalid start date input. Result without criteria will be printed");
            return;
        }
        if (checkIfNumbers(startDateSplit)) {
            int year = Integer.parseInt(startDateSplit[0]);
            int month = Integer.parseInt(startDateSplit[1]);
            int day = Integer.parseInt(startDateSplit[2]);
            LocalDate date = LocalDate.of(year, month, day);
            builder.startDateEquals(date);
        } else {
            System.out.println("Invalid start date input. Result without criteria will be printed");
        }
    }

    private void endDateEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter end date (yyyy-mm-dd) >>");
        String[] endDateSplit = scanner.useDelimiter("\n").next().split("-");
        if (endDateSplit.length != 3) {
            System.out.println("Invalid end date input. Result without criteria will be printed");
            return;
        }
        if (checkIfNumbers(endDateSplit)) {
            int year = Integer.parseInt(endDateSplit[0]);
            int month = Integer.parseInt(endDateSplit[1]);
            int day = Integer.parseInt(endDateSplit[2]);
            LocalDate endDate = LocalDate.of(year, month, day);
            builder.endDateEquals(endDate);
        } else {
            System.out.println("Invalid end date input. Result without criteria will be printed");
        }
    }

    private boolean checkIfNumbers(String[] checkedNumbers) {
        for (String checkedNumber : checkedNumbers) {
            if (!checkIfNumber(checkedNumber)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfNumber(String number) {
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void missionResultIs(FlightMissionCriteria.Builder builder) {
        System.out.println("List of mission results");
        Arrays.stream(MissionResult.values()).forEach(System.out::println);
        System.out.print("Enter mission result >>");
        String input = scanner.next();
        if (checkIfMissionResultExists(input)) {
            MissionResult result = MissionResult.valueOf(input);
            builder.missionResultIs(result);
        } else {
            System.out.println("Invalid mission result input. Result without criteria will be printed");
        }
    }

    private boolean checkIfMissionResultExists(String input) {
        return Arrays.stream(MissionResult.values()).anyMatch(missionResult -> missionResult.name().equals(input));
    }

    private void toPlanetNameIs(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter to Planet name >>");
        String input = scanner.next();
        if (checkIfPlanetExists(input)) {
            builder.toPlanetNameIs(input);
        } else {
            System.out.println("There is no such planet. Result without criteria will be printed");
        }
    }

    private void fromPlanetNameIs(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter from planet name >>");
        String input = scanner.next();
        if (checkIfPlanetExists(input)) {
            builder.fromPlanetNameIs(input);
        } else {
            System.out.println("There is no such planet. Result without criteria will be printed");
        }
    }

    private boolean checkIfPlanetExists(String input) {
        return spacemapService.getAllPlanets().stream().anyMatch(planet -> planet.getName().equals(input));
    }

}
