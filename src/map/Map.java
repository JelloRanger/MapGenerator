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

    protected int mCityGen;

	protected Noise mNoise;

	protected Noise mForestNoise;

	protected boolean mLandEnabled = true;

	protected boolean mHillsEnabled = true;

	protected boolean mMountainsEnabled = true;

	protected boolean mRiversEnabled = true;

	protected boolean mCitiesEnabled = true;

	protected boolean mNamesEnabled = true;

	protected boolean mTerritoriesEnabled = true;

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

	public boolean isLandEnabled() {
		return mLandEnabled;
	}

	public boolean isHillsEnabled() {
		return mHillsEnabled;
	}

	public boolean isMountainsEnabled() {
		return mMountainsEnabled;
	}

	public boolean isRiversEnabled() {
		return mRiversEnabled;
	}

	public boolean isCitiesEnabled() {
		return mCitiesEnabled;
	}

	public boolean isNamesEnabled() {
		return mNamesEnabled;
	}

	public boolean isTerritoriesEnabled() {
		return mTerritoriesEnabled;
	}

	public abstract Terrain getTerrain(int x, int y);

	protected abstract Terrain determineTerrainTypeBasedOnElevation(Terrain terrain, double elevation);
	
}
