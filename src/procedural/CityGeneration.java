package procedural;

import map.Map;
import model.*;

import java.util.*;

public class CityGeneration {

    private Map mMap;

    private Grid cityScoresGrid;

    private List<Terrain> citiesPlaced;

    private int numCities;

    private final double RIVER_SCORE = 1;

    private final double SHORE_SCORE = 0.75;

    private final double HIGH_GROUND_SCORE = 0.3;

    private final int DEGREES = 3;

    private final int MIN_CITY_DISTANCE = 75;

    PriorityQueue<Terrain> cities;

    public CityGeneration(Map map, int numCities) {
        mMap = map;
        this.numCities = numCities;
        cities = new PriorityQueue<>(numCities, new CityComparator());

        cityScoresGrid = new Grid(map.getWidth(), map.getHeight());
        cityScoresGrid.initializeGridWithTerrain();
        citiesPlaced =  new ArrayList<>();
    }

    public void generate() {

        for (List<Point> row : mMap.getNoise().getGrid().getGrid()) {
            for (Point point : row) {
                if (!isValidTerrainType((Terrain) point)) {
                    continue;
                }

                List<Point> adjacentPoints = mMap.getNoise().getGrid().getAdjacentPoints(point, DEGREES);

                Set<TerrainType> adjacentTerrainTypes = new HashSet<>();

                for (Point adjPoint : adjacentPoints) {
                    Terrain terrain = (Terrain) adjPoint;
                    adjacentTerrainTypes.add(terrain.getTerrainType());
                }

                ((Terrain) cityScoresGrid.getPoint(point.getX(), point.getY())).setScore(getScore(adjacentTerrainTypes));

                cities.add((Terrain) cityScoresGrid.getPoint(point.getX(), point.getY()));
            }
        }

        placeCities();
     }

    // rank a location's suitability to be a city based on weights assigned to
    // terrain types and some randomness sprinkled in
    private double getScore(Set<TerrainType> adjacentTerrainTypes) {
        double score = 0;

        if (adjacentTerrainTypes.contains(TerrainType.RIVER) ||
                adjacentTerrainTypes.contains((TerrainType.RIVER_BANK))) {
            score += RIVER_SCORE * Math.random();
        }
        if (adjacentTerrainTypes.contains(TerrainType.WATER)) {
            score += SHORE_SCORE * Math.random();
        }
        if (adjacentTerrainTypes.contains(TerrainType.MOUNTAIN) ||
                adjacentTerrainTypes.contains(TerrainType.HILL)) {
            score += HIGH_GROUND_SCORE * Math.random();
        }

        score += Math.random();

        return score;
    }

    // place x number of cities based on rank and proximity
    private void placeCities() {
        int numCitiesToBePlaced = numCities;
        while (numCitiesToBePlaced > 0 && cities.size() > 0) {
            Terrain location = cities.poll();
            if (nearCity(location)) {
                continue;
            }

            Terrain city = mMap.getTerrain(location.getX(), location.getY());
            city.setLocationType(LocationType.CITY);
            city.setLocation(new City());
            citiesPlaced.add(city);
            numCitiesToBePlaced--;
        }
    }

    // return true if within certain distance of an already placed city
    private boolean nearCity(Terrain location) {
        for (Terrain city : citiesPlaced) {
            if (Math.sqrt(Math.pow(location.getX() - city.getX(), 2) +
                    Math.pow(location.getY() - city.getY(), 2)) <= MIN_CITY_DISTANCE) {
                return true;
            }
        }

        return false;
    }

    // returns true if terrain type is valid for a city to be placed
    private boolean isValidTerrainType(Terrain terrain) {
        return !(terrain.getTerrainType().equals(TerrainType.WATER) ||
                terrain.getTerrainType().equals(TerrainType.RIVER) ||
                terrain.getTerrainType().equals(TerrainType.RIVER_BANK) ||
                terrain.getTerrainType().equals(TerrainType.BEACH) ||
                terrain.getTerrainType().equals(TerrainType.MOUNTAIN));
    }

    private class CityComparator implements Comparator<Terrain> {

        @Override
        public int compare(Terrain a, Terrain b) {
            if (a.getScore() < b.getScore()) {
                return 1;
            } else if (a.getScore() > b.getScore()) {
                return -1;
            }

            return 0;
        }
    }
}
