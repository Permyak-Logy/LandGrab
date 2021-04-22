package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.pyply.games.points.geometry.Point;
import ru.pyply.games.points.views.GameView;

public class Camp implements DrawGameObj {
    public Team team;
    public Point point;
    public static final int RADIUS = 20;

    public static final Map<Point, Camp> map_camps = Collections.synchronizedMap(new HashMap<>());

    public Camp(Point point, Team team) {


        this.team = team;
        this.point = point;

        synchronized (map_camps) {
            map_camps.put(point, this);
        }
        Wall.autoCreator(point);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return "Camp{" +
                "team=" + team +
                ", point=" + point +
                ", radius=" + RADIUS +
                '}';
    }

    @Override
    public void draw(Canvas canvas, float cam_x, float cam_y, float zoom) {
        float x = GameView.DrawThread.getRealPosX(point.x, cam_x, zoom);
        float y = GameView.DrawThread.getRealPosY(point.y, cam_y, zoom);
        canvas.drawCircle(x, y, RADIUS * zoom, team.paintPoints);
    }

    @Override
    public boolean isVisibleOnSheet(Canvas canvas, float cam_x, float cam_y, float zoom) {
        float x = GameView.DrawThread.getRealPosX(point.x, cam_x, zoom);
        float y = GameView.DrawThread.getRealPosY(point.y, cam_y, zoom);
        if (!(-RADIUS <= x && x <= canvas.getWidth() + RADIUS)) {
            return false;
        } else return (-RADIUS <= y && y <= canvas.getHeight() + RADIUS);
    }
}
