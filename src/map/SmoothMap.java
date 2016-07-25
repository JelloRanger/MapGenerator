package map;

import noise.SmoothNoise;

public class SmoothMap extends RandomMap {

	protected int mDegrees;
	
	public SmoothMap(int width, int height, double seed, double seedForest, int degrees, double landGen,
					 double waterGen, double mountainGen, double hillGen, double beachGen, double forestGen) {
		super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen);
		
		mDegrees = degrees;
	}
	
	@Override
	public void generateMap() {
		mNoise = new SmoothNoise(mWidth, mHeight, mSeed, mDegrees);
		mNoise.initializeMapGrid();
	}
}
