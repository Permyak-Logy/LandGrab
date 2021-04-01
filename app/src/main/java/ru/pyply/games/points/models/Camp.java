package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.HashMap;

import ru.pyply.games.points.views.GameView;

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
    public void draw(Canvas canvas, float cam_x, float cam_y, float zoom) {
        float x = GameView.DrawThread.getRealPosX(point.x, cam_x, zoom);
        float y = GameView.DrawThread.getRealPosY(point.y, cam_y, zoom);
        canvas.drawCircle(x, y, radius * zoom, team.paintPoints);
    }

    @Override
    public boolean isVisibleOnSheet(Canvas canvas, float cam_x, float cam_y, float zoom) {
        float x = GameView.DrawThread.getRealPosX(point.x, cam_x, zoom);
        float y = GameView.DrawThread.getRealPosY(point.y, cam_y, zoom);
        if (!(-radius <= x && x <= canvas.getWidth() + radius)) {
            return false;
        } else return (-radius <= y && y <= canvas.getHeight() + radius);
    }
}
