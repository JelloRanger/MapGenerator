package model;

import java.util.ArrayList;
import java.util.List;

public class Grid {

	protected int width;
	
	protected int height;
	
	protected List<List<Point>> grid;
	
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void initializeGrid() {
		
		grid = new ArrayList<>();
		
		for (int y = 0; y < height; y++) {
			grid.add(new ArrayList<>());
			for (int x = 0; x < width; x++) {
				grid.get(y).add(new Point(x, y));
			}
		}
	}
	
	public void initializeGridWithTerrain() {
		
		grid = new ArrayList<>();
		
		for (int y = 0; y < height; y++) {
			grid.add(new ArrayList<>());
			for (int x = 0; x < width; x++) {
				grid.get(y).add(new Terrain(x, y));
			}
		}
	}
	
	public List<List<Point>> getGrid() {
		return grid;
	}
	
	public Point getPoint(int x, int y) {
		return grid.get(y).get(x);
	}
	
	public List<Point> getAdjacentPoints(Point point, int degrees) {
		return getAdjacentPoints(point.getX(), point.getY(), degrees);
	}
	
	public List<Point> getAdjacentPoints(int x, int y, int degrees) {
		List<Point> adjacentPoints = new ArrayList<>();
		
		for (int dy = degrees * -1; dy <= degrees; dy++) {
			for (int dx = degrees * -1; dx <= degrees; dx++) {
				if ((dx != 0 || dy != 0) && 
						x + dx >= 0 && x + dx < width && 
						y + dy >= 0 && y + dy < height) {
					adjacentPoints.add(grid.get(y + dy).get(x + dx));
				}
			}
		}
		
		return adjacentPoints;
	}

	public List<Terrain> getAdjacentTerrain(Terrain terrain, int degrees) {
        return getAdjacentTerrain(terrain.getX(), terrain.getY(), degrees);
    }

    public List<Terrain> getAdjacentTerrain(int x, int y, int degrees) {
        List<Terrain> adjacentTerrain = new ArrayList<>();

        for (int dy = degrees * -1; dy <= degrees; dy++) {
            for (int dx = degrees * -1; dx <= degrees; dx++) {
                if ((dx != 0 || dy != 0) &&
                        x + dx >= 0 && x + dx < width &&
                        y + dy >= 0 && y + dy < height) {
                    adjacentTerrain.add((Terrain) grid.get(y + dy).get(x + dx));
                }
            }
        }

        return adjacentTerrain;
    }

	public double getSlope(Point point) {
		return getSlope(point.getX(), point.getY());
	}

	public double getSlope(int x, int y) {
		if (x - 1 < 0 || y - 1 < 0) {
			return 0;
		}

		return grid.get(y - 1).get(x - 1).getElevation() - grid.get(y).get(x).getElevation();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
