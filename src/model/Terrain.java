package model;

public class Terrain extends Point {

	protected TerrainType terrainType;

    protected BiomeType biomeType;

    protected LocationType locationType;

    protected Location location;

    protected double score;

    protected int territory;

    protected double temperature;

    protected double humidity;
	
	public Terrain(int x, int y) {
		super(x, y);
		score = 0;
        territory = -1;
	}
	
	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}

    public void setBiomeType(BiomeType biomeType) {
        this.biomeType = biomeType;
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

    public void setTemperature(double temp) {
        temperature = temp;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

	public TerrainType getTerrainType() {
		return terrainType;
	}

    public BiomeType getBiomeType() {
        return biomeType;
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

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }
}
