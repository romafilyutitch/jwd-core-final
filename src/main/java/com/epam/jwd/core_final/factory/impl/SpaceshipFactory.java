package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public enum SpaceshipFactory implements EntityFactory<Spaceship> {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(SpaceshipFactory.class);

    @Override
    public Spaceship create(Object... args) {
        logger.trace("Creation of Spaceship object");
        checkArgsLength(args);
        String spaceshipName = String.valueOf(args[0]);
        Long spaceshipDistance = Long.valueOf(String.valueOf(args[1]));
        Map<Role, Short> roleMap = makeRoleIdAndCountMap(args[2]);
        logger.trace("Creation was completed");
        return new Spaceship(spaceshipName, roleMap, spaceshipDistance);
    }

    private Map<Role, Short> makeRoleIdAndCountMap(Object argument) {
        Map<Role, Short> roleIdAndCountMap = new HashMap<>();
        String rolesMapString = String.valueOf(argument);
        Scanner scan = new Scanner(rolesMapString);
        scan.useDelimiter(",");
        while (scan.hasNext()) {
            String roleIdAndCount = scan.next();
            String[] splitRoleIdAndCount = roleIdAndCount.split(":");
            Role role = Role.resolveRoleById(Integer.parseInt(splitRoleIdAndCount[0]));
            short count = Short.parseShort(splitRoleIdAndCount[1]);
            roleIdAndCountMap.put(role, count);
        }
        return roleIdAndCountMap;
    }

    private void checkArgsLength(Object... args) {
        if (args.length != 3) {
            logger.error("Passed illegal objects array");
            throw new IllegalArgumentException("args length mus be 3");
        }
    }
}
