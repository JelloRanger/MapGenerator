package model;

import java.util.Comparator;

public class PointComparator implements Comparator<Point> {

    @Override
    public int compare(Point a, Point b) {
        if (a.getElevation() < b.getElevation()) {
            return -1;
        } else if (a.getElevation() > b.getElevation()) {
            return 1;
        }
        return 0;
    }
}
