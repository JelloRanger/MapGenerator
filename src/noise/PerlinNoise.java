package noise;

import model.Point;

public class PerlinNoise extends RevisedSmoothNoise {

    protected double persistence;

    protected int octaves;

    //private final double ZOOM = 80;

    private final double ZOOM = 200;

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

            elevation += interpolatedNoise2(point.getX() / ZOOM * frequency, point.getY() / ZOOM * frequency) * amplitude;
        }

        point.setElevation(elevation);
    }


    // DEPRECTED - Original perlin noise algorithm, suffers from square artifacting when shading
    private double interpolatedNoise(double x, double y)  {
        int xCoord = (int) x;
        int yCoord = (int) y;
        double xFract = x - xCoord;
        double yFract = y - yCoord;

        double gradient1 = noise(xCoord, yCoord);
        double gradient2 = noise(xCoord + 1, yCoord);
        double gradient3 = noise(xCoord, yCoord + 1);
        double gradient4 = noise(xCoord + 1, yCoord + 1);

        double interpolation1 = linearInterpolate(gradient1, gradient2, xFract);
        double interpolation2 = linearInterpolate(gradient3, gradient4, xFract);

        return linearInterpolate(interpolation1, interpolation2, yFract);
    }

    // Improved perlin noise algorithm, solves the issue of square artifacts when shading
    private double interpolatedNoise2(double x, double y) {
        int xCoord = (int) x;
        int yCoord = (int) y;
        double xFract = x - xCoord;
        double yFract = y - yCoord;

        double u = fade(xFract);
        double v = fade(yFract);

        return linearInterpolate(
                linearInterpolate(noise(xCoord, yCoord), noise(xCoord + 1, yCoord), u),
                linearInterpolate(noise(xCoord, yCoord + 1), noise(xCoord + 1, yCoord + 1), u),
                v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double linearInterpolate(double a, double b, double x) {
        return a * (1 - x) + b * x;
    }

}
