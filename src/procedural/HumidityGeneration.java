package procedural;

import map.Map;
import model.Point;
import model.Terrain;

import java.util.List;

public class HumidityGeneration {

    private Map mMap;

    private WindCurrents mWindCurrents;

    private final double MIN_HUMIDITY = 0.0;

    private final double MAX_HUMIDITY = 1.0;

    private final int WATER_RADIUS = 300;

    public HumidityGeneration(Map map) {
        mMap = map;
        mWindCurrents = new WindCurrents(mMap.getHeight());
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

        /*List<Terrain> adjacentTerrains = mMap.getNoise().getGrid().getAdjacentTerrainByDirection(
                terrain.getX(),
                terrain.getY(),
                WATER_RADIUS,
                mWindCurrents.getDirection((terrain.getY()));*/

        List<Terrain> adjacentTerrains = mMap.getNoise().getGrid().getAdjacentTerrainByDirAndStrength(
                terrain.getX(),
                terrain.getY(),
                WATER_RADIUS,
                mWindCurrents.getDirection(terrain.getY()));

        for (Terrain adjacentTerrain : adjacentTerrains) {
            switch (adjacentTerrain.getTerrainType()) {
                case WATER:
                case RIVER:
                case RIVER_BANK:
                case BEACH:
                    humidity++;
                    break;
                case MOUNTAIN:
                    //humidity -= 20;
                    break;
            }
        }

        humidity = humidity < 0 ? 0 : humidity;

        return normalize(humidity);
    }

    private double normalize(double value) {
        double maxHumid = Math.pow(((double) (2 * WATER_RADIUS + 1)), 2) - 1;
        assert(maxHumid == 3721);
        return (MAX_HUMIDITY - MIN_HUMIDITY) / /*maxHumid*/WATER_RADIUS * value;
    }
}
