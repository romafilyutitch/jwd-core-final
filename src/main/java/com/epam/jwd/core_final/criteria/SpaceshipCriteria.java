package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;
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

    public static class Builder {
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
            idEquals((long) random.nextInt(40));
            flightDistanceEquals((long) random.nextInt(1_000_000));
            return this;
        }

        public SpaceshipCriteria build() {
            return product;
        }


    }

}
