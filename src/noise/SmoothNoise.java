package noise;

import java.util.List;

import model.Point;

public class SmoothNoise extends RandomNoise {
	
	protected int degrees;
	
	public SmoothNoise(int width, int height, double seed,int degrees) {
		super(width, height, seed);
		
		this.degrees = degrees;
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
	
	// Smooth a point by averaging its elevation with all adjacent points
	private void smoothPoint(Point point) {
		List<Point> adjacentPoints = mGrid.getAdjacentPoints(point, degrees);
		
		double elevation = point.getElevation();
		double sum = elevation;
		
		for (Point adjacentPoint : adjacentPoints) {
			sum += adjacentPoint.getElevation();
		}
		
		point.setElevation(sum / (adjacentPoints.size() + 1));
	}

}
