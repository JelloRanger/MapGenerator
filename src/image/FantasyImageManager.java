package image;

import map.PerlinMap;
import metrics.Metric;
import metrics.MetricKey;
import model.LocationType;
import model.Point;
import model.Terrain;
import model.TerrainType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FantasyImageManager {

    private static final String TAG = FantasyImageManager.class.getSimpleName();

    private PerlinMap mMap;

    private BufferedImage mImage;

    private List<Point> mMountains;

    public FantasyImageManager(PerlinMap map) {
        mMap = map;
        mImage = new BufferedImage(mMap.getWidth(), mMap.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        mMountains = new ArrayList<>();
    }

    public void generate() {
        colorMap();
    }

    public BufferedImage getImage() {
        return mImage;
    }

    protected void colorMap() {
        Metric.start(MetricKey.FANTASYCOLORMAP);
        mImage.getGraphics().fillRect(0, 0, mImage.getWidth(), mImage.getHeight());
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                colorByTerrain(terrain);
            }
        }
        colorCities();
        colorNames();
        Metric.record(MetricKey.FANTASYCOLORMAP);
    }

    private void colorCities() {
        if (!mMap.isCitiesEnabled()) {
            return;
        }

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType().equals(LocationType.CITY)) {
                    Graphics graphics = mImage.getGraphics();
                    graphics.setColor(Color.black);
                    graphics.fillOval(x - 8/2, y - 8/2, 8, 8);
                }
            }
        }
    }

    private void colorNames() {
        if (!mMap.isNamesEnabled()) {
            return;
        }

        Graphics graphics = mImage.getGraphics();
        graphics.setFont(new Font("Mercury", Font.BOLD, 14)); //Papyrus, Plantin
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType().equals(LocationType.CITY)) {
                    graphics.setColor(Color.black);
                    graphics.drawString(terrain.getLocation().getName(), x - 8 * 4, y - 8);
                }
            }
        }
    }

    private void colorByTerrain(Terrain terrain) {

        if (terrain.getTerrainType().equals(TerrainType.LAND)) {
            Set<TerrainType> adjacentTerrain = mMap.getNoise().getGrid().getAdjacentTerrainTypes(terrain, 1);
            if (adjacentTerrain.contains(TerrainType.BEACH) || adjacentTerrain.contains(TerrainType.WATER)) {
                mImage.setRGB(terrain.getX(), terrain.getY(), Color.black.getRGB());
            }
        } else if (terrain.getTerrainType().equals(TerrainType.RIVER)) {
            Set<TerrainType> adjacentTerrain = mMap.getNoise().getGrid().getAdjacentTerrainTypes(terrain, 1);
            if (adjacentTerrain.contains(TerrainType.LAND)) {
                mImage.setRGB(terrain.getX(), terrain.getY(), Color.black.getRGB());
            } else {
                // Make water transparent
                mImage.setRGB(terrain.getX(), terrain.getY(), 0);
            }
        } else if (terrain.getTerrainType().equals(TerrainType.HILL)) {
            if (Math.random() < 0.2) {
                for (Point mountain : mMountains) {
                    if (distance(terrain.getX(), terrain.getY(), mountain.getX(), mountain.getY()) <= 20) {
                        return;
                    }
                }
                drawHill(terrain.getX(), terrain.getY());
                mMountains.add(terrain);
            }
        } else if (terrain.getTerrainType().equals(TerrainType.MOUNTAIN)) {
            if (Math.random() < 0.2) {
                for (Point mountain : mMountains) {
                    if (distance(terrain.getX(), terrain.getY(), mountain.getX(), mountain.getY()) <= 20) {
                        return;
                    }
                }
                drawMountain(terrain.getX(), terrain.getY());
                mMountains.add(terrain);
            }
        } else {
            // Make water transparent
            mImage.setRGB(terrain.getX(), terrain.getY(), 0);
        }
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void drawHill(int x, int y) {
        Graphics graphics = mImage.getGraphics();
        graphics.setColor(Color.black);

        List<Point> points = new LinkedList<>();
        points.add(new Point(x, y));
        points.add(new Point(x, y - 3));
        points.add(new Point(x + 3, y - 6));
        points.add(new Point(x + 8, y - 6));
        points.add(new Point(x + 11, y - 3));
        points.add(new Point(x + 11, y));

        for (Point point : points) {
            point.setY(point.getY() + (int) ((Math.random() - 0.5) * 3));
        }

        Point currPoint = points.remove(0);
        Point oldPoint = currPoint;
        while (!points.isEmpty()) {
            currPoint = points.remove(0);
            graphics.drawLine(oldPoint.getX(), oldPoint.getY(), currPoint.getX(), currPoint.getY());
            oldPoint = currPoint;
        }
    }

    private void drawMountain(int x, int y) {

        Graphics graphics = mImage.getGraphics();
        graphics.setColor(Color.black);

        List<Point> points = new LinkedList<>();
        points.add(new Point(x, y));
        points.add(new Point(x += 4, y -= 4));
        points.add(new Point(x += 4, y -= 4));
        points.add(new Point(x += 4, y -= 4));

        points.add(new Point(x += 4, y += 4));
        points.add(new Point(x += 4, y += 4));
        points.add(new Point(x += 4, y += 4));


        for (Point point : points) {
            point.setY(point.getY() + (int) ((Math.random() - 0.5) * 10));
        }

        Point currPoint = points.remove(0);
        Point oldPoint = currPoint;
        while (!points.isEmpty()) {
            currPoint = points.remove(0);
            graphics.drawLine(oldPoint.getX(), oldPoint.getY(), currPoint.getX(), currPoint.getY());
            oldPoint = currPoint;
        }
    }
}
