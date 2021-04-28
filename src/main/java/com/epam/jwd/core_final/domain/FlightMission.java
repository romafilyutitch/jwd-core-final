package com.epam.jwd.core_final.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * missions name {@link String}
 * start date {@link java.time.LocalDate}
 * end date {@link java.time.LocalDate}
 * distance {@link Long} - missions distance
 * assignedSpaceShift {@link Spaceship} - not defined by default
 * assignedCrew {@link java.util.List<CrewMember>} - list of missions members based on ship capacity - not defined by default
 * missionResult {@link MissionResult}
 * from {@link Planet}
 * to {@link Planet}
 */
public class FlightMission extends AbstractBaseEntity {
    private String missionsName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Spaceship assignedSpaceShift;
    private Long distance;
    private List<CrewMember> assignedCrew = new ArrayList<>();
    private MissionResult missionResult = MissionResult.PLANNED;
    private Planet from;
    private Planet to;

    public FlightMission(String name, String missionsName, Planet from, Planet to) {
        super(name);
        this.missionsName = missionsName;
        this.from = from;
        this.to = to;
    }

    public String getMissionsName() {
        return missionsName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Spaceship getAssignedSpaceShift() {
        return assignedSpaceShift;
    }

    public Long getDistance() {
        return distance;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public Planet getFrom() {
        return from;
    }

    public Planet getTo() {
        return to;
    }

    public void setMissionsName(String missionsName) {
        this.missionsName = missionsName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setAssignedSpaceShift(Spaceship assignedSpaceShift) {
        this.assignedSpaceShift = assignedSpaceShift;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public void setAssignedCrew(List<CrewMember> assignedCrew) {
        this.assignedCrew = assignedCrew;
    }

    public void setMissionResult(MissionResult missionResult) {
        if (missionResult == MissionResult.FAILED) {
            assignedSpaceShift.setReadyForNextMissions(false);
            assignedCrew.forEach(member -> member.setReadyForNextMissions(false));
        }
        this.missionResult = missionResult;
    }

    public void setFrom(Planet from) {
        this.from = from;
    }

    public void setTo(Planet to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlightMission mission = (FlightMission) o;
        return Objects.equals(missionsName, mission.missionsName) && Objects.equals(startDate, mission.startDate) && Objects.equals(endDate, mission.endDate) && Objects.equals(assignedSpaceShift, mission.assignedSpaceShift) && Objects.equals(distance, mission.distance) && Objects.equals(assignedCrew, mission.assignedCrew) && missionResult == mission.missionResult && Objects.equals(from, mission.from) && Objects.equals(to, mission.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), missionsName, startDate, endDate, assignedSpaceShift, distance, assignedCrew, missionResult, from, to);
    }

    @Override
    public String toString() {
        String startDateString = startDate == null ? "null" : DateTimeFormatter.ofPattern(ApplicationProperties.INSTANCE.getDateTimeFormat()).format(startDate.atStartOfDay());
        String endDateString = endDate == null ? "null" : DateTimeFormatter.ofPattern(ApplicationProperties.INSTANCE.getDateTimeFormat()).format(endDate.atStartOfDay());
        return "FlightMission{" +
                "id = " + getId() +
                ", name = " + getName() +
                ", missionsName='" + missionsName + '\'' +
                ", startDate=" + startDateString +
                ", endDate=" + endDateString +
                ", distance=" + distance +
                ", assignedSpaceShift=" + assignedSpaceShift +
                ", assignedCrew=" + assignedCrew +
                ", missionResult=" + missionResult;
    }
}
