package ru.pyply.games.points;

import android.graphics.Color;
import android.media.Image;

public class Team {
    public Player[] players;
    public Player leader;
    public Color color;
    public Image icon;

    Team(Player leader, Color color, Image icon) {}

    public boolean joinPlayer(Player player) {
        return false;
    }

    public boolean kickPlayer(Player player) {
        return false;
    }

    public boolean hasPlayer(Player player) {
        return false;
    }

    public boolean changeLeader(Player player) {
        return false;
    }

    public boolean leave() {
        return false;
    }
}
