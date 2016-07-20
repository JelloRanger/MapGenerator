package procedural;

import map.PerlinMap;
import model.*;

import java.util.PriorityQueue;
import java.util.Stack;

public class LakesAndRiversGeneration {

    private PerlinMap map;

    private Grid waterGrid;

    private final int NUM_STARTING_POINTS = 10;

    private final int QUADRANTS = 500;

    private Stack<Point> riverStartingPoints = new Stack<>();

    public LakesAndRiversGeneration(PerlinMap map) {
        this.map = map;
    }

    public void generate() {
        waterGrid = new Grid(map.getWidth(), map.getHeight());
        waterGrid.initializeGrid();

        PriorityQueue<Point> highestPoints = new PriorityQueue<>(new PointComparator());

        // get highest elevation points per quadrant
        for (int yQuadrant = 0; yQuadrant < map.getHeight() / QUADRANTS; yQuadrant++) {
            for (int xQuadrant = 0; xQuadrant < map.getWidth() / QUADRANTS; xQuadrant++) {

                highestPoints.add(new Point(-2.0));
                for (int y = yQuadrant * QUADRANTS; y < yQuadrant * QUADRANTS + QUADRANTS; y++) {
                    for (int x = xQuadrant * QUADRANTS; x < xQuadrant * QUADRANTS + QUADRANTS; x++) {
                        highestPoints.add(map.getNoise().getGrid().getPoint(x, y));
                        highestPoints.poll();
                    }
                }
                riverStartingPoints.add(highestPoints.poll());
            }
        }

        flowDownhill();
    }

    private void flowDownhill() {
        Point currentPoint;

        PriorityQueue<Point> adjacentPoints = new PriorityQueue<>(new PointComparator());

        while (!riverStartingPoints.isEmpty()) {
            currentPoint = riverStartingPoints.pop();
            setRiver(currentPoint);

            while (true) {
                adjacentPoints.addAll(map.getNoise().getGrid().getDirectlyAdjacentPoints(currentPoint));
                if (isWater(adjacentPoints.peek())) {
                    adjacentPoints.clear();
                    break;
                }

                while (isRiver(adjacentPoints.peek())) {
                    adjacentPoints.poll();
                }

                if (adjacentPoints.isEmpty()) {
                    break;
                }

                currentPoint = adjacentPoints.peek();
                setRiver(currentPoint);
                adjacentPoints.clear();
            }
        }

    }

    private void setRiver(Point point) {
        map.getTerrain(point.getX(), point.getY()).setTerrainType(TerrainType.RIVER);
    }

    private boolean isWater(Point point) {
        return map.getTerrain(point.getX(), point.getY()).getTerrainType() == TerrainType.WATER;
    }

    private boolean isRiver(Point point) {
        Terrain terrain = (Terrain) point;
        if (terrain != null && terrain.getTerrainType() != null && terrain.getTerrainType().equals(TerrainType.RIVER)) {
            return true;
        }

        return false;
    }
}
