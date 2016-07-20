package model;

public class Terrain extends Point {

	protected TerrainType terrainType;

    protected boolean isForest = false;
	
	public Terrain(int x, int y) {
		super(x, y);
	}
	
	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}
	
	public TerrainType getTerrainType() {
		return terrainType;
	}

}
