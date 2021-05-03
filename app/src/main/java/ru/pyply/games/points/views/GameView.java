package ru.pyply.games.points.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import ru.pyply.games.points.activities.GameActivity;
import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.geometry.Point;
import ru.pyply.games.points.models.Land;
import ru.pyply.games.points.models.Wall;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    // ------------------------------------------------------------- Scroll and Double Click
    private static class GestureListenerOnTouch extends GestureDetector.SimpleOnGestureListener {
        GameView view;

        public GestureListenerOnTouch(GameView view) {
            super();
            this.view = view;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            DrawThread dt = view.getDrawThread();
            dt.camera_x -= distanceX / Math.sqrt(dt.zoom);
            dt.camera_y -= distanceY / Math.sqrt(dt.zoom);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!((GameActivity) this.view.getContext()).running) return true;

            DrawThread dt = view.getDrawThread();
            float x = DrawThread.getPointXOnSheet(e.getX(), dt.camera_x, dt.zoom);
            boolean val_x = true;

            float y = DrawThread.getPointYOnSheet(e.getY(), dt.camera_y, dt.zoom);
            boolean val_y = true;

            float r = 0.33f;

            if (Math.abs(x) % 1 <= r)
                x = (float) Math.copySign(Math.floor(Math.abs(x)), x);
            else if (Math.abs(x) % 1 >= 1 - r)
                x = (float) Math.copySign(Math.ceil(Math.abs(x)), x);
            else
                val_x = false;

            if (Math.abs(y) % 1 <= r)
                y = (float) Math.copySign(Math.floor(Math.abs(y)), y);
            else if (Math.abs(y) % 1 >= 1 - r)
                y = (float) Math.copySign(Math.ceil(Math.abs(y)), y);
            else
                val_y = false;

            if (val_x && val_y) {
                Point point = new Point((long) x, (long) y);
                GameActivity activity = (GameActivity) view.getContext();

                if (Camp.map_camps.get(point) == null) {
                    activity.teams[activity.team_move_i].createCamp(point);
                    activity.nextMove();
                }
            }

            return true;
        }
    }

    private GestureDetector gesturesOnTouch;

    // ------------------------------------------------------------- Scale
    private static class GestureListenerOnScale extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        GameView view;

        public GestureListenerOnScale(GameView view) {
            super();
            this.view = view;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            super.onScale(detector);

            DrawThread dt = view.getDrawThread();
            dt.zoom *= detector.getScaleFactor();
            dt.zoom = Math.max(0.1f, Math.min(dt.zoom, 5.0f));
            return true;
        }
    }

    private ScaleGestureDetector gesturesOnScale;

    // ------------------------------------------------------------- Draw Tread
    public static class DrawThread extends Thread {

        private final SurfaceHolder surfaceHolder;

        private volatile boolean running = true; //флаг для остановки потока

        // Параметры для отображения поля
        public float zoom = 1;
        public float camera_x = 0;
        public float camera_y = 0;

        // Стандартные свойства сетки
        public static final int WIDTH_BETWEEN_LINES = 100;

        // Цвета для ресования объектов с частными случаями
        public Paint background = new Paint();
        public Paint default_sheet_lines = new Paint();

        {
            // По умолчанию
            background.setColor(Color.WHITE);
            default_sheet_lines.setColor(Color.LTGRAY);
        }


        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void requestStop() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        drawBackground(canvas);
                        drawSheet(canvas);
                        drawPoints(canvas);
                        drawWalls(canvas);
                        drawLands(canvas);

                        //noinspection BusyWait
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public void drawBackground(Canvas canvas) {
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), background);
        }

        // TODO: Пофиксить аномальные ошиюки с вылетами (Возможно из-за добавления новых объектов во время отрисовки)
        public void drawSheet(Canvas canvas) {
            for (int i = 0; getPosX(i, camera_x, zoom) < canvas.getWidth(); i++) {
                int x = (int) (getPosX(i, camera_x, zoom));
                canvas.drawLine(x, 0, x, canvas.getHeight(), default_sheet_lines);
            }

            for (int i = 0; getPosY(i, camera_y, zoom) < canvas.getHeight(); i++) {
                int y = (int) (getPosY(i, camera_y, zoom));
                canvas.drawLine(0, y, canvas.getWidth(), y, default_sheet_lines);
            }
        }

        public void drawPoints(Canvas canvas) {
            synchronized (Camp.map_camps) {
                Camp[] camps = Camp.map_camps.values().toArray(new Camp[0]);

                for (Camp camp : camps) {
                    if (camp.isVisibleOnSheet(canvas, camera_x, camera_y, zoom)) {
                        camp.draw(canvas, camera_x, camera_y, zoom);
                    }
                }
            }
        }

        public void drawWalls(Canvas canvas) {
            synchronized (Wall.map_walls) {
                Wall[] walls = Wall.map_walls.values().toArray(new Wall[0]);
                for (Wall wall : walls) {
                    if (wall.isVisibleOnSheet(canvas, camera_x, camera_y, zoom)) {
                        wall.draw(canvas, camera_x, camera_y, zoom);
                    }
                }
            }
        }

        public void drawLands(Canvas canvas) {
            synchronized (Land.list_lands) {
                Land[] lands = Land.list_lands.toArray(new Land[0]);
                for (Land land : lands) {
                    if (land.isVisibleOnSheet(canvas, camera_x, camera_y, zoom)) {
                        land.draw(canvas, camera_x, camera_y, zoom);
                    }
                }
            }
        }

        public static Point getLeftTopPointOnSheet(float camera_x, float camera_y, float zoom) {
            return new Point((long) (-camera_x / (WIDTH_BETWEEN_LINES * zoom)),
                    (long) (-camera_y / (WIDTH_BETWEEN_LINES * zoom)));
        }

        public static int getShiftSheetLinesX(float camera_x, float zoom) {
            // Даёт сдвиг от края (горизонтального) самой ближайшей линии вдоль Y
            return (int) (camera_x % (WIDTH_BETWEEN_LINES * zoom));
        }

        public static int getShiftSheetLinesY(float camera_y, float zoom) {
            // Даёт сдвиг от края (вертикального) самой ближайшелй линии вдоль X
            return (int) (camera_y % (WIDTH_BETWEEN_LINES * zoom));
        }

        public static float getPosX(int i, float camera_x, float zoom) {
            // Даёт координату X на видимой сетке для i перекрестия по оси X
            return getShiftSheetLinesX(camera_x, zoom) + i * WIDTH_BETWEEN_LINES * zoom;
        }

        public static float getPosY(int i, float camera_y, float zoom) {
            // Даёт координату Y на видимой сетке для i перекрестия по оси Y
            return getShiftSheetLinesY(camera_y, zoom) + i * WIDTH_BETWEEN_LINES * zoom;
        }

        public static float getRealPosX(long i, float camera_x, float zoom) {
            // Даёт координату X на всей сетке для i перекрестия по оси X
            return WIDTH_BETWEEN_LINES * zoom * i + camera_x;
        }

        public static float getRealPosY(long i, float camera_y, float zoom) {
            // Даёт координату X на всей сетке для i перекрестия по оси Y
            return WIDTH_BETWEEN_LINES * zoom * i + camera_y;
        }

        public static float getPointXOnSheet(float point_x, float camera_x, float zoom) {
            return (point_x - camera_x) / (WIDTH_BETWEEN_LINES * zoom);
        }

        public static float getPointYOnSheet(float point_y, float camera_y, float zoom) {
            return (point_y - camera_y) / (WIDTH_BETWEEN_LINES * zoom);
        }
    }

    public DrawThread drawThread;

    // ------------------------------------------------------------- Init
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public DrawThread getDrawThread() {
        return this.drawThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gesturesOnTouch = new GestureDetector(getContext(), new GestureListenerOnTouch(this));
        gesturesOnScale = new ScaleGestureDetector(getContext(), new GestureListenerOnScale(this));

        drawThread = new DrawThread(getHolder());
        drawThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.requestStop();
        boolean retry = true;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gesturesOnTouch.onTouchEvent(event);
        gesturesOnScale.onTouchEvent(event);
        return true;
    }
}
