package map;

import model.Grid;
import model.LocationType;
import model.Terrain;
import model.TerrainType;
import noise.PerlinNoise;
import procedural.CityGeneration;
import procedural.LakesAndRiversGeneration;
import procedural.NameGeneration;
import procedural.TerritoryGeneration;

public class PerlinMap extends RandomMap {

    protected Double mPersistence;

    protected Integer mOctaves;

    protected static final int NUM_CITIES = 50;

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

    public PerlinMap(int width,
                     int height,
                     double seed,
                     double seedForest,
                     double landGen,
                     double waterGen,
                     double mountainGen,
                     double hillGen,
                     double beachGen,
                     double forestGen,
                     double persistence,
                     int octaves,
                     boolean landEnabled,
                     boolean hillsEnabled,
                     boolean mountainsEnabled,
                     boolean riversEnabled,
                     boolean citiesEnabled,
                     boolean namesEnabled) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen);
        mPersistence = persistence;
        mOctaves = octaves;
        mLandEnabled = landEnabled;
        mHillsEnabled = hillsEnabled;
        mMountainsEnabled = mountainsEnabled;
        mRiversEnabled = riversEnabled;
        mCitiesEnabled = citiesEnabled;
        mNamesEnabled = namesEnabled;
    }

    @Override
    public void generateMap() {
        mNoise = new PerlinNoise(mWidth, mHeight, mSeed, mPersistence, mOctaves);
        mNoise.initializeMapGrid();

        //generateForests();

        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                getTerrain(x, y).setLocationType(LocationType.EMPTY);
            }
        }

        if (mRiversEnabled) {
            generateLakesAndRivers();
        }
        if (true) {
            generateCities();
        }
        if (mNamesEnabled) {
            generateNames();
        }

        if (mCitiesEnabled) {
            generateTerritories();
        }
    }

    @Override
    public Terrain getTerrain(int x, int y) {
        Terrain terrain = (Terrain) mNoise.getGrid().getPoint(x, y);

        TerrainType terrainType = terrain.getTerrainType();
        if (terrainType == null) {
            if (mForestNoise != null && mForestNoise.getGrid().getPoint(x, y).getElevation() >= mForestGen &&
                    determineTerrainTypeBasedOnElevation(terrain, terrain.getElevation()).getTerrainType()
                            == TerrainType.LAND) {
                terrain.setTerrainType(TerrainType.FOREST);
            } else {
                return determineTerrainTypeBasedOnElevation(terrain, terrain.getElevation());
            }
        }

        return terrain;
    }

    protected void generateForests() {
        mForestNoise = new PerlinNoise(mWidth, mHeight, mSeedForest, mPersistence, 6);
        mForestNoise.initializeMapGrid();
    }

    protected void generateLakesAndRivers() {
        LakesAndRiversGeneration lakesAndRiversGeneration = new LakesAndRiversGeneration(this);
        lakesAndRiversGeneration.generate();
    }

    protected void generateCities() {
        CityGeneration cityGeneration = new CityGeneration(this, NUM_CITIES);
        cityGeneration.generate();
    }

    protected void generateNames() {
        NameGeneration nameGeneration = new NameGeneration(this, NUM_CITIES);
        nameGeneration.generate();
    }

    protected void generateTerritories() {
        TerritoryGeneration territoryGeneration = new TerritoryGeneration(this);
        territoryGeneration.generate();
    }
}
