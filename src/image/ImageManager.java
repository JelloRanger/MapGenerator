package image;

import map.PerlinMap;
import metrics.Metric;
import metrics.MetricKey;
import model.BiomeType;
import model.LocationType;
import model.Terrain;
import model.TerrainType;
import procedural.WindCurrents;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import static java.lang.StrictMath.abs;

public class ImageManager {

    protected PerlinMap mMap;

    protected BufferedImage mImage;

    protected boolean mGridEnabled;

    protected final int OVAL_WIDTH = 5;

    protected final int OVAL_HEIGHT = 5;

    public ImageManager(PerlinMap map, boolean gridEnabled) {
        mMap = map;
        mGridEnabled = gridEnabled;
        mImage = new BufferedImage(mMap.getWidth(), mMap.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    public void generateImage() {
        initializeMap();
    }

    public BufferedImage getImage() {
        return mImage;
    }
    
    protected void initializeMap() {
        mMap.generateMap();

        colorMap();
    }

    public void colorMap() {
        FantasyImageManager fantasyImageManager = new FantasyImageManager(mMap);
        fantasyImageManager.generate();
        mImage = fantasyImageManager.getImage();

        /*if (mMap.isBiomesEnabled()) {
            colorBiomes();
        } else {
            colorTerrain();
        }*/

        if (mGridEnabled) {
            colorGrid();
        }
    }

    private void colorBiomes() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getTerrainType().equals(TerrainType.WATER) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER) ||
                        terrain.getTerrainType().equals(TerrainType.BEACH) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER_BANK) ||
                        terrain.getTerrainType().equals(TerrainType.MOUNTAIN)) {
                    mImage.setRGB(x, y, getColorByTerrain(terrain).getRGB());
                } else {
                    mImage.setRGB(x, y, getColorByBiome(terrain).getRGB());
                }
            }
        }

        if (mMap.getPersistence() < 0.8 && mMap.getPersistence() > 0.4) {
            shadeMap(false);
        } else {
            shadeMapWithoutSlopeDiff();
        }

        //drawWindCurrents();

        if (mMap.isCitiesEnabled()) {
            colorCities();
        }

        if (mMap.isCitiesEnabled() && mMap.isNamesEnabled()) {
            colorNames();
        }
    }

    protected void colorTerrain() {
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                mImage.setRGB(x, y, getColorByTerrain(mMap.getTerrain(x, y)).getRGB());
            }
        }

        if (mMap.getPersistence() < 0.8 && mMap.getPersistence() > 0.4) {
            shadeMap(false);
        } else {
            shadeMapWithoutSlopeDiff();
        }

        if (mMap.isCitiesEnabled()) {
            colorCities();
        }

        if (mMap.isCitiesEnabled() && mMap.isNamesEnabled()) {
            colorNames();
        }
    }

    private void colorGrid() {
        for (int y = 0; y < mMap.getHeight(); y += 100) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                mImage.setRGB(x, y, mixColorsWithAlpha(new Color(mImage.getRGB(x, y)), Color.cyan, 25).getRGB());
            }
        }
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x += 100) {
                mImage.setRGB(x, y, mixColorsWithAlpha(new Color(mImage.getRGB(x, y)), Color.cyan, 25).getRGB());
            }
        }
    }

    private void shadeMapWithoutSlopeDiff() {
        Metric.start(MetricKey.SHADEMAP);
        BufferedImage copyImage = deepCopy(mImage);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                double slope = mMap.getNoise().getGrid().getSlope(terrain);
                if (slope < 0) {
                    mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.white, 8).getRGB());
                } else if (slope > 0) {
                    mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.black, 8).getRGB());
                }
            }
        }
        Metric.record(MetricKey.SHADEMAP);
    }

    private void shadeMap(boolean ignoreWater) {
        Metric.start(MetricKey.SHADEMAP);
        BufferedImage copyImage = deepCopy(mImage);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);

                if (ignoreWater && terrain.getTerrainType().equals(TerrainType.WATER)) {
                    continue;
                }

                double slope = mMap.getNoise().getGrid().getSlope(terrain);
                if (slope < 0) {
                    mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.white,
                            (int) (8 * abs(slope) * 50)).getRGB());
                } else if (slope > 0) {
                    mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.black,
                            (int) (8 * (slope) * 50)).getRGB());
                }
            }
        }
        Metric.record(MetricKey.SHADEMAP);
    }

    private BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel cm = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    protected Color mixColorsWithAlpha(Color color1, Color color2, int alpha) {
        float factor = alpha / 255f;
        int red = (int) (color1.getRed() * (1 - factor) + color2.getRed() * factor);
        int green = (int) (color1.getGreen() * (1 - factor) + color2.getGreen() * factor);
        int blue = (int) (color1.getBlue() * (1 - factor) + color2.getBlue() * factor);

        if (red > 255)
            red = 255;
        if (green > 255)
            green = 255;
        if (blue > 255)
            blue = 255;
        if (red < 0)
            red = 0;
        if (green < 0)
            green = 0;
        if (blue < 0)
            blue = 0;
        return new Color(red, green, blue);
    }

    public void colorPoliticalMap() {
        colorCountries();

        shadeMap(true);

        // Color cities and names, if enabled, on top of countries
        if (mMap.isCitiesEnabled()) {
            colorCities();
        }
        if (mMap.isNamesEnabled()) {
            colorNames();
        }

        if (mGridEnabled) {
            colorGrid();
        }
    }

    private void colorCities() {
        Graphics2D graph = mImage.createGraphics();
        graph.setColor(Color.red);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType() != null && terrain.getLocationType().equals(LocationType.CITY)) {
                    graph.fillOval(x - OVAL_WIDTH / 2, y - OVAL_HEIGHT / 2, OVAL_WIDTH, OVAL_HEIGHT);
                }
            }
        }
    }

    private void colorNames() {
        Graphics2D graph = mImage.createGraphics();
        graph.setColor(Color.black);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType() != null && terrain.getLocationType().equals(LocationType.CITY)) {
                    graph.drawString(terrain.getLocation().getName(), x - OVAL_WIDTH * 4, y - OVAL_HEIGHT);
                }
            }
        }
    }

    private void colorCountries() {
        //BufferedImage copyImage = deepCopy(mImage);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getTerrainType().equals(TerrainType.WATER) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER_BANK) ||
                        terrain.getTerrainType().equals(TerrainType.BEACH)) {
                    continue;
                }
                int territoryMod = terrain.getTerritory() % 6;
                switch (territoryMod) {
                    case 0:
                        mImage.setRGB(x, y, Color.red.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.red.darker(), 128).getRGB());
                        break;
                    case 1:
                        mImage.setRGB(x, y, Color.orange.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.orange.darker(), 128).getRGB());
                        break;
                    case 2:
                        mImage.setRGB(x, y, Color.yellow.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.yellow.darker(), 128).getRGB());
                        break;
                    case 3:
                        mImage.setRGB(x, y, Color.green.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.green.darker(), 128).getRGB());
                        break;
                    case 4:
                        mImage.setRGB(x, y, Color.blue.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.blue.darker(), 128).getRGB());
                        break;
                    case 5:
                        mImage.setRGB(x, y, Color.magenta.darker().getRGB());
                        //mImage.setRGB(x, y, mixColorsWithAlpha(new Color(copyImage.getRGB(x, y)), Color.magenta.darker(), 128).getRGB());
                        break;
                }
            }
        }
    }
    
    protected Color getColorByTerrain(Terrain terrain) {
        TerrainType terrainType = terrain.getTerrainType();
        double elevation = terrain.getElevation();
        double percent = (elevation + 1) / 2;
        
        Color terrainColor;
        switch (terrainType) {
            case MOUNTAIN:
                terrainColor = new Color(50, 50, 50);
                //terrainColor = new Color(244, 241, 222);
                //terrainColor = new Color(118, 112, 88);
                //terrainColor = averageColors(new Color(118, 112, 88), new Color(0, 0, 0), percent);
                //terrainColor = averageColors(new Color(50,50,50), new Color(128, 128, 128), percent);
                break;
            case HILL:
                terrainColor = new Color(134, 151, 81);
                //terrainColor = new Color(166, 149, 102);
                //terrainColor = new Color(134, 151, 81);
                //terrainColor = averageColors(new Color(134, 151, 81), new Color(0, 0, 0), percent);
                break;
            case FOREST:
                //terrainColor = Color.DARKGREEN;
                terrainColor = new Color(50, 70, 33);
                break;
            case LAND:
                terrainColor = new Color(29, 128, 64);
                //terrainColor = new Color(41, 59, 33);
                //terrainColor = new Color(39, 128, 64);
                //terrainColor = averageColors(new Color(29, 128, 64), new Color(18, 61, 30), percent);
                //terrainColor = averageColors(new Color(29, 128, 64), new Color(0, 45, 0), percent);
                break;
            case BEACH:
                terrainColor = new Color(29, 78, 145);
                //terrainColor = new Color(9, 13, 47);
                //terrainColor = new Color(51, 101, 167);
                //terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            case WATER:
                terrainColor = new Color(29, 78, 145);
                //terrainColor = new Color(1, 0, 14);
                //terrainColor = new Color(29, 78, 145);
                //terrainColor = averageColors(new Color(29, 78, 145), new Color(18, 49, 90), percent);
                //terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            case RIVER:
                terrainColor = new Color(29, 78, 145);
                //terrainColor = new Color(1, 0, 14);
                //terrainColor = new Color(29, 78, 145);
                //terrainColor = averageColors(new Color(29, 78, 145), new Color(14, 37, 69), percent);
                break;
            default:
                terrainColor = Color.WHITE;
                break;
        }

        return terrainColor;
    }

    protected Color getColorByBiome(Terrain terrain) {
        BiomeType biomeType = terrain.getBiomeType();

        Color biomeColor;
        switch (biomeType) {
            case TROPICALRAINFOREST:
                biomeColor = Color.green.darker();
                break;
            case SAVANNA:
                biomeColor = Color.yellow.darker();
                break;
            case SUBTROPICALDESERT:
                biomeColor = Color.orange.darker();
                break;
            case TEMPERATERAINFOREST:
                biomeColor = Color.green;
                break;
            case TEMPERATESEASONALFOREST:
                biomeColor = Color.pink;
                break;
            case SHRUBLAND:
                biomeColor = Color.yellow;
                break;
            case COLDDESERT:
                biomeColor = Color.orange;
                break;
            case BOREALFOREST:
                biomeColor = Color.cyan.darker();
                break;
            case TUNDRA:
            default:
                biomeColor = Color.white;
                break;
        }

        return biomeColor;
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

    private void drawWindCurrents() {

        WindCurrents windCurrents = new WindCurrents(mMap.getHeight());
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 4; x++) {
                if (windCurrents.getDirection(y * mImage.getHeight()/12) < 0) {
                    Graphics g = mImage.getGraphics();
                    g.setColor(Color.red);
                    drawArrowLine(g,
                            x * mImage.getWidth() / 4 + 100, y * mImage.getHeight() / 12,
                            x * mImage.getWidth() / 4, y * mImage.getHeight() / 12,
                            50, 10);
                } else {
                    Graphics g = mImage.getGraphics();
                    g.setColor(Color.magenta);
                    drawArrowLine(g,
                            x * mImage.getWidth() / 4, y * mImage.getHeight() / 12,
                            x * mImage.getWidth() / 4 + 100, y * mImage.getHeight() / 12,
                            50, 10);
                }
            }
        }
    }

    /**
     * Draw an arrow line betwwen two point
     * @param g the graphic component
     * @param x1 x-position of first point
     * @param y1 y-position of first point
     * @param x2 x-position of second point
     * @param y2 y-position of second point
     * @param d  the width of the arrow
     * @param h  the height of the arrow
     */
    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h){
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy/D, cos = dx/D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}
