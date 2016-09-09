package procedural;

import map.Map;
import model.LocationType;
import model.Terrain;
import model.TerrainType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TerritoryGenerationV2 {

    private static final String TAG = TerritoryGenerationV2.class.getSimpleName();

    private Map mMap;

    private int mTerritoryNum;

    private List<Terrain> mCities;

    private Set<Coords> mRivers;

    public TerritoryGenerationV2(Map map) {
        mMap = map;
        mTerritoryNum = 0;
        mCities = new ArrayList<>();
        mRivers = new HashSet<>();
    }

    public void generate() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType().equals(LocationType.CITY)) {
                    terrain.setTerritory(mTerritoryNum);
                    mCities.add(terrain);
                    mTerritoryNum++;
                } else if (terrain.getTerrainType().equals(TerrainType.RIVER)) {
                    mRivers.add(new Coords(terrain.getX(), terrain.getY()));
                }
            }
        }

        determineVoronoiTerritories();
    }

    private void determineVoronoiTerritories() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getTerrainType().equals(TerrainType.LAND) ||
                        terrain.getTerrainType().equals(TerrainType.MOUNTAIN) ||
                        terrain.getTerrainType().equals(TerrainType.HILL)) {
                    terrain.setTerritory(getClosestCityTerritory(terrain));
                }
            }
        }
    }

    private int getClosestCityTerritory(Terrain terrain) {
        double min = mMap.getHeight() > mMap.getWidth() ? mMap.getHeight() : mMap.getWidth();
        Terrain closestCity = null;
        for (Terrain city : mCities) {
            double dist = getDistanceRivers(city, terrain);
            if (dist < min) {
                min = dist;
                closestCity = city;
            }
        }

        if (closestCity == null) {
            Logger.getLogger(TAG).log(Level.SEVERE, "No closest city found.");
            System.exit(0);
        }

        return closestCity.getTerritory();
    }

    private double getDistance(Terrain city, Terrain terrain) {
        //return Math.sqrt(Math.pow(terrain.getX() - city.getX(), 2) + Math.pow(terrain.getY() - city.getY(), 2));
        return Math.abs(terrain.getX() - city.getX()) + Math.abs(terrain.getY() - city.getY());
    }

    private double getDistanceRivers(Terrain city, Terrain terrain) {
        double riverScore = 0;

        // check if we intersect a river point
        int dx = terrain.getX() - city.getX();
        int dy = terrain.getY() - city.getY();
        int distance = dy - dx;
        int y = city.getY();

        for (int x = city.getX(); x < terrain.getX(); x++) {

            if (mRivers.contains(new Coords(x, y))) {
                riverScore = 500;
                break;
            }

            if (distance >= 0) {
                y++;
                distance = distance - dx;
            }
            distance = distance + dy;
        }

        return Math.abs(terrain.getX() - city.getX()) + Math.abs(terrain.getY() - city.getY()) + riverScore;
    }

    private class Coords {
        int x;
        int y;

        public boolean equals(Object o) {
            Coords coords = (Coords) o;
            return coords.x == x && coords.y == y;
        }

        public Coords(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

        public int hashCode() {
            return new Integer(x + "0" + y);
        }
    }
}
