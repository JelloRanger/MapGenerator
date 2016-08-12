package model;

public class Terrain extends Point {

	protected TerrainType terrainType;

    protected LocationType locationType;

    protected Location location;

    protected double score;

    protected int territory;
	
	public Terrain(int x, int y) {
		super(x, y);
		score = 0;
        territory = -1;
	}
	
	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setScore(double amt) {
        score = amt;
    }

    public void setTerritory(int num) {
        territory = num;
    }

	public TerrainType getTerrainType() {
		return terrainType;
	}

    public LocationType getLocationType() {
        return locationType;
    }

    public Location getLocation() {
        return location;
    }

    public double getScore() {
        return score;
    }

    public int getTerritory() {
        return territory;
    }
}
