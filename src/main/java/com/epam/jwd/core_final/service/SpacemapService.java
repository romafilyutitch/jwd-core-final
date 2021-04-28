package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.domain.Planet;
import com.epam.jwd.core_final.exception.NotAbleToBeCreatedException;

import java.util.List;

public interface SpacemapService {

    Planet getRandomPlanet();

    List<Planet> getAllPlanets();

    // Dijkstra ?
    int getDistanceBetweenPlanets(Planet first, Planet second);

    Planet createPlanet(Planet planet) throws NotAbleToBeCreatedException;
}
