package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;
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
        List<FlightMission> allCrewMembersByCriteria = missionService.findAllMissionsByCriteria(randomCriteria);
        System.out.println("All flight missions found by random criteria");
        allCrewMembersByCriteria.forEach(System.out::println);
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
        missionService.findAllMissions().forEach(System.out::println);
    }

    private void printAllFlightMissionsByCriteria() {
        getAllFlightMissionsByCriteria().forEach(System.out::println);
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
                System.out.println("There is no such criteria");
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
        long input = scanner.nextInt();
        builder.idEquals(input);
    }

    private void nameEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter name >>");
        String input = scanner.next();
        builder.nameEquals(input);
    }

    private void startDateEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter start year >> ");
        int year = scanner.nextInt();
        System.out.print("Enter start month >> ");
        int month = scanner.nextInt();
        System.out.print("Enter start day >> ");
        int day = scanner.nextInt();
        LocalDate date = LocalDate.of(year, month, day);
        builder.startDateEquals(date);
    }

    private void endDateEquals(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter end year >>");
        int year = scanner.nextInt();
        System.out.print("Enter end month >>");
        int month = scanner.nextInt();
        System.out.print("Enter end day >>");
        int day = scanner.nextInt();
        LocalDate date = LocalDate.of(year, month, day);
        builder.endDateEquals(date);
    }

    private void missionResultIs(FlightMissionCriteria.Builder builder) {
        System.out.println("List of mission results");
        Arrays.stream(MissionResult.values()).forEach(System.out::println);
        System.out.print("Enter mission result >>");
        String input = scanner.next();
        MissionResult result = MissionResult.valueOf(input);
        builder.missionResultIs(result);
    }

    private void toPlanetNameIs(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter to Planet name >>");
        String input = scanner.next();
        builder.toPlanetNameIs(input);
    }

    private void fromPlanetNameIs(FlightMissionCriteria.Builder builder) {
        System.out.print("Enter from planet name >>");
        String input = scanner.next();
        builder.fromPlanetNameIs(input);
    }

}
