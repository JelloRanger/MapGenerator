package noise;

import model.Grid;

public abstract class Noise {

	protected int mWidth;
	
	protected int mHeight;
	
	protected Grid mGrid;

	protected double mSeed;
	
	public abstract void initializeNoiseGrid();
	
	public abstract void initializeMapGrid();
	
	protected abstract void genNoise();
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}

	public Grid getGrid() {
		return mGrid;
	}
}
