package ru.pyply.games.points.db;

import java.io.Serializable;

public class Result implements Serializable {
    public final String id; // format "{player_id}-{game_id}"
    public final int player_id;
    public final int game_id;
    public int result;
    public int m_points;
    public int captured_points;
    public int lost_points;
    public int count_moves;
    public long total_time_moves;

    public Result(int player_id, int game_id, int result, int m_points, int captured_points, int lost_points, int count_moves, long total_time_moves) {
        this.id = player_id + "-" + game_id;
        this.player_id = player_id;
        this.game_id = game_id;
        this.result = result;
        this.m_points = m_points;
        this.captured_points = captured_points;
        this.lost_points = lost_points;
        this.count_moves = count_moves;
        this.total_time_moves = total_time_moves;
    }
}
