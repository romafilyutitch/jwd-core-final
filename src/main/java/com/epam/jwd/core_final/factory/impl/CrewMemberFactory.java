package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// do the same for other entities
public enum CrewMemberFactory implements EntityFactory<CrewMember> {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(CrewMemberFactory.class);

    @Override
    public CrewMember create(Object... args) {
        logger.trace("creation of CrewMember object");
        checkArgsLength(args);
        Role role = Role.resolveRoleById(Integer.parseInt(String.valueOf(args[0])));
        String name = String.valueOf(args[1]);
        Rank rank = Rank.resolveRankById(Integer.parseInt(String.valueOf(args[2])));
        logger.trace("creation of CrewMember object was completed");
        return new CrewMember(name, role, rank);
    }

    private void checkArgsLength(Object... args) {
        if (args.length != 3) {
            logger.error("passed illegal array of objects");
            throw new IllegalArgumentException("args length must be 3");
        }
    }
}
