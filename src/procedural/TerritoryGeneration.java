package procedural;

import map.Map;
import model.LocationType;
import model.Terrain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TerritoryGeneration {

    private Map mMap;

    private int mTerritoryNum;

    private List<Terrain> mTerrainToBePlaced;

    public TerritoryGeneration(Map map) {
        mMap = map;
        mTerritoryNum = 0;
        mTerrainToBePlaced = new LinkedList<>();
    }

    public void generate() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType().equals(LocationType.CITY)) {
                    terrain.setTerritory(mTerritoryNum);
                    mTerrainToBePlaced.add(terrain);
                    mTerritoryNum++;
                }
            }
        }

        expandTerritories();
    }

    private void expandTerritories() {

        while (!mTerrainToBePlaced.isEmpty()) {

            if (Math.random() > 0.95)
                Collections.shuffle(mTerrainToBePlaced);

            Terrain terrain = mTerrainToBePlaced.remove(0);
            List<Terrain> adjacentTerrains = mMap.getNoise().getGrid().getAdjacentTerrain(terrain, 1);
            for (Terrain adjacentTerrain : adjacentTerrains) {
                if (adjacentTerrain.getElevation() > mMap.getLandGen() - 0.1 &&
                        adjacentTerrain.getTerritory() == -1 || terrain.getElevation() > adjacentTerrain.getElevation()) {

                    adjacentTerrain.setTerritory(terrain.getTerritory());
                    if (Math.random() > 0.2)
                        mTerrainToBePlaced.add(adjacentTerrain);
                }
            }
        }
    }
}
