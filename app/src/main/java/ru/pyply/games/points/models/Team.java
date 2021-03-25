package ru.pyply.games.points.models;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;

import java.util.ArrayList;


public class Team {
    public ArrayList<Player> players;
    public Player leader;
    public Paint paint;
    public Image icon;

    public Team(Player leader, int color) {
        players = new ArrayList<>();
        paint = new Paint();
        paint.setColor(color);

        joinPlayer(leader);
        updatePlayers();
    }

    public Team(Player leader, Color color, Image icon) {
    }

    @Override
    public String toString() {
        return "Team{}";
    }

    public Camp createCamp(Point point) {
        return new Camp(point, this);
    }

    public boolean joinPlayer(Player player) {
        if (player.team != null)
            return false;

        players.add(player);
        player.team = this;

        return true;
    }

    public boolean kickPlayer(Player player) {
        return false;
    }

    public boolean hasPlayer(Player player) {
        return false;
    }

    public void updatePlayers() {
        if (leader == null) {
            leader = players.get(Math.min(players.size() - 1, (int) Math.floor(Math.random() * players.size())));
        }
    }

    public boolean changeLeader(Player player) {
        return false;
    }

    public boolean leave() {
        return false;
    }
}
