package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import map.Map;
import model.LocationType;
import model.Terrain;
import model.TerrainType;

public class ColorMapDialog {

    protected final int OVAL_WIDTH = 7;

    protected final int OVAL_HEIGHT = 7;

	public void plotMap(Map map) {
		BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				image.setRGB(x, y, getColorByTerrain(map.getTerrain(x, y)).getRGB());
			}
		}

		Graphics2D graph = image.createGraphics();
		graph.setColor(Color.red);

		for (int y = 0; y < map.getHeight(); y++) {
			for (int x = 0; x < map.getWidth(); x++) {
				if (map.getTerrain(x, y).getLocationType().equals(LocationType.CITY)) {
                    graph.fillOval(x - OVAL_WIDTH / 2, y - OVAL_HEIGHT / 2, OVAL_WIDTH, OVAL_HEIGHT);

				}
			}
		}

		graph.dispose();
		
		createFrame(image);
	}
	
	protected Color getColorByTerrain(Terrain terrain) {
		TerrainType terrainType = terrain.getTerrainType();
		
		Color terrainColor;
		switch (terrainType) {
			case MOUNTAIN:
				terrainColor = Color.gray;
				break;
			case FOREST:
				terrainColor = Color.orange;
				break;
			case LAND:
				terrainColor = Color.green;
				break;
			case BEACH:
				terrainColor = Color.yellow;
				break;
			case WATER:
				terrainColor = Color.blue;
				break;
			default:
				terrainColor = Color.white;
				break;
		}
		
		return terrainColor;
	}
	
	public void createFrame(BufferedImage image) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
