package ru.pyply.games.points.models;

import android.graphics.Canvas;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.pyply.games.points.geometry.Point;
import ru.pyply.games.points.geometry.Polygon;
import ru.pyply.games.points.views.GameView;

public class Land implements DrawGameObj {
    protected Team team;
    protected Polygon polygon;

    public static final List<Land> list_lands = Collections.synchronizedList(new ArrayList<>());

    public Land(Team team, Polygon polygon) {
        this.team = team;
        this.polygon = polygon;

        synchronized (list_lands) {
            list_lands.add(this);
        }
    }

    @Override
    public void draw(Canvas canvas, float cam_x, float cam_y, float zoom) {
        Path path = new Path();
        for (int i = 0; i <= polygon.countPoints(); i++) {
            float x = GameView.DrawThread.getRealPosX(polygon.getPoint(i % polygon.countPoints()).x, cam_x, zoom);
            float y = GameView.DrawThread.getRealPosY(polygon.getPoint(i % polygon.countPoints()).y, cam_y, zoom);
            if (i == 0)
                path.moveTo(x, y);
            else
                path.lineTo(x, y);
        }
        canvas.drawPath(path, team.paintLand);
    }

    @Override
    public boolean isVisibleOnSheet(Canvas canvas, float cam_x, float cam_y, float zoom) {
        return true;
    }
}
