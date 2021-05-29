package ru.pyply.games.points.db;

import java.io.Serializable;

public class Game implements Serializable {
    public final int id;
    public int count_players;
    public int target_points;
    public int limit_time;
    public long duration;

    public Game(int id, int count_players, int target_points, int limit_time, long duration) {
        this.id = id;
        this.count_players = count_players;
        this.target_points = target_points;
        this.limit_time = limit_time;
        this.duration = duration;
    }
}
