package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.service.SpacemapService;
import com.epam.jwd.core_final.service.impl.SpaceMapServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private String missionsNameEquals;
    private LocalDate startDateEquals;
    private LocalDate endDateEquals;
    private MissionResult missionResultIs;
    private String toPlanetNameIs;
    private String fromPlanetNameIs;

    public boolean matches(FlightMission flightMission) {
        return super.matches(flightMission) &&
                checkIfMissionsNameEquals(flightMission) &&
                checkIfStartDateEquals(flightMission) &&
                checkIfEndDateEquals(flightMission) &&
                checkIfMissionResultIs(flightMission) &&
                checkIfToPlanetNameIs(flightMission) &&
                checkIfFromPlanetNameIs(flightMission);
    }

    @Override
    public String toString() {
        return "FlightMissionCriteria{" +
                "idEquals = " + idEquals +
                ", nameEquals = " + nameEquals +
                ", missionsNameEquals='" + missionsNameEquals + '\'' +
                ", startDateEquals=" + startDateEquals +
                ", endDateEquals=" + endDateEquals +
                ", missionResultIs=" + missionResultIs +
                ", toPlanetNameIs='" + toPlanetNameIs + '\'' +
                ", fromPlanetNameIs='" + fromPlanetNameIs + '\'' +
                "} ";
    }

    @Override
    public List<String> getListOfCriteriaNames() {
        List<String> criteriaNames = new ArrayList<>(super.getListOfCriteriaNames());
        criteriaNames.add("missionsNameEquals");
        criteriaNames.add("startDateEquals");
        criteriaNames.add("endDateEquals");
        criteriaNames.add("missionResultIs");
        criteriaNames.add("toPlanetNameIs");
        criteriaNames.add("fromPlanetNameIs");
        return criteriaNames;
    }

    public boolean checkIfMissionsNameEquals(FlightMission mission) {
        return missionsNameEquals == null || mission.getMissionsName().equals(missionsNameEquals);
    }

    public boolean checkIfStartDateEquals(FlightMission mission) {
        return startDateEquals == null || mission.getStartDate().equals(startDateEquals);
    }


    public boolean checkIfEndDateEquals(FlightMission mission) {
        return endDateEquals == null || mission.getEndDate().equals(endDateEquals);
    }


    public boolean checkIfMissionResultIs(FlightMission mission) {
        return missionResultIs == null || mission.getMissionResult().equals(missionResultIs);
    }

    public boolean checkIfToPlanetNameIs(FlightMission mission) {
        return toPlanetNameIs == null || mission.getTo().getName().equals(toPlanetNameIs);
    }

    public boolean checkIfFromPlanetNameIs(FlightMission mission) {
        return fromPlanetNameIs == null || mission.getFrom().getName().equals(fromPlanetNameIs);
    }

    public static class Builder {
        private final FlightMissionCriteria product = new FlightMissionCriteria();

        public Builder idEquals(Long id) {
            product.idEquals = id;
            return this;
        }

        public List<String> getListOfCriteriaNames() {
            return product.getListOfCriteriaNames();
        }

        public Builder nameEquals(String name) {
            product.nameEquals = name;
            return this;
        }

        public Builder missionsNameEquals(String name) {
            product.missionsNameEquals = name;
            return this;
        }

        public Builder startDateEquals(LocalDate startDate) {
            product.startDateEquals = startDate;
            return this;
        }

        public Builder endDateEquals(LocalDate endDate) {
            product.endDateEquals = endDate;
            return this;
        }

        public Builder missionResultIs(MissionResult result) {
            product.missionResultIs = result;
            return this;
        }

        public Builder toPlanetNameIs(String toPlanet) {
            product.toPlanetNameIs = toPlanet;
            return this;
        }

        public Builder fromPlanetNameIs(String fromPlanet) {
            product.fromPlanetNameIs = fromPlanet;
            return this;
        }

        public Builder setRandomFindCriteria() {
            SpacemapService spacemapService = SpaceMapServiceImpl.INSTANCE;
            fromPlanetNameIs(spacemapService.getRandomPlanet().getName());
            toPlanetNameIs(spacemapService.getRandomPlanet().getName());
            return this;
        }

        public FlightMissionCriteria build() {
            return product;
        }
    }
}
