package ru.pyply.games.points.models;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import ru.pyply.games.points.BuildConfig;
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

    public static final Map<DoublePoint, Wall> map_walls = Collections.synchronizedMap(new HashMap<>());

    public Wall(Camp camp_a, Camp camp_b) {
        this.camp_a = camp_a;
        this.camp_b = camp_b;

        if (BuildConfig.DEBUG && Math.abs(Math.pow(camp_a.point.x - camp_b.point.x, 2) + Math.pow(camp_a.point.y - camp_b.point.y, 2)) > 2) {
            throw new AssertionError(String.format("Very big long of wall for %s %s", camp_a.point, camp_b.point));
        }

        synchronized (map_walls) {
            map_walls.put(new DoublePoint(camp_a.point, camp_b.point), this);
            map_walls.put(new DoublePoint(camp_b.point, camp_a.point), this);
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

    public static void autoCreator(Camp camp) {

        Stack<ElemChain> campStack = new Stack<>(); // Линия сборки территории
        campStack.add(new ElemChain(camp));

        // Пытемся создать самую большую територию
        Polygon max_polygon = null;
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
                if (max_square < square) {
                    max_polygon = polygon;
                    max_square = square;
                }
            }
        }
        if (max_polygon == null)
            return;

        // Проверяем на захват хотя бы 1 врага
        boolean any_grab = false;
        synchronized (Camp.map_camps) {
            Camp[] camps = Camp.map_camps.values().toArray(new Camp[0]);

            for (Camp enemy_camp : camps) {
                if (enemy_camp.team != camp.team && enemy_camp.captured == null) {
                    boolean captured = max_polygon.contains(enemy_camp.point);
                    if (captured) {
                        any_grab = true;
                        enemy_camp.captured = camp.team;
                    }
                }
            }
        }

        // Создаём стены
        if (any_grab) {
            for (int i = 0; i < max_polygon.countPoints(); i++) {
                Camp a = Camp.map_camps.get(max_polygon.getPoint(i));
                Camp b = Camp.map_camps.get(max_polygon.getPoint(i == max_polygon.countPoints() - 1 ? 0 : i + 1));
                assert a != null && b != null;
                if (map_walls.get(new DoublePoint(a.point, b.point)) == null)
                    new Wall(a, b);
            }

            new Land(camp.team, max_polygon);
        }
    }

    protected static void try_fill_chain(Stack<ElemChain> campStack) {
        // TODO: Убрать включение старых территорий
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
                    if (new_camp != null && new_camp.team == campStack.peek().camp.team && new_camp.captured == null) {
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
