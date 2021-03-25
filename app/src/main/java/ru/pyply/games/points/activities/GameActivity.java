package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.pyply.games.points.R;
import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.models.MePlayer;
import ru.pyply.games.points.models.Point;
import ru.pyply.games.points.models.Team;


// TODO: Для каждой можели сделать адекватный toString()...
public class GameActivity extends AppCompatActivity {

    public Team[] teams;
    public int team_move_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Camp.map_camps.clear();

        teams = new Team[1];

        MePlayer me_player = new MePlayer("Permyak_Logy");

        teams[0] = new Team(me_player,
                getResources().getColor(R.color.purple_700));

        me_player.createCamp(new Point(0, 0));
        me_player.createCamp(new Point(0, 1));
        me_player.createCamp(new Point(2, 2));
        me_player.createCamp(new Point(-5, -5));
    }

    public void nextMove() {
        team_move_i = (team_move_i + 1) % teams.length;
    }
}