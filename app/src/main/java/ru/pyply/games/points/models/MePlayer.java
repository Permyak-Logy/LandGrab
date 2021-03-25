package ru.pyply.games.points.models;

import android.media.Image;

public class MePlayer extends Player {
    public MePlayer(String name) {
        super(name);
    }

    public MePlayer(String name, Image icon, boolean head_room) {
        super(name, icon, head_room);
    }

    public Camp createCamp(Point point) {
        return team.createCamp(point);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void endTurn() {
    }
}
