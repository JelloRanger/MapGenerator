package noise;

import model.Grid;
import model.Point;

import java.util.List;

/**
 * Generates an N x N grid where points closer to the edges have lower elevations and points closer to the center have
 * higher elevations
 */
public class ContinentNoise extends Noise {

    private final double MIN_ELEVATION = -1.0;

    private final double MAX_ELEVATION = 1.0;

    private int mMaxDistance;

    public ContinentNoise(int width, int height) {
        mWidth = width;
        mHeight = height;
        mMaxDistance = mWidth / 2 < mHeight / 2 ? mWidth / 2 - 1: mHeight / 2;
    }

    @Override
    public void initializeNoiseGrid() {
        mGrid = new Grid(mWidth, mHeight);
        mGrid.initializeGrid();
        genNoise();
    }

    @Override
    public void initializeMapGrid() {
        mGrid = new Grid(mWidth, mHeight);
        mGrid.initializeGridWithTerrain();
        genNoise();
    }

    @Override
    protected void genNoise() {
        for (List<Point> row : mGrid.getGrid()) {
            for (Point point : row) {
                point.setElevation(determineElevation(point.getX(), point.getY()));
            }
        }
    }

    private double determineElevation(int x, int y) {
        int distX = getDistanceFromEdgeX(x);
        int distY = getDistanceFromEdgeY(y);
        int minDistFromEdge = distX < distY ? distX : distY;

        return (MAX_ELEVATION - MIN_ELEVATION) * minDistFromEdge / mMaxDistance + MIN_ELEVATION;
    }

    private int getDistanceFromEdgeX(int x) {
        return mWidth - x - 1 < x ? mWidth - x - 1 : x;
    }

    private int getDistanceFromEdgeY(int y) {
        return mHeight - y - 1 < y ? mHeight - y - 1 : y;
    }
}
