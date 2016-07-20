package noise;

import model.Point;

public class RevisedSmoothNoise extends RandomNoise {
	
	public RevisedSmoothNoise(int width, int height, double seed) {
		super(width, height, seed);
	}

	@Override
	public void initializeNoiseGrid() {
		super.initializeNoiseGrid();
		
		smoothElevation();
	}

	@Override
	public void initializeMapGrid() {
		super.initializeMapGrid();
		
		smoothElevation();
	}

	private void smoothElevation() {
		for (int y = 0; y < mHeight; y++) {
			for (int x = 0; x < mWidth; x++) {
				smoothPoint(mGrid.getPoint(x, y));
			}
		}
	}
	
	// Smooth a point by averaging its elevation with adjacent points, weighted
	private void smoothPoint(Point point) {
		int x = point.getX();
		int y = point.getY();

		point.setElevation(smoothNoise((double) x, (double) y));
	}

	protected double smoothNoise(double xDoub, double yDoub) {

		int x = (int) xDoub;
		int y = (int) yDoub;

		// weight edges at half
		double edgeNeighborElevations = noise(x - 1, y) +
				noise(x + 1, y) +
				noise(x, y - 1) +
				noise(x, y + 1);

		// weight corners at a quarter
		double cornerNeighborElevations = noise(x - 1, y - 1) +
				noise(x - 1, y + 1) +
				noise(x + 1, y + 1) +
				noise(x + 1, y - 1);

		return noise(x, y) / 4 + edgeNeighborElevations / 8 + cornerNeighborElevations / 16;
	}
}
