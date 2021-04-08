package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ru.pyply.games.points.views.GameView;

public class Wall implements DrawGameObj {
    public Camp camp_a;
    public Camp camp_b;

    public static final Map<DoublePoint, Wall> walls_map = Collections.synchronizedMap(new HashMap<>());

    public Wall(Camp camp_a, Camp camp_b) {
        this.camp_a = camp_a;
        this.camp_b = camp_b;

        synchronized (walls_map) {
            walls_map.put(new DoublePoint(camp_a.point, camp_b.point), this);
            walls_map.put(new DoublePoint(camp_b.point, camp_a.point), this);
        }
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

    public static void autoCreator(Point point) {
        Camp camp = Camp.map_camps.get(point);
        int[][] dirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        for (int[] dir : dirs) {
            Camp other = Camp.map_camps.get(new Point(point.x + dir[0], point.y + dir[1]));

            if (camp != null && other != null)
                if (camp.team == other.team)
                    if (walls_map.get(new DoublePoint(camp.point, other.point)) == null)
                        new Wall(camp, other);


        }
    }
}
