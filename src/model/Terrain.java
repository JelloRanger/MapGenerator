package model;

public class Terrain extends Point {

	protected TerrainType terrainType;

    protected LocationType locationType;

    protected double score;
	
	public Terrain(int x, int y) {
		super(x, y);
		score = 0;
	}
	
	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public void setScore(double amt) {
        score = amt;
    }

	public TerrainType getTerrainType() {
		return terrainType;
	}

    public LocationType getLocationType() {
        return locationType;
    }

    public double getScore() {
        return score;
    }
}
