package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public Set<TerrainType> getAdjacentTerrainTypes(Terrain terrain, int degrees) {
		Set<TerrainType> adjacentTerrain = new HashSet<>();
		int x = terrain.getX();
		int y = terrain.getY();

		for (int dy = degrees * -1; dy <= degrees; dy++) {
			for (int dx = degrees * -1; dx <= degrees; dx++) {
				if ((dx != 0 || dy != 0) &&
						x + dx >= 0 && x + dx < width &&
						y + dy >= 0 && y + dy < height) {
					adjacentTerrain.add(((Terrain) grid.get(y + dy).get(x + dx)).getTerrainType());
				}
			}
		}

		return adjacentTerrain;
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

	// direction is -1 for left, 1 for right
	public List<Terrain> getAdjacentTerrainByDirection(int x, int y, int degrees, int dir) {
		List<Terrain> adjacentTerrain = new ArrayList<>();

		// left
		if (dir == -1) {
			for (int dy = -15; dy <= 15; dy++) {
				for (int dx = degrees * -1; dx <= 0; dx++) {
					if ((dx != 0 || dy != 0) &&
							x + dx >= 0 && x + dx < width &&
							y + dy >= 0 && y + dy < height) {
						adjacentTerrain.add((Terrain) grid.get(y + dy).get(x + dx));
					}
				}
			}
		}

		else if (dir == 1) {
			for (int dy = -15; dy <= 15; dy++) {
				for (int dx = 0; dx <= degrees; dx++) {
					if ((dx != 0 || dy != 0) &&
							x + dx >= 0 && x + dx < width &&
							y + dy >= 0 && y + dy < height) {
						adjacentTerrain.add((Terrain) grid.get(y + dy).get(x + dx));
					}
				}
			}
		}

		return adjacentTerrain;
	}

	public List<Terrain> getAdjacentTerrainByDirAndStrength(int x, int y, int degrees, double strength) {
		List<Terrain> adjacentTerrain = new ArrayList<>();

        // left
        if (strength <= 0) {

            strength *= -1;
            int startPos = -1 * ((int) (strength * degrees + ((1 - strength) / 2 * degrees)));
            int endPos = startPos + degrees;

            for (int dy = -5; dy <= 5; dy++) {
                List<Terrain> adjacentTerrainForThisRow = new ArrayList<>();
                for (int dx = endPos; dx >= startPos; dx--) {
                    if ((dx != 0 || dy != 0) &&
                            x + dx >= 0 && x + dx < width &&
                            y + dy >= 0 && y + dy < height) {
                        if (((Terrain) grid.get(y + dy).get(x + dx)).getTerrainType().equals(TerrainType.MOUNTAIN) &&
                                dx <= 0) {
                            adjacentTerrainForThisRow.clear();
                            continue;
                        }
                        adjacentTerrainForThisRow.add((Terrain) grid.get(y + dy).get(x + dx));
                    }
                }
                adjacentTerrain.addAll(adjacentTerrainForThisRow);
            }
        }

        // right
        else if (strength > 0) {

            int endPos = ((int) (strength * degrees + ((1 - strength / 2 * degrees))));
            int startPos = endPos - degrees;

            for (int dy = -5; dy <= 5; dy++) {
                List<Terrain> adjacentTerrainForThisRow = new ArrayList<>();
                for (int dx = startPos; dx <= endPos; dx++) {
                    if ((dx != 0 || dy != 0) &&
                            x + dx >= 0 && x + dx < width &&
                            y + dy >= 0 && y + dy < height) {
                        if (((Terrain) grid.get(y + dy).get(x + dx)).getTerrainType().equals(TerrainType.MOUNTAIN) &&
                                dx >= 0) {
                            adjacentTerrainForThisRow.clear();
                            continue;
                        }
                        adjacentTerrainForThisRow.add((Terrain) grid.get(y + dy).get(x + dx));
                    }
                }
                adjacentTerrain.addAll(adjacentTerrainForThisRow);
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
