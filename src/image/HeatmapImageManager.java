package image;

import map.PerlinMap;
import model.Terrain;

import java.awt.*;

public class HeatmapImageManager extends ImageManager {

    double min, max;

    public HeatmapImageManager(PerlinMap map) {
        super(map, false);
    }

    @Override
    public void colorTerrain() {

        colorHumidity();
        //colorTemperature();

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {

                switch (mMap.getTerrain(x, y).getTerrainType()) {
                    case WATER:
                    case RIVER:
                    case RIVER_BANK:
                        mImage.setRGB(x, y, Color.white.getRGB());
                }

                mImage.setRGB(x, y, mixColorsWithAlpha(
                        getColorByTerrain(mMap.getTerrain(x, y)),
                        super.getColorByTerrain(mMap.getTerrain(x, y)), 0).getRGB());
            }
        }
    }

    private void colorHumidity() {
        min = mMap.getTerrain(0, 0).getHumidity();
        max = mMap.getTerrain(0, 0).getHumidity();

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {


                switch (mMap.getTerrain(x, y).getTerrainType()) {
                    case WATER:
                    case RIVER:
                    case RIVER_BANK:
                        continue;
                }

                if (mMap.getTerrain(x, y).getHumidity() < min)
                    min = mMap.getTerrain(x, y).getHumidity();
                if (mMap.getTerrain(x, y).getHumidity() > max)
                    max = mMap.getTerrain(x, y).getHumidity();
            }
        }
    }

    private void colorTemperature() {
        min = mMap.getTerrain(0, 0).getTemperature();
        max = mMap.getTerrain(0, 0).getTemperature();

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                if (mMap.getTerrain(x, y).getTemperature() < min)
                    min = mMap.getTerrain(x, y).getTemperature();
                if (mMap.getTerrain(x, y).getTemperature() > max)
                    max = mMap.getTerrain(x, y).getTemperature();
            }
        }
    }

    @Override
    protected Color getColorByTerrain(Terrain terrain) {
        return mixColorsWithAlpha(Color.blue, Color.red, normalize(terrain.getHumidity()));
    }

    private int normalize(double value) {
        final double MIN = 0.0;
        final double MAX = 255.0;

        return (int) ((MAX - MIN) / (max - min) * (value - min) + MIN);

    }
}
