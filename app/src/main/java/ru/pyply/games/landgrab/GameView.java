package ru.pyply.games.landgrab;

import android.accessibilityservice.GestureDescription;
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

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GestureDetector gestures;
    public DrawThread drawThread;

    private static class GestureListener implements GestureDetector.OnGestureListener {
        GameView view;

        public GestureListener(GameView view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            System.out.println("onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("onSingleTapUp");
            return false;
        }

        // TODO: Что то не так с этим методом... Не робит
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            System.out.println("onScroll");
            view.drawThread.camera_x += distanceX;
            view.drawThread.camera_y += distanceY;
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            System.out.println("onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("onFling");
            return false;
        }
    }

    public static class DrawThread extends Thread {

        private final SurfaceHolder surfaceHolder;
        private volatile boolean running = true; //флаг для остановки потока

        // Параметры для отображения поля
        private float zoom = 1;
        private double camera_x = 0;
        private double camera_y = 0;

        // Стандартные свойства сетки
        private final int width_between_lines = 100;

        // Цвета для ресования объектов с частными случаями
        public Paint background = new Paint();
        public Paint default_sheet_lines = new Paint();
        public Paint default_sheet_points = new Paint();

        {
            // По умолчанию
            background.setColor(Color.WHITE);
            default_sheet_lines.setColor(Color.GRAY);
            default_sheet_points.setColor(Color.DKGRAY);
        }


        @SuppressWarnings("unused")
        public DrawThread(Context context, SurfaceHolder surfaceHolder) {
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

                        Thread.sleep(1000);
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
            // Определим сдвиг линий по умолчанию
            int shift_x = (int) (camera_x % width_between_lines);
            int shift_y = (int) (camera_y % width_between_lines);

            // Рисуем сетку
            for (int i = 0; i * width_between_lines < canvas.getWidth(); i++) {
                int x = shift_x + i * width_between_lines;
                canvas.drawLine(x, 0, x, canvas.getHeight(), default_sheet_lines);
            }

            for (int i = 0; i * width_between_lines < canvas.getHeight(); i++) {
                int y = shift_y + i * width_between_lines;
                canvas.drawLine(0, y, canvas.getWidth(), y, default_sheet_points);
            }
        }

        public void drawPoints(Canvas canvas) {

        }
    }

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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gestures = new GestureDetector(getContext(), new GestureListener(this));

        drawThread = new DrawThread(getContext(), getHolder());
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
                //
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return gestures.onTouchEvent(event);
    }
}
