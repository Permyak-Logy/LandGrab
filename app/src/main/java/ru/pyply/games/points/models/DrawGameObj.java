package ru.pyply.games.points.models;

import android.graphics.Canvas;

public interface DrawGameObj {
    void draw(Canvas canvas, float cam_x, float cam_y, float zoom);
    boolean isVisibleOnSheet(Canvas canvas, float cam_x, float cam_y, float zoom);
}
