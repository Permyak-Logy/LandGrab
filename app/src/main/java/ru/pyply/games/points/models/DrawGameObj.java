package ru.pyply.games.points.models;

import android.graphics.Canvas;

public interface DrawGameObj {
    void draw(Canvas canvas, float x, float y, float zoom);
}
