package ru.pyply.games.points.models;

import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import ru.pyply.games.points.R;
import ru.pyply.games.points.geometry.Point;


public class Team {
    public ArrayList<Player> players;
    public Paint paintPoints;
    public Paint paintLines;
    public Paint paintLand;

    private String name;

    public static int count_teams = 0;

    public Team(Player[] players_, int color, String name) {
        this(players_, color);
        setName(name);
    }

    public Team(Player[] players_, int color) {
        players = new ArrayList<>();

        paintPoints = new Paint();
        paintPoints.setColor(color);

        paintLines = new Paint();
        paintLines.setColor(color);

        paintLand = new Paint();
        paintLand.setColor(color);
        paintLand.setAlpha(100);

        name = "Команда " + ++count_teams;

        for (Player player : players_) {
            joinPlayer(player);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        return String.format("%s{color=%s}", getClass().getName(), paintPoints.getColor());
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

    public int[] getCampsData() {
        synchronized (Camp.map_camps) {
            Camp[] camps = Camp.map_camps.values().toArray(new Camp[0]);
            int count = 0;
            int captured = 0;
            int lost = 0;

            for (Camp camp : camps) {
                if (camp.team == this) {
                    count++;
                }
                if (camp.captured == this) {
                    captured++;
                }
                if (camp.team == this && camp.captured != null && camp.captured != this) {
                    lost++;
                }
            }
            return new int[]{count, captured, lost};
        }
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player);
    }
}
