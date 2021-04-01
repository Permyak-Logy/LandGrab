package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.HashMap;

import ru.pyply.games.points.views.GameView;

public class Wall implements DrawGameObj {
    public Camp camp_a;
    public Camp camp_b;

    public static HashMap<DoublePoint, Wall> walls_map = new HashMap<>();

    public Wall(Camp camp_a, Camp camp_b) {
        walls_map.put(new DoublePoint(camp_a.point, camp_b.point), this);

        this.camp_a = camp_a;
        this.camp_b = camp_b;
    }


    @Override
    public void draw(Canvas canvas, float cam_x, float cam_y, float zoom) {
        float x1 = GameView.DrawThread.getRealPosX(camp_a.point.x, cam_x, zoom);
        float y1 = GameView.DrawThread.getRealPosY(camp_a.point.y, cam_y, zoom);

        float x2 = GameView.DrawThread.getRealPosX(camp_b.point.x, cam_x, zoom);
        float y2 = GameView.DrawThread.getRealPosY(camp_b.point.y, cam_y, zoom);

        camp_a.team.paintLines.setStrokeWidth(zoom * 20);
        canvas.drawLine(x1, y1, x2, y2, camp_a.team.paintLines);
    }

    @Override
    public boolean isVisibleOnSheet(Canvas canvas, float cam_x, float cam_y, float zoom) {
        return camp_a.isVisibleOnSheet(canvas, cam_x, cam_y, zoom) || camp_b.isVisibleOnSheet(canvas, cam_x, cam_y, zoom);
    }
}
