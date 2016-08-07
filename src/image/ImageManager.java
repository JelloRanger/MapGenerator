package image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import map.Map;
import model.Terrain;
import model.TerrainType;

public class ImageManager {

    private Map mMap;

    private Canvas mCanvas;

    private GraphicsContext mGraphicsContext;

    private PixelWriter mPixelWriter;

    public ImageManager(int width, int height) {
        mCanvas = new Canvas(width, height);
        mGraphicsContext = mCanvas.getGraphicsContext2D();
        mPixelWriter = mGraphicsContext.getPixelWriter();
    }

    public void generateImage() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                mPixelWriter.setColor(x, y, getColorByTerrain(mMap.getTerrain(x, y)));
            }
        }
    }

    private Color getColorByTerrain(Terrain terrain) {
        TerrainType terrainType = terrain.getTerrainType();

        Color terrainColor;
        switch (terrainType) {
            case MOUNTAIN:
                terrainColor = Color.GREY;
                break;
            case HILL:
                terrainColor = Color.SADDLEBROWN;
                break;
            case FOREST:
                terrainColor = Color.DARKGREEN;
                break;
            case LAND:
                terrainColor = Color.GREEN;
                break;
            case BEACH:
                terrainColor = Color.TAN;
                break;
            case WATER:
                terrainColor = Color.BLUE;
                break;
            case RIVER:
                terrainColor = Color.BLUE;
                break;
            default:
                terrainColor = Color.WHITE;
                break;
        }

        return terrainColor;
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public void setMap(Map map) {
        mMap = map;
    }

}
