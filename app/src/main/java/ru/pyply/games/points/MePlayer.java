package ru.pyply.games.points;

import android.media.Image;

public class MePlayer extends Player {
    MePlayer(Image icon, boolean head_room) {
        super(icon, head_room);
    }

    public Camp createCamp(Point point) {
        return new Camp(point, this);
    }

    public void endTurn() {}
}
