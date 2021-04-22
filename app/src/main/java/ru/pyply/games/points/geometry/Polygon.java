package ru.pyply.games.points.geometry;

import java.util.ArrayList;
import java.util.Collections;

public class Polygon {
    private final ArrayList<Long> xpoints;
    private final ArrayList<Long> ypoints;
    private int npoints;

    public Polygon() {
        this.xpoints = new ArrayList<>();
        this.ypoints = new ArrayList<>();
        this.npoints = 0;
    }

    public Polygon(Point[] points) {
        this();
        for (Point point : points) {
            addPoint(point);
        }
    }

    public void addPoint(Point point) {
        addPoint(point.x, point.y);
    }

    public void addPoint(long x, long y) {
        xpoints.add(x);
        ypoints.add(y);
        npoints++;
    }

    public Point getPoint(int i) {
        if (!(0 <= i && i < npoints))

            return null;
        return new Point(xpoints.get(i), ypoints.get(i));
    }

    public int countPoints() {
        return npoints;
    }

    public double getSquare() {
        // TODO: Проверить на работоспособность
        // Находится по формуле пика
        int a = 0;
        int b = 0;
        for (long x = Collections.min(xpoints); x <= Collections.max(xpoints); x++) {
            for (long y = Collections.min(ypoints); y <= Collections.max(ypoints); y++) {
                Point point = new Point(x, y);
                if (containsPoint(point))
                    a++;
                else if (contains(x, y))
                    b++;

            }
        }
        System.out.println("Граница " + a + " Входит " + b);
        return a / 2. + b - 1;
    }

    private boolean isOutside(Point point) {
        long tlx = Collections.min(xpoints), tly = Collections.min(ypoints);
        long brx = Collections.max(xpoints), bry = Collections.min(ypoints);

        if (!(tlx <= point.x && point.x <= tly))
            return false;
        return brx <= point.y && point.y <= bry;
    }

    private boolean containsPoint(Point point) {
        Point check;

        for (int i = 0; i < npoints; i++) {
            check = getPoint(i);
            assert check != null;
            if (check.x == point.x && check.y == point.y) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(long x, long y) {
        return contains(new Point(x, y));
    }

    public boolean contains(Point point) {

        if (npoints < 3 || isOutside(point))
            return false;

        if (containsPoint(point))
            return false;

        int result = 1;
        for (int i = 0; i < npoints; i++) {
            Point a = getPoint(i);
            Point b = getPoint(i == npoints - 1 ? 0 : i + 1);
            result *= check_point(a, b, point);
        }
        return result < 0;
    }

    private int sign(long number) {
        if (number > 0)
            return 1;
        if (number < 0)
            return -1;
        return 0;
    }

    private int check_point(Point a, Point b, Point point) {
        long s_ax = a.x;
        long s_ay = a.y;
        long s_bx = b.x;
        long s_by = b.y;
        long s_mx = point.x;
        long s_my = point.y;

        long ax = s_ax - s_mx;
        long ay = s_ay - s_my;
        long bx = s_bx - s_mx;
        long by = s_by - s_my;

        int s = sign(ax * by - ay * bx);
        if (s == 0 && (ay == 0 || by == 0) && ax * bx <= 0)
            return 0;
        if ((ay < 0) ^ (by < 0)) {
            if (by < 0)
                return s;
            return -s;
        }
        return 1;
    }
}
