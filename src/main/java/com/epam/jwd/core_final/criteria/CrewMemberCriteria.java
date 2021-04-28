package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private Role roleIs;
    private Rank rankIs;

    @Override
    public boolean matches(CrewMember crewMember) {
        return super.matches(crewMember) &&
                checkIfRankIs(crewMember) &&
                checkIfRoleIs(crewMember);
    }

    @Override
    public List<String> getListOfCriteriaNames() {
        List<String> criteriaNames = new ArrayList<>(super.getListOfCriteriaNames());
        criteriaNames.add("roleIs");
        criteriaNames.add("rankIs");
        return criteriaNames;
    }

    private boolean checkIfRoleIs(CrewMember crewMember) {
        return roleIs == null || roleIs == crewMember.getRole();
    }

    private boolean checkIfRankIs(CrewMember crewMember) {
        return rankIs == null || rankIs == crewMember.getRank();
    }


    public static class Builder {
        private CrewMemberCriteria product = new CrewMemberCriteria();

        public List<String> getListOfCriteriaNames() {
            return product.getListOfCriteriaNames();
        }

        public Builder idEquals(Long id) {
            product.idEquals = id;
            return this;
        }

        public Builder nameEquals(String name) {
            product.nameEquals = name;
            return this;
        }

        public Builder roleIs(Role role) {
            product.roleIs = role;
            return this;
        }

        public Builder rankIs(Rank rank) {
            product.rankIs = rank;
            return this;
        }

        public Builder setRandomCriteria() {
            Random random = new Random();
            Role[] roles = Role.values();
            Rank[] ranks = Rank.values();
            roleIs(roles[random.nextInt(roles.length)]);
            rankIs(ranks[random.nextInt(ranks.length)]);
            return this;
        }

        public CrewMemberCriteria build() {
            return product;
        }
    }
}
