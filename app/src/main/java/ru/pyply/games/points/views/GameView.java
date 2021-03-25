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

import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.models.Point;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    // ------------------------------------------------------------- Scroll
    private static class GestureListenerOnScroll extends GestureDetector.SimpleOnGestureListener {
        GameView view;

        public GestureListenerOnScroll(GameView view) {
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
    }

    private GestureDetector gesturesOnScroll;

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
        private float zoom = 1;
        private float camera_x = 0;
        private float camera_y = 0;

        // Стандартные свойства сетки
        private final int width_between_lines = 100;

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

        public void drawSheet(Canvas canvas) {
            for (int i = 0; getPosX(i) < canvas.getWidth(); i++) {
                int x = (int) (getPosX(i));
                canvas.drawLine(x, 0, x, canvas.getHeight(), default_sheet_lines);
            }

            for (int i = 0; getPosY(i) < canvas.getHeight(); i++) {
                int y = (int) (getPosY(i));
                canvas.drawLine(0, y, canvas.getWidth(), y, default_sheet_lines);
            }
        }

        public void drawPoints(Canvas canvas) {
            // TODO: Пофиксить дёрганье точек и их качание при пролистывания карты

            for (int i = 0; getPosX(i) <= canvas.getWidth(); i++) {
                for (int j = 0; getPosY(j) <= canvas.getHeight(); j++) {
                    // Координаты точки (в системе), которую мы рисуем
                    Point point = new Point((long) (-this.camera_x / (width_between_lines * zoom)) + i,
                            (long) (-this.camera_y / (width_between_lines * zoom)) + j);
                    Camp camp = Camp.map_camps.get(point);
                    if (camp != null) {
                        camp.draw(canvas, getPosX(i), getPosY(j), zoom);
                    }
                }
            }
        }

        @SuppressWarnings({"unused", "RedundantSuppression"})
        public void drawWalls(Canvas canvas) {

        }

        @SuppressWarnings({"unused", "RedundantSuppression"})
        public void drawLands(Canvas canvas) {

        }

        public int getShiftSheetLinesX() {
            // Даёт сдвиг от края (горизонтального) самой ближайшей линии вдоль Y
            return (int) (this.camera_x % (width_between_lines * zoom));
        }

        public int getShiftSheetLinesY() {
            // Даёт сдвиг от края (вертикального) самой ближайшелй линии вдоль X
            return (int) (this.camera_y % (width_between_lines * zoom));
        }

        public float getPosX(int i) {
            // Даёт координату X на видимой сетке для i перекрестия по оси X
            return getShiftSheetLinesX() + i * width_between_lines * zoom;
        }

        public float getPosY(int i) {
            // Даёт координату Y на видимой сетке для i перекрестия по оси Y
            return getShiftSheetLinesY() + i * width_between_lines * zoom;
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
        gesturesOnScroll = new GestureDetector(getContext(), new GestureListenerOnScroll(this));
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
        gesturesOnScroll.onTouchEvent(event);
        gesturesOnScale.onTouchEvent(event);
        return true;
    }
}
