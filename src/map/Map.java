package map;
import model.Terrain;
import noise.Noise;

public abstract class Map {

	protected int mWidth;
	
	protected int mHeight;

	protected double mSeed;

	protected double mSeedForest;

	protected double mLandGen;
	
	protected double mWaterGen;

	protected double mMountainGen;

	protected double mHillGen;

	protected double mBeachGen;

	protected double mForestGen;

	protected Noise mNoise;

	protected Noise mForestNoise;

	public abstract void generateMap();
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public Noise getNoise() {
		return mNoise;
	}

    public double getLandGen() {
        return mLandGen;
    }
	
	public abstract Terrain getTerrain(int x, int y);

	protected abstract Terrain determineTerrainTypeBasedOnElevation(Terrain terrain, double elevation);
	
}
