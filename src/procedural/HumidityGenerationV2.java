package procedural;

import map.PerlinMap;
import model.Direction;
import model.Point;
import model.Terrain;
import model.TerrainType;

import java.util.List;

public class HumidityGenerationV2 {

    private PerlinMap mMap;

    private WindCurrentsV2 mWindCurrents;

    private final double MIN_HUMIDITY = 0.0;

    private final double MAX_HUMIDITY = 1.0;

    private final int OCEAN_SCORE = 10;

    private final int RIVER_SCORE = 0;

    private final int MOUNTAIN_SCORE = -1;

    public HumidityGenerationV2(PerlinMap map) {
        mMap = map;
        mWindCurrents = new WindCurrentsV2(mMap.getHeight());
    }

    public void generate() {

        mWindCurrents.generate();

        for (List<Point> row : mMap.getNoise().getGrid().getGrid()) {
            for (Point point : row) {
                Terrain terrain = (Terrain) point;

                switch (terrain.getTerrainType()) {
                    case WATER:
                    case RIVER:
                    case RIVER_BANK:
                    case BEACH:
                    case MOUNTAIN:
                        continue;
                }

                if (terrain.getTemperature() > 0.25) {
                    terrain.setHumidity(determineHumidity(terrain));
                } else {
                    terrain.setHumidity(0);
                }
            }
        }
    }

    private double determineHumidity(Terrain terrain) {

        double humidity = 0.0;

        List<Direction> directions = mWindCurrents.getDirection(terrain.getY());

        double humidityDir = 0.0;
        for (Direction dir : directions) {
            switch (dir) {
                case NORTH:
                    humidityDir = travelY(terrain, -1);
                    break;
                case NORTHEAST:
                    humidityDir = travelDiagonal(terrain, 1, -1);
                    break;
                case EAST:
                    humidityDir = travelX(terrain, 1);
                    break;
                case SOUTHEAST:
                    humidityDir = travelDiagonal(terrain, 1, 1);
                    break;
                case SOUTH:
                    humidityDir = travelY(terrain, 1);
                    break;
                case SOUTHWEST:
                    humidityDir = travelDiagonal(terrain, -1, 1);
                    break;
                case WEST:
                    humidityDir = travelX(terrain, -1);
                    break;
                case NORTHWEST:
                    humidityDir = travelDiagonal(terrain, -1, -1);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            //humidity = humidityDir > humidity ? humidityDir : humidity;
            humidity = (humidityDir + humidity) / 2;
        }

        return normalize(humidity);
    }

    private double travelDiagonal(Terrain terrain, int dx, int dy) {
        double humidity = 0.0;

        int x = terrain.getX() + dx;
        int y = terrain.getY() + dy;
        while (x >= 0 && x < mMap.getWidth() && y >= 0 && y < mMap.getHeight()) {
            humidity += getHumidity(mMap.getTerrain(x, y).getTerrainType());

            if (humidity < MOUNTAIN_SCORE * 30 || humidity > OCEAN_SCORE * 5) {
                break;
            }

            x += dx;
            y += dy;
        }

        return humidity;
    }

    private double travelX(Terrain terrain, int dx) {
        double humidity = 0.0;

        int x = terrain.getX() + dx;
        while (x >= 0 && x < mMap.getWidth()) {
            humidity += getHumidity(mMap.getTerrain(x, terrain.getY()).getTerrainType());

            if (humidity < MOUNTAIN_SCORE * 30 || humidity > OCEAN_SCORE * 5) {
                break;
            }

            x += dx;
        }

        return humidity;
    }

    private double travelY(Terrain terrain, int dy) {
        double humidity = 0.0;

        int y = terrain.getY() + dy;
        while (y >= 0 && y < mMap.getHeight()) {
            humidity += getHumidity(mMap.getTerrain(terrain.getX(), y).getTerrainType());

            if (humidity < MOUNTAIN_SCORE * 30 || humidity > OCEAN_SCORE * 5) {
                break;
            }

            y += dy;
        }

        return humidity;
    }

    private double getHumidity(TerrainType terrainType) {
        double humidity = 0.0;

        if (terrainType.equals(TerrainType.MOUNTAIN)) {
            humidity += MOUNTAIN_SCORE;
        } else if (terrainType.equals(TerrainType.RIVER) ||
                terrainType.equals(terrainType.RIVER_BANK)) {
            humidity += RIVER_SCORE;
        } else if (terrainType.equals(TerrainType.WATER) ||
                terrainType.equals(TerrainType.BEACH)) {
            humidity += OCEAN_SCORE;
        }

        return humidity;
    }

    private double normalize(double value) {
        double maxHumid = OCEAN_SCORE * 5;
        double minHumid = MOUNTAIN_SCORE * 30;
        return (MAX_HUMIDITY - MIN_HUMIDITY) / (maxHumid - minHumid) * value;
    }
}
