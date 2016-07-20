package map;

import model.Terrain;
import model.TerrainType;
import noise.RandomNoise;

public class RandomMap extends Map {

	public RandomMap(int width,
                     int height,
                     double seed,
                     double seedForest,
                     double landGen,
                     double waterGen,
                     double mountainGen,
					 double beachGen,
                     double forestGen) {

		mWidth = width;
		mHeight = height;
		mSeed = seed;
		mSeedForest = seedForest;
		mLandGen = landGen;
		mWaterGen = waterGen;
		mMountainGen = mountainGen;
		mBeachGen = beachGen;
		mForestGen = forestGen;
	}
	
	@Override
	public void generateMap() {
		mNoise = new RandomNoise(mWidth, mHeight, mSeed);
		mNoise.initializeMapGrid();
	}
	
	@Override
	public Terrain getTerrain(int x, int y) {
		Terrain terrain = (Terrain) mNoise.getGrid().getPoint(x, y);

		if (terrain.getTerrainType() == null) {
			return determineTerrainTypeBasedOnElevation(terrain, terrain.getElevation());
		}
		
		return terrain;
	}

	@Override
	protected Terrain determineTerrainTypeBasedOnElevation(Terrain terrain, double elevation) {
		TerrainType terrainType;

		if (elevation >= mMountainGen) {
			terrainType = TerrainType.MOUNTAIN;
		} else if (elevation >= mLandGen) {
			terrainType = TerrainType.LAND;
		} else if (elevation >= mLandGen + mBeachGen) {
			terrainType = TerrainType.BEACH;
		} else {
			terrainType = TerrainType.WATER;
		}

		terrain.setTerrainType(terrainType);

        return terrain;
	}
}
