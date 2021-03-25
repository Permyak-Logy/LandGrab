package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.HashMap;

public class Camp implements DrawGameObj {
    public Team team;
    public Point point;
    public final int radius = 20;

    static public HashMap<Point, Camp> map_camps = new HashMap<>();

    public Camp(Point point, Team team) {
        map_camps.put(point, this);
        this.team = team;
        this.point = point;

        System.out.println("Created: " + this);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Camp{" +
                "team=" + team +
                ", point=" + point +
                ", radius=" + radius +
                '}';
    }

    @Override
    public void draw(Canvas canvas, float x, float y, float zoom) {
        System.out.println("draw" + point);
        canvas.drawCircle(x, y, (float) (radius * zoom), team.paint);
    }
}
