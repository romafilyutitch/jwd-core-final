package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.context.ApplicationMenu;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public enum MembersMenu implements ApplicationMenu {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(MembersMenu.class);
    private final CrewService crewService = CrewServiceImpl.INSTANCE;
    private final List<String> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private CrewMemberCriteria.Builder builder = new CrewMemberCriteria.Builder();

    MembersMenu() {
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
        System.out.println("List of options with crew member service");
        System.out.println(commands);
        System.out.print("Enter command >>");
        return scanner.next();
    }

    @Override
    public ApplicationMenu handleUserInput(String userInput) {
        logger.trace("user input {} is being handled ", userInput);
        switch (userInput) {
            case "back":
                return MainMenu.INSTANCE;
            case "printAll":
                printAllCrewMembers();
                break;
            case "printAllCriteria":
                printAllCrewMembersByCriteria();
                break;
            case "printAllCriteriaRandom":
                printAllCrewMembersByRandomCriteria();
                break;
            case "printOneCriteria":
                printOneMemberByCriteria();
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

    private void printAllCrewMembersByRandomCriteria() {
        CrewMemberCriteria randomCriteria = builder.setRandomCriteria().build();
        builder = new CrewMemberCriteria.Builder();
        List<CrewMember> allCrewMembersByCriteria = crewService.findAllCrewMembersByCriteria(randomCriteria);
        System.out.println("All crew members found by random criteria " + randomCriteria);
        if (allCrewMembersByCriteria.isEmpty()) {
            System.out.println("No crew members was found");
        } else {
            allCrewMembersByCriteria.forEach(System.out::println);
            System.out.println(allCrewMembersByCriteria.size() + " crew members was found");
        }
    }

    private void update() {
        CrewMember crewMember = makeUpdateObject();
        CrewMember updatedCrewMember = crewService.updateCrewMemberDetails(crewMember);
        System.out.println("Crew member : " + updatedCrewMember.getId() + " " + updatedCrewMember.getName() + " was updated");
    }

    private CrewMember makeUpdateObject() {
        System.out.print("Enter name of crew member to update >>");
        String name = scanner.useDelimiter("\n").next();
        printRoles();
        System.out.print("Enter new role number of crew member >>");
        String roleName = scanner.next();
        printRanks();
        System.out.print("Enter new rank number of crew member >>");
        String rankName = scanner.next();
        EntityFactory<CrewMember> factory = CrewMemberFactory.INSTANCE;
        return factory.create(roleName, name, rankName);
    }

    private void printRanks() {
        Arrays.stream(Rank.values()).forEach(rank -> System.out.println(rank.getId() + "." + rank.getName()));
    }

    private void printRoles() {
        Arrays.stream(Role.values()).forEach(role -> System.out.println(role.getId() + "." + role.getName()));
    }

    private void printAllCrewMembers() {
        System.out.println("All crew members ");
        crewService.findAllCrewMembers().forEach(System.out::println);
        System.out.println(crewService.findAllCrewMembers().size() + " crew members was found");
    }

    private void printAllCrewMembersByCriteria() {
        List<CrewMember> allCrewMembersByCriteria = getAllCrewMembersByCriteria();
        if (allCrewMembersByCriteria.isEmpty()) {
            System.out.println("No crew members was found");
        } else {
            allCrewMembersByCriteria.forEach(System.out::println);
            System.out.println(allCrewMembersByCriteria.size() + " crew members was found");
        }
    }

    private void printOneMemberByCriteria() {
        System.out.println("One of crew members found by criteria ");
        getAllCrewMembersByCriteria().stream()
                .findAny()
                .ifPresentOrElse(System.out::println, () -> System.out.println("There is no such crew member in storage"));
    }

    private List<CrewMember> getAllCrewMembersByCriteria() {
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
            case "roleIs":
                roleIs(builder);
                break;
            case "rankIs":
                rankIs(builder);
                break;
            default:
                System.out.println("There is no such criteria. Result without criteria will be printed");
        }
        CrewMemberCriteria criteriaResult = builder.build();
        builder = new CrewMemberCriteria.Builder();
        return crewService.findAllCrewMembersByCriteria(criteriaResult);
    }

    private void nameEquals(CrewMemberCriteria.Builder builder) {
        System.out.print("Enter name >>");
        String input = scanner.next();
        builder.nameEquals(input);
    }

    private void idEquals(CrewMemberCriteria.Builder builder) {
        System.out.print("Enter id equals >>");
        if (scanner.hasNextInt()) {
            long input = scanner.nextInt();
            builder.idEquals(input);
        } else {
            System.out.println("Invalid id input. Result without criteria will be printed");
        }
    }

    private void roleIs(CrewMemberCriteria.Builder builder) {
        printRoles();
        System.out.print("Enter role is >>");
        String input = scanner.next();
        if (checkIfRoleExits(input)) {
            builder.roleIs(Role.valueOf(input));
        } else {
            System.out.println("Invalid role input. Result without criteria will be printed");
        }
    }

    private void rankIs(CrewMemberCriteria.Builder builder) {
        printRanks();
        System.out.print("Enter rank is >>");
        String input = scanner.next();
        if (checkIfRankExists(input)) {
            builder.rankIs(Rank.valueOf(input));
        } else {
            System.out.println("Invalid rank input. Result without criteria will be printed");
        }
    }

    private boolean checkIfRoleExits(String input) {
        return Arrays.stream(Role.values()).anyMatch(role -> role.getName().equals(input));
    }

    private boolean checkIfRankExists(String input) {
        return Arrays.stream(Rank.values()).anyMatch(rank -> rank.getName().equals(input));
    }
}
