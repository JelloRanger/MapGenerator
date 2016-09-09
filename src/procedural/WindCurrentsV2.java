package procedural;

import model.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Model wind currents. Chooses a random spin direction, and separates wind into 12 zones
 */
public class WindCurrentsV2 {

    private int mHeight;

    private int mSpin = 1; // 1 for west to east (earth), -1 for east to west

    private final double ZONES = 12.0;

    private double mWindCurrentMidpoint;

    public WindCurrentsV2(int height) {
        mHeight = height;
        mWindCurrentMidpoint = height / 12.0;
    }

    public void generate() {
        mSpin = Math.random() > 0.5 ? -1 : 1;
    }

    // give wind direction
    public List<Direction> getDirection(int y) {
        List<Direction> directions = new ArrayList<>();

        switch (normalizeZones(y)) {
            case 0:
            case 1:
                directions.add(Direction.NORTH);
                directions.add(Direction.NORTHEAST);
                directions.add(Direction.EAST);
                break;
            case 2:
            case 3:
                directions.add(Direction.NORTH);
                directions.add(Direction.NORTHWEST);
                directions.add(Direction.WEST);
                break;
            case 4:
            case 5:
                directions.add(Direction.NORTH);
                directions.add(Direction.NORTHEAST);
                directions.add(Direction.EAST);
                break;
            case 6:
            case 7:
                directions.add(Direction.SOUTH);
                directions.add(Direction.SOUTHEAST);
                directions.add(Direction.EAST);
                break;
            case 8:
            case 9:
                directions.add(Direction.SOUTH);
                directions.add(Direction.SOUTHWEST);
                directions.add(Direction.WEST);
                break;
            case 10:
            case 11:
                directions.add(Direction.SOUTH);
                directions.add(Direction.SOUTHEAST);
                directions.add(Direction.EAST);
                break;
            default:
                throw new IllegalArgumentException();
        }

        return directions;
    }

    private int normalizeZones(int value) {
        return (int) (ZONES / mHeight * value);
    }
}
