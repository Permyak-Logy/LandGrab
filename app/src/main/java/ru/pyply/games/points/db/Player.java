package ru.pyply.games.points.db;

import java.io.Serializable;

public class Player implements Serializable {
    public final long id;
    public final String name;

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
