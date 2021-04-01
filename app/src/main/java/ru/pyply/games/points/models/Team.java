package ru.pyply.games.points.models;

import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import ru.pyply.games.points.R;


public class Team {
    public ArrayList<Player> players;
    public Paint paint;
    private String name;

    public static int count_teams = 0;

    public Team(Player[] players_, int color, String name) {
        this(players_, color);
        setName(name);
    }

    public Team(Player[] players_, int color) {
        players = new ArrayList<>();
        paint = new Paint();
        paint.setColor(color);

        name = R.string.team + " " + count_teams++;

        for (Player player : players_) {
            joinPlayer(player);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(String name) {
        return name;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return String.format("%s{color=%s}", getClass().getName(), paint.getColor());
    }

    public Camp createCamp(Point point) {
        return new Camp(point, this);
    }

    public void joinPlayer(Player player) {
        if (hasPlayer(player))
            Log.e("TEAM", String.format("Не удалось добавить игрока %s в команду %s (игрок уже в команде)", player, this));
        else if (player.team != null)
            Log.e("TEAM", String.format("Не удалость добавить игрока %s в команду %s (игрок уже в другой команде)", player, this));
        else {
            players.add(player);
            player.team = this;
            Log.i("TEAM", String.format("Игрок %s присоединился к команде %s", player, this));
        }
    }

    public void kickPlayer(Player player) {
        if (!hasPlayer(player))
            Log.e("TEAM", String.format("Не удалось искючить игрока %s из команды %s (игрок не состоит в этой команде)", player, this));
        else {
            players.remove(player);
            player.team = null;
            Log.i("TEAM", String.format("Игрок %s был исключён из команды %s", player, this));
        }
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }
}
