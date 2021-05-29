package ru.pyply.games.points.db;

import java.io.Serializable;

public class Player implements Serializable {
    public final int id;
    public final String name;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
