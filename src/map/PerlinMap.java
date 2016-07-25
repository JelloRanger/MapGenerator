package map;

import model.Grid;
import model.LocationType;
import model.Terrain;
import model.TerrainType;
import noise.PerlinNoise;
import procedural.CityGeneration;
import procedural.LakesAndRiversGeneration;

public class PerlinMap extends RandomMap {

    protected static final double PERSISTENCE = 0.5;

    protected static final int OCTAVES = 8;

    protected Grid waterGrid;

    public PerlinMap(int width,
                     int height,
                     double seed,
                     double seedForest,
                     double landGen,
                     double waterGen,
                     double mountainGen,
                     double hillGen,
                     double beachGen,
                     double forestGen) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen);
    }

    @Override
    public void generateMap() {
        mNoise = new PerlinNoise(mWidth, mHeight, mSeed, PERSISTENCE, OCTAVES);
        mNoise.initializeMapGrid();

        mForestNoise = new PerlinNoise(mWidth, mHeight, mSeedForest, PERSISTENCE, 6);
        mForestNoise.initializeMapGrid();

        generateLakesAndRivers();

        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                getTerrain(x, y).setLocationType(LocationType.EMPTY);
            }
        }

        generateCities();
    }

    @Override
    public Terrain getTerrain(int x, int y) {
        Terrain terrain = (Terrain) mNoise.getGrid().getPoint(x, y);

        TerrainType terrainType = terrain.getTerrainType();
        if (terrainType == null) {
            if (mForestNoise.getGrid().getPoint(x, y).getElevation() >= mForestGen &&
                    determineTerrainTypeBasedOnElevation(terrain, terrain.getElevation()).getTerrainType()
                            == TerrainType.LAND) {
                terrain.setTerrainType(TerrainType.FOREST);
            } else {
                return determineTerrainTypeBasedOnElevation(terrain, terrain.getElevation());
            }
        }

        return terrain;
    }

    protected void generateLakesAndRivers() {
        LakesAndRiversGeneration lakesAndRiversGeneration = new LakesAndRiversGeneration(this);
        lakesAndRiversGeneration.generate();
    }

    protected void generateCities() {
        CityGeneration cityGeneration = new CityGeneration(this);
        cityGeneration.generate();
    }
}
