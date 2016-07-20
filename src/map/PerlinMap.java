package map;

import model.Grid;
import model.Terrain;
import model.TerrainType;
import noise.PerlinNoise;
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
                     double beachGen,
                     double forestGen) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, beachGen, forestGen);
    }

    @Override
    public void generateMap() {
        mNoise = new PerlinNoise(mWidth, mHeight, mSeed, PERSISTENCE, OCTAVES);
        mNoise.initializeMapGrid();

        mForestNoise = new PerlinNoise(mWidth, mHeight, mSeedForest, PERSISTENCE, 6);
        mForestNoise.initializeMapGrid();

        generateLakesAndRivers();
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
        } /*else if (waterGrid != null && waterGrid.getPoint(x, y).getElevation() > 0) {
            terrain.setTerrainType(TerrainType.RIVER);
        }*/

        return terrain;
    }

    public void generateLakesAndRivers() {
        LakesAndRiversGeneration lakesAndRiversGeneration = new LakesAndRiversGeneration(this);
        lakesAndRiversGeneration.generate();
    }

    public void addLakesAndRivers(Grid waterGrid) {
        this.waterGrid = waterGrid;

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < waterGrid.getHeight(); y++) {
            for (int x = 0; x < waterGrid.getWidth(); x++) {
                sb.append(waterGrid.getPoint(x, y).getElevation() + " ");
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }
}
