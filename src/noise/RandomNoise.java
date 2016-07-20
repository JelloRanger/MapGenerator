package noise;

import java.util.List;

import model.Grid;
import model.Point;

import static java.lang.Math.abs;

public class RandomNoise extends Noise {

	public RandomNoise(int width, int height, double seed) {
		mWidth = width;
		mHeight = height;
		mSeed = seed;
	}

	@Override
	public void initializeNoiseGrid() {
		mGrid = new Grid(mWidth, mHeight);
		mGrid.initializeGrid();
		genNoise();
	}

	@Override
	public void initializeMapGrid() {
		mGrid = new Grid(mWidth, mHeight);
		mGrid.initializeGridWithTerrain();
		genNoise();
	}

	@Override
	protected void genNoise() {
		for (List<Point> row : mGrid.getGrid()) {
			for (Point point : row) {
				point.setElevation(noise(point.getX(), point.getY()));
			}
		}
	}

	// credit to http://freespace.virgin.net/hugo.elias/models/m_perlin.htm
	protected double noise(int x, int y) {
		int n = x + y * 75326 + ((int) (mSeed * 15485867));
		n = (n<<13) ^ n;

		return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}

}
