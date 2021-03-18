package ru.pyply.games.points.models;

import android.media.Image;

public class Player {
    protected long id;
    public Camp[] camps;
    public Team team;
    public Image icon;
    public boolean head_room;

    Player(Image icon, boolean head_room) {}

    public boolean anAlly() {
        return false;
    }

    public boolean me() {
        return false;
    }

    public boolean hisMove() {
        return false;
    }
}
