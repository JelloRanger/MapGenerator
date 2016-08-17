package map;

import metrics.Metric;
import metrics.MetricKey;
import model.LocationType;
import model.Terrain;
import model.TerrainType;
import noise.PerlinNoise;
import procedural.*;

public class PerlinMap extends RandomMap {

    protected Double mPersistence;

    protected Integer mOctaves;

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
                     int cityGen) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen, cityGen);
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
                     int cityGen,
                     double persistence,
                     int octaves,
                     boolean landEnabled,
                     boolean hillsEnabled,
                     boolean mountainsEnabled,
                     boolean riversEnabled,
                     boolean citiesEnabled,
                     boolean namesEnabled,
                     boolean continentsEnabled,
                     boolean territoriesEnabled) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen, cityGen);
        mPersistence = persistence;
        mOctaves = octaves;
        mLandEnabled = landEnabled;
        mHillsEnabled = hillsEnabled;
        mMountainsEnabled = mountainsEnabled;
        mRiversEnabled = riversEnabled;
        mCitiesEnabled = citiesEnabled;
        mNamesEnabled = namesEnabled;
        mContinentsEnabled = continentsEnabled;
        mTerritoriesEnabled = territoriesEnabled;
    }

    @Override
    public void generateMap() {
        mNoise = new PerlinNoise(mWidth, mHeight, mSeed, mPersistence, mOctaves);
        mNoise.initializeMapGrid();

        if (mContinentsEnabled) {
            generateContinents();
        }

        //generateForests();

        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                getTerrain(x, y).setLocationType(LocationType.EMPTY);
            }
        }

        if (mRiversEnabled) {
            generateLakesAndRivers();
        }

        // We need to generate cities if territories are enabled since
        // they're used to create territory starting points
        if (mCitiesEnabled || mTerritoriesEnabled) {
            generateCities();
        }

        if (mCitiesEnabled && mNamesEnabled) {
            generateNames();
        }

        if (mCitiesEnabled && mTerritoriesEnabled) {
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

    protected void generateContinents() {
        Metric.start(MetricKey.CONTINENTGENERATION);
        ContinentGeneration continentGeneration = new ContinentGeneration(this);
        continentGeneration.generate();
        Metric.record(MetricKey.CONTINENTGENERATION);
    }

    protected void generateLakesAndRivers() {
        Metric.start(MetricKey.RIVERGENERATION);
        LakesAndRiversGeneration lakesAndRiversGeneration = new LakesAndRiversGeneration(this);
        lakesAndRiversGeneration.generate();
        Metric.record(MetricKey.RIVERGENERATION);
    }

    protected void generateCities() {
        Metric.start(MetricKey.CITYGENERATION);
        CityGeneration cityGeneration = new CityGeneration(this, mCityGen);
        cityGeneration.generate();
        Metric.record(MetricKey.CITYGENERATION);
    }

    protected void generateNames() {
        Metric.start(MetricKey.NAMEGENERATION);
        NameGeneration nameGeneration = new NameGeneration(this, mCityGen);
        nameGeneration.generate();
        Metric.record(MetricKey.NAMEGENERATION);
    }

    protected void generateTerritories() {
        Metric.start(MetricKey.TERRITORYGENERATION);
        TerritoryGeneration territoryGeneration = new TerritoryGeneration(this);
        territoryGeneration.generate();
        Metric.record(MetricKey.TERRITORYGENERATION);
    }

    public Double getPersistence() {
        return mPersistence;
    }

    public Integer getOctaves() {
        return mOctaves;
    }
}
