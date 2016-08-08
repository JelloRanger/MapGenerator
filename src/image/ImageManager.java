package image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import map.Map;
import model.LocationType;
import model.Terrain;
import model.TerrainType;

import java.awt.*;

public class ImageManager {

    private Map mMap;

    private Canvas mCanvas;

    private GraphicsContext mGraphicsContext;

    private PixelWriter mPixelWriter;

    protected final int OVAL_WIDTH = 5;

    protected final int OVAL_HEIGHT = 5;

    public ImageManager(int width, int height) {
        mCanvas = new Canvas(width, height);
        mGraphicsContext = mCanvas.getGraphicsContext2D();
        mPixelWriter = mGraphicsContext.getPixelWriter();
    }

    public void generateImage() {

        mGraphicsContext.clearRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
        mCanvas.setWidth(mMap.getWidth());
        mCanvas.setHeight(mMap.getHeight());

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                mPixelWriter.setColor(x, y, getColorByTerrain(mMap.getTerrain(x, y)));
            }
        }

        mGraphicsContext.setFill(Color.RED);

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType() != null && terrain.getLocationType().equals(LocationType.CITY)) {
                    mGraphicsContext.fillOval(x - OVAL_WIDTH / 2, y - OVAL_HEIGHT / 2, OVAL_WIDTH, OVAL_HEIGHT);
                    mGraphicsContext.setFill(Color.BLACK);
                    mGraphicsContext.fillText(terrain.getLocation().getName(), x - OVAL_WIDTH * 4, y - OVAL_HEIGHT);
                    mGraphicsContext.setFill(Color.RED);
                }
            }
        }
    }

    private Color getColorByTerrain(Terrain terrain) {
        TerrainType terrainType = terrain.getTerrainType();
        double elevation = terrain.getElevation(); // number between -1 and 1 roughly
        double percent = (elevation + 1) / 2;

        Color terrainColor;
        switch (terrainType) {
            case MOUNTAIN:
                //terrainColor = Color.GREY;
                //terrainColor = Color.rgb(244, 241, 222);
                //terrainColor = Color.rgb(118, 112, 88);
                //terrainColor = averageColors(Color.rgb(118, 112, 88), Color.rgb(0, 0, 0), percent);
                terrainColor = averageColors(Color.rgb(50,50,50), Color.rgb(128, 128, 128), percent);
                break;
            case HILL:
                //terrainColor = Color.SADDLEBROWN;
                //terrainColor = Color.rgb(166, 149, 102);
                //terrainColor = Color.rgb(134, 151, 81);
                terrainColor = averageColors(Color.rgb(134, 151, 81), Color.rgb(0, 0, 0), percent);
                break;
            case FOREST:
                //terrainColor = Color.DARKGREEN;
                terrainColor = Color.rgb(50, 70, 33);
                break;
            case LAND:
                //terrainColor = Color.GREEN;
                //terrainColor = Color.rgb(41, 59, 33);
                terrainColor = Color.rgb(39, 128, 64);
                terrainColor = averageColors(Color.rgb(29, 128, 64), Color.rgb(18, 61, 30), percent);
                //terrainColor = averageColors(Color.rgb(29, 128, 64), Color.rgb(0, 45, 0), percent);
                break;
            case BEACH:
                //terrainColor = Color.TAN;
                //terrainColor = Color.rgb(9, 13, 47);
                terrainColor = Color.rgb(51, 101, 167);
                terrainColor = averageColors(Color.rgb(29, 78, 145), Color.rgb(14, 37, 69), percent);
                break;
            case WATER:
                //terrainColor = Color.BLUE;
                //terrainColor = Color.rgb(1, 0, 14);
                //terrainColor = Color.rgb(29, 78, 145);
                terrainColor = averageColors(Color.rgb(29, 78, 145), Color.rgb(18, 49, 90), percent);
                terrainColor = averageColors(Color.rgb(29, 78, 145), Color.rgb(14, 37, 69), percent);
                break;
            case RIVER:
                //terrainColor = Color.BLUE;
                //terrainColor = Color.rgb(1, 0, 14);
                //terrainColor = Color.rgb(29, 78, 145);
                terrainColor = averageColors(Color.rgb(29, 78, 145), Color.rgb(14, 37, 69), percent);
                break;
            default:
                terrainColor = Color.WHITE;
                break;
        }

        return terrainColor;
    }

    protected Color averageColors(Color color1, Color color2, double percent) {
        double red = color1.getRed() * percent * 255 + color2.getRed() * (1.0 - percent) * 255;
        double green = color1.getGreen() * percent * 255 + color2.getGreen() * (1.0 - percent) * 255;
        double blue = color1.getBlue() * percent * 255 + color2.getBlue() * (1.0 - percent) * 255;

        try {
            return Color.rgb((int) red, (int) green, (int) blue);
        } catch (IllegalArgumentException e) {
            System.out.println("Color out of range");
            System.out.println("red: " + red);
            System.out.println("green: " + green);
            System.out.println("blue: " + blue);
            System.exit(0);
        }

        return Color.WHITE;
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public void setMap(Map map) {
        mMap = map;
    }

}
