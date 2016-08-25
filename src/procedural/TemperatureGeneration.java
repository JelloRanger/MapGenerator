package procedural;

import map.Map;
import model.Point;
import model.Terrain;
import model.TerrainType;

import java.util.List;

public class TemperatureGeneration {

    private Map mMap;

    private final double MIN_TEMPERATURE = 0.0;

    private final double MAX_TEMPERATURE = 1.0;

    public TemperatureGeneration(Map map) {
        mMap = map;
    }

    public void generate() {

        for (List<Point> row : mMap.getNoise().getGrid().getGrid()) {
            for (Point point : row) {
                Terrain terrain = (Terrain) point;
                terrain.setTemperature(determineTemperature(terrain));
            }
        }
    }

    private double determineTemperature(Terrain terrain) {
        double temperature = (MAX_TEMPERATURE - MIN_TEMPERATURE) * getDistanceFromEdgeY(terrain.getY()) /
                (mMap.getHeight() / 2 - 1) + MIN_TEMPERATURE;

        if (!(terrain.getTerrainType().equals(TerrainType.WATER) ||
                terrain.getTerrainType().equals(TerrainType.RIVER) ||
                terrain.getTerrainType().equals(TerrainType.RIVER_BANK))) {
            temperature -= terrain.getElevation() / 3;
        }

        return temperature;
    }

    private int getDistanceFromEdgeY(int y) {
        return mMap.getHeight() - y - 1 < y ? mMap.getHeight() - y - 1 : y;
    }
}
