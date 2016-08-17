package procedural;

import map.Map;
import noise.ContinentNoise;

/**
 * Modifies the map to have continental landmasses. This is done by combining elevations from the map with continent
 * noise, thereby increasing the elevations of points in the center of the map and decreasing the elevations of points
 * near the edges of the map. This has the effect of making points near the center more likely to be land and
 * points near the edges more likely to be water
 */
public class ContinentGeneration {

    private Map mMap;

    private ContinentNoise mContinentNoise;

    public ContinentGeneration(Map map) {
        mMap = map;
    }

    public void generate() {
        mContinentNoise = new ContinentNoise(mMap.getWidth(), mMap.getHeight());
        mContinentNoise.initializeNoiseGrid();

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                double elevation = mMap.getNoise().getGrid().getPoint(x, y).getElevation();
                mMap.getNoise().getGrid().getPoint(x, y).setElevation(
                        elevation + mContinentNoise.getGrid().getPoint(x, y).getElevation());
            }
        }
    }
}
