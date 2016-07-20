package noise;

import model.Point;

public class PerlinNoise extends RevisedSmoothNoise {

    protected double persistence;

    protected int octaves;

    private final double ZOOM = 80;

    public PerlinNoise(int width, int height, double seed, double persistence, int octaves) {
        super(width, height, seed);

        this.persistence = persistence;
        this.octaves = octaves;

    }

    @Override
    public void initializeNoiseGrid() {
        super.initializeNoiseGrid();

        perlinElevation();
    }

    @Override
    public void initializeMapGrid() {
        super.initializeMapGrid();

        perlinElevation();
    }

    private void perlinElevation() {
        for (int y = 0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                perlinPoint(mGrid.getPoint(x, y));
            }
        }
    }

    protected void perlinPoint(Point point) {

        double elevation = 0;

        for (int i = 0; i < octaves - 1; i++) {
            double frequency = Math.pow(2, i);
            double amplitude = Math.pow(persistence, i);

            elevation += interpolatedNoise(point.getX() / ZOOM * frequency, point.getY() / ZOOM * frequency) * amplitude;
        }

        point.setElevation(elevation);
    }

    private double interpolatedNoise(double x, double y)  {
        int xCoord = (int) x;
        int yCoord = (int) y;
        double xFract = x - xCoord;
        double yFract = y - yCoord;

        double gradient1 = smoothNoise(xCoord, yCoord);
        double gradient2 = smoothNoise(xCoord + 1, yCoord);
        double gradient3 = smoothNoise(xCoord, yCoord + 1);
        double gradient4 = smoothNoise(xCoord + 1, yCoord + 1);

        double interpolation1 = linearInterpolate(gradient1, gradient2, xFract);
        double interpolation2 = linearInterpolate(gradient3, gradient4, xFract);

        return linearInterpolate(interpolation1, interpolation2, yFract);
    }

    protected double linearInterpolate(double a, double b, double x) {
        return a * (1 - x) + b * x;
    }

}
