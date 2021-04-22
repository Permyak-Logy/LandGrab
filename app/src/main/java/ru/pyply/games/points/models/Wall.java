package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import ru.pyply.games.points.geometry.DoublePoint;
import ru.pyply.games.points.geometry.Point;
import ru.pyply.games.points.geometry.Polygon;
import ru.pyply.games.points.views.GameView;

public class Wall implements DrawGameObj {
    protected static class ElemChain {
        public final Camp camp;
        public int anInt;

        public ElemChain(Camp a) {
            this.camp = a;
            this.anInt = -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ElemChain pair = (ElemChain) o;
            return camp.equals(pair.camp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(camp);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public String toString() {
            return "ElemChain{" +
                    "camp=" + camp +
                    ", anInt=" + anInt +
                    '}';
        }
    }

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

        Stack<ElemChain> campStack = new Stack<>(); // Линия сборки территории
        Camp camp = Camp.map_camps.get(point);
        campStack.add(new ElemChain(camp));

        // Пытемся создать самую большую територию
        Polygon max_land = null;
        double max_square = 0;

        while (!campStack.isEmpty()) {
            if (campStack.size() > 1)
                campStack.pop();
            try_fill_chain(campStack);
            if (!campStack.isEmpty()) {
                Polygon polygon = new Polygon();
                for (int i = 0; i < campStack.size() - 1; i++) {
                    polygon.addPoint(campStack.get(i).camp.point);
                }
                double square = polygon.getSquare();
                System.out.println(square + " " + polygon.getPoint(0) + " " + polygon.getPoint(1) + polygon.getPoint(polygon.countPoints() - 1));
                if (max_square < square) {
                    max_land = polygon;
                    max_square = square;
                }
            }
        }

        //noinspection ConstantConditions
        System.out.println("Образовалась территория? " + max_land != null);
        if (max_land != null) {
            for (int i = 0; i < max_land.countPoints(); i++) {
                Camp a = Camp.map_camps.get(max_land.getPoint(i));
                Camp b = Camp.map_camps.get(max_land.getPoint(i == max_land.countPoints() - 1 ? 0 : i + 1));
                assert a != null && b != null;
                if (walls_map.get(new DoublePoint(a.point, b.point)) == null)
                    new Wall(a, b);
            }
        }


        /*
        for (int[] dir : dirs) {
            Camp other = Camp.map_camps.get(new Point(point.x + dir[0], point.y + dir[1]));
            if (camp != null && other != null)
                if (camp.team == other.team)
                    if (walls_map.get(new DoublePoint(camp.point, other.point)) == null)
                        new Wall(camp, other);


        }
        */
    }

    protected static void try_fill_chain(Stack<ElemChain> campStack) {
        boolean isClosed = false;

        // Пробуем собрать цепь
        while (!campStack.isEmpty() && !isClosed) {
            ElemChain cur_elem = campStack.peek();
            // Ищем куда пойти нам
            while (cur_elem.anInt < 9) {
                cur_elem.anInt++;
                int x = cur_elem.anInt % 3 - 1, y = cur_elem.anInt / 3 % 3 - 1;

                // Ход в одну и ту же точку исключаем
                if (!(x == y && x == 0)) {
                    Camp new_camp = Camp.map_camps.get(
                            new Point(cur_elem.camp.point.x + x,
                                    cur_elem.camp.point.y + y)
                    );
                    if (new_camp != null && new_camp.team == campStack.peek().camp.team) {
                        ElemChain new_elem = new ElemChain(new_camp);
                        boolean isFirst = campStack.firstElement().camp == new_camp;

                        if (!campStack.contains(new_elem) || (isFirst && campStack.size() >= 4)) {
                            campStack.add(new_elem);
                            if (isFirst) isClosed = true;
                            break;
                        }
                    }
                }
            }
            if (cur_elem.anInt == 9)
                campStack.pop();
        }
    }
}
