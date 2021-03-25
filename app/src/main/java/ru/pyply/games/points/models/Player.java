package ru.pyply.games.points.models;

import android.media.Image;

public class Player {
    // TODO: Сделать генерируемым id
    protected long id = 0;
    public Image icon;
    public String name;
    public boolean head_room;
    public Team team;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", head_room=" + head_room +
                ", team=" + team +
                '}';
    }

    public Player(String name, Image icon, boolean head_room) {}

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
