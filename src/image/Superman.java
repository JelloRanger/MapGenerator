package image;

import map.Map;
import model.LocationType;
import model.Terrain;
import model.TerrainType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Superman {

    protected Map mMap;

    protected BufferedImage mImage;

    protected final int OVAL_WIDTH = 5;

    protected final int OVAL_HEIGHT = 5;

    public Superman(Map map) {
        mMap = map;
        mImage = new BufferedImage(mMap.getWidth(), mMap.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
    
    public void generateImage() {
        initializeMap();
    }

    public BufferedImage getImage() {
        return mImage;
    }
    
    private void initializeMap() {
        mMap.generateMap();
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                mImage.setRGB(x, y, getColorByTerrain(mMap.getTerrain(x, y)).getRGB());
            }
        }

        if (!mMap.isCitiesEnabled()) {
            return;
        }

        Graphics2D graph = mImage.createGraphics();
        graph.setColor(Color.red);

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType() != null && terrain.getLocationType().equals(LocationType.CITY)) {
                    graph.fillOval(x - OVAL_WIDTH / 2, y - OVAL_HEIGHT / 2, OVAL_WIDTH, OVAL_HEIGHT);
                    if (mMap.isNamesEnabled()) {
                        graph.setColor(Color.black);
                        graph.drawString(terrain.getLocation().getName(), x - OVAL_WIDTH * 4, y - OVAL_HEIGHT);
                        graph.setColor(Color.red);
                    }
                }
            }
        }
    }
    
    private Color getColorByTerrain(Terrain terrain) {
        TerrainType terrainType = terrain.getTerrainType();
        double elevation = terrain.getElevation();
        double percent = (elevation + 1) / 2;
        
        Color terrainColor;
        switch (terrainType) {
            case MOUNTAIN:
                //terrainColor = Color.GREY;
                //terrainColor = new Color(244, 241, 222);
                //terrainColor = new Color(118, 112, 88);
                //terrainColor = averageColors(new Color(118, 112, 88), new Color(0, 0, 0), percent);
                terrainColor = averageColors(new Color(50,50,50), new Color(128, 128, 128), percent);
                break;
            case HILL:
                //terrainColor = Color.SADDLEBROWN;
                //terrainColor = new Color(166, 149, 102);
                //terrainColor = new Color(134, 151, 81);
                terrainColor = averageColors(new Color(134, 151, 81), new Color(0, 0, 0), percent);
                break;
            case FOREST:
                //terrainColor = Color.DARKGREEN;
                terrainColor = new Color(50, 70, 33);
                break;
            case LAND:
                //terrainColor = Color.GREEN;
                //terrainColor = new Color(41, 59, 33);
                terrainColor = new Color(39, 128, 64);
                //terrainColor = averageColors(new Color(29, 128, 64), new Color(18, 61, 30), percent);
                terrainColor = averageColors(new Color(29, 128, 64), new Color(0, 45, 0), percent);
                break;
            case BEACH:
                //terrainColor = Color.TAN;
                //terrainColor = new Color(9, 13, 47);
                //terrainColor = new Color(51, 101, 167);
                terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            case WATER:
                //terrainColor = Color.BLUE;
                //terrainColor = new Color(1, 0, 14);
                //terrainColor = new Color(29, 78, 145);
                //terrainColor = averageColors(new Color(29, 78, 145), new Color(18, 49, 90), percent);
                terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            case RIVER:
                //terrainColor = Color.BLUE;
                //terrainColor = new Color(1, 0, 14);
                //terrainColor = new Color(29, 78, 145);
                terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            default:
                terrainColor = Color.WHITE;
                break;
        }

        return terrainColor;
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
