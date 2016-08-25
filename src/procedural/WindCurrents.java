package procedural;

/**
 * Model wind currents. Chooses a random spin direction, and separates wind into 12 zones
 */
public class WindCurrents {

    private int mHeight;

    private int mSpin = 1; // 1 for west to east (earth), -1 for east to west

    private final double ZONES = 12.0;

    private double mWindCurrentMidpoint;

    public WindCurrents(int height) {
        mHeight = height;
        mWindCurrentMidpoint = height / 12.0;
    }

    public void generate() {
        mSpin = Math.random() > 0.5 ? -1 : 1;
    }

    // give wind direction and strength
    // direction based on spin, strength based on closeness to center of wind current
    public double getDirection(int y) {
        switch (normalizeZones(y)) {
            case 0:
                return normalizeToMidpoint(mWindCurrentMidpoint - y) * -1 * mSpin;
            case 1:
                return normalizeToMidpoint(y - mWindCurrentMidpoint) * -1 * mSpin;
            case 4:
                return normalizeToMidpoint(mWindCurrentMidpoint*5 - y) * -1 * mSpin;
            case 5:
                return normalizeToMidpoint(y - mWindCurrentMidpoint*5) * -1 * mSpin;
            case 6:
                return normalizeToMidpoint(mWindCurrentMidpoint*7 - y) * -1 * mSpin;
            case 7:
                return normalizeToMidpoint(y - mWindCurrentMidpoint*7)  * -1 * mSpin;
            case 10:
                return normalizeToMidpoint(mWindCurrentMidpoint*11 - y) * -1 * mSpin;
            case 11:
                return normalizeToMidpoint(y - mWindCurrentMidpoint*11) * -1 * mSpin;
            case 2:
                return normalizeToMidpoint(mWindCurrentMidpoint*3 - y) * mSpin;
            case 3:
                return normalizeToMidpoint(y - mWindCurrentMidpoint*3) * mSpin;
            case 8:
                return normalizeToMidpoint(mWindCurrentMidpoint*9 - y) * mSpin;
            case 9:
                return normalizeToMidpoint(y - mWindCurrentMidpoint*9) * mSpin;
            default:
                throw new IllegalArgumentException();
        }
    }

    /*private double getDistanceToNearestMidPoint(int y) {

    }*/

    private double normalizeToMidpoint(double value) {
        return 1 - 1 / mWindCurrentMidpoint * value;
    }

    private int normalizeZones(int value) {
        return (int) (ZONES / mHeight * value);
    }

}
