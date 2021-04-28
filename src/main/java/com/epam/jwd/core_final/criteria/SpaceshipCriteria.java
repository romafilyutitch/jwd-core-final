package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Spaceship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {
    private Long flightDistanceEquals;

    public boolean matches(Spaceship spaceship) {
        return super.matches(spaceship) && checkIfFlightDistanceEquals(spaceship);
    }

    @Override
    public List<String> getListOfCriteriaNames() {
        List<String> criteriaNames = new ArrayList<>(super.getListOfCriteriaNames());
        criteriaNames.add("flightDistanceEquals");
        return criteriaNames;
    }

    private boolean checkIfFlightDistanceEquals(Spaceship spaceship) {
        return flightDistanceEquals == null || spaceship.getFlightDistance().equals(flightDistanceEquals);
    }


    @Override
    public String toString() {
        return "SpaceshipCriteria{" +
                "flightDistanceEquals=" + flightDistanceEquals +
                "} " + super.toString();
    }

    public static class Builder {
        public static final int SPACESHIPS_RANDOM_ID_RANGE = 40;
        public static final int SPACESHIP_RANDOM_FLIGHT_DISTANCE_RANGE = 1_000_000;
        private SpaceshipCriteria product = new SpaceshipCriteria();

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

        public Builder flightDistanceEquals(Long distance) {
            product.flightDistanceEquals = distance;
            return this;
        }

        public Builder setRandomFindCriteria() {
            Random random = new Random();
            idEquals((long) random.nextInt(SPACESHIPS_RANDOM_ID_RANGE));
            flightDistanceEquals((long) random.nextInt(SPACESHIP_RANDOM_FLIGHT_DISTANCE_RANGE));
            return this;
        }

        public SpaceshipCriteria build() {
            return product;
        }


    }

}
