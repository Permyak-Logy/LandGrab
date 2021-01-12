package ru.pyply.games.landgrab;

public class Land {
    protected long id;
    protected Wall[] walls;

    Land(Wall[] walls) {}

    public boolean collidePoint(Point point) {
        return false;
    }
}
