package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
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
    private  CrewMemberCriteria.Builder builder = new CrewMemberCriteria.Builder();

    MembersMenu() {
        commands.add("back");
        commands.add("printAll");
        commands.add("printAllCriteria");
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
            case "back" : return MainMenu.INSTANCE;
            case "printAll" : printAllCrewMembers(); break;
            case "printAllCriteria" : printAllCrewMembersByCriteria(); break;
            case "printOneCriteria" : printOneMemberByCriteria(); break;
            case "update" : update(); break;
            default:
                System.out.println("There is no such command. Try again");
                return this;
        }
        logger.trace("user input {} was handled", userInput);
        return MainMenu.INSTANCE;
    }

    private void update() {
        CrewMember crewMember = makeUpdateObject();
        crewService.updateCrewMemberDetails(crewMember);
    }

    private CrewMember makeUpdateObject() {
        System.out.println("Enter name of crew member to update");
        String name = scanner.useDelimiter("\n").next();
        Role[] roles = Role.values();
        for (Role role : roles) {
            System.out.println(role.getId() + ". " + role.getName());
        }
        System.out.println("Enter new role of crew member: ");
        String roleName = scanner.next();
        Rank[] ranks = Rank.values();
        for (Rank rank : ranks) {
            System.out.println(rank.getId() +". " + rank.getName());
        }
        System.out.println("Enter new rank of crew member: ");
        String rankName = scanner.next();
        EntityFactory<CrewMember> factory = new CrewMemberFactory();
        return factory.create(roleName, name, rankName);
    }

    private void printAllCrewMembers() {
        System.out.println("All crew members ");
        crewService.findAllCrewMembers().forEach(System.out::println);
    }

    private void printAllCrewMembersByCriteria() {
        getAllCrewMembersByCriteria().forEach(System.out::println);
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
            case "nameEquals" : nameEquals(builder); break;
            case "idEquals" : idEquals(builder); break;
            case "roleIs" : roleIs(builder); break;
            case "rankIs" : rankIs(builder); break;
            default:
                System.out.println("There is no such criteria");
        }
        CrewMemberCriteria criteriaResult = builder.build();
        builder = new CrewMemberCriteria.Builder();
        return crewService.findAllCrewMembersByCriteria(criteriaResult);
    }

    private void nameEquals(CrewMemberCriteria.Builder builder) {
        System.out.print("Enter name : ");
        String input = scanner.next();
        builder.nameEquals(input);
    }

    private void idEquals(CrewMemberCriteria.Builder builder) {
        System.out.print("Enter id equals : ");
        long input = scanner.nextInt();
        builder.idEquals(input);
    }

    private void roleIs(CrewMemberCriteria.Builder builder) {
        Role[] roles = Role.values();
        Arrays.stream(roles).forEach(System.out::println);
        System.out.println("Enter role is : ");
        String input = scanner.next();
        builder.roleIs(Role.valueOf(input));
    }

    private void rankIs(CrewMemberCriteria.Builder builder) {
        Rank[] ranks = Rank.values();
        Arrays.stream(ranks).forEach(System.out::println);
        System.out.println("Enter rank is : ");
        String input = scanner.next();
        builder.rankIs(Rank.valueOf(input));
    }
}
