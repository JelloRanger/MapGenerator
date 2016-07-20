package model;

public class Point {

	private int x;
	
	private int y;
	
	protected double elevation;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(double elevation) {
		this.elevation = elevation;
	}
	
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public double getElevation() {
		return elevation;
	}

}
