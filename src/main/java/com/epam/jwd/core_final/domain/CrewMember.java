package com.epam.jwd.core_final.domain;

import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    private Role role;
    private Rank rank;
    private Boolean isReadyForNextMissions = true;
    private Boolean isAssignedToFlightMission = false;

    public CrewMember(String name, Role role, Rank rank) {
        super(name);
        this.role = role;
        this.rank = rank;
    }

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }

    public void setAssignedToFlightMission(Boolean assignedToFlightMission) {
        isAssignedToFlightMission = assignedToFlightMission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CrewMember that = (CrewMember) o;
        return role == that.role && rank == that.rank && Objects.equals(isReadyForNextMissions, that.isReadyForNextMissions) && Objects.equals(isAssignedToFlightMission, that.isAssignedToFlightMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), role, rank, isReadyForNextMissions, isAssignedToFlightMission);
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                "id=" + getId() +
                ", name = " + getName() +
                ", role=" + role +
                ", rank=" + rank +
                ", isReadyForNextMissions=" + isReadyForNextMissions +
                ", isAssignedToFlightMission=" + isAssignedToFlightMission +
                '}';
    }
}
