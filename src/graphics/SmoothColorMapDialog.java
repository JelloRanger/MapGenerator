package graphics;

import java.awt.Color;

import model.Terrain;

public class SmoothColorMapDialog extends ColorMapDialog {

	@Override
	protected Color getColorByTerrain(Terrain terrain) {
		double elevation = terrain.getElevation(); // number between -1 and 1 roughly
		double percent = (elevation + 1) / 2;

		switch (terrain.getLocationType()) {
			case CITY:
				return Color.pink;
		}

		switch (terrain.getTerrainType()) {
			case WATER:
				return averageColors(Color.cyan, Color.blue, percent);
			case RIVER:
				return averageColors(Color.blue, Color.cyan, percent);
			case BEACH:
				return averageColors(new Color(209, 199, 119), new Color(227, 221, 175), percent);
			case LAND:
				return averageColors(Color.green, new Color(44, 125, 30), percent);
			case FOREST:
				return averageColors(new Color(5, 102, 0), new Color(7, 170, 0), percent);
			case MOUNTAIN:
				return averageColors(new Color(87, 87, 87), new Color(150, 150, 150), percent);
			default:
				return averageColors(new Color(179, 124, 21), new Color(140, 98, 18), percent);

		}
	}

	protected Color averageColors(Color color1, Color color2, double percent) {
		double red = color1.getRed() * percent + color2.getRed() * (1.0 - percent);
		double green = color1.getGreen() * percent + color2.getGreen() * (1.0 - percent);
		double blue = color1.getBlue() * percent + color2.getBlue() * (1.0 - percent);

		try {
			return new Color((int) red, (int) green, (int) blue);
		} catch (IllegalArgumentException e) {
			System.out.println("Color out of range");
			System.out.println("red: " + red);
			System.out.println("green: " + green);
			System.out.println("blue: " + blue);
			System.exit(0);
		}

		return Color.white;
	}
}
