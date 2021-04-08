package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import ru.pyply.games.points.R;
import ru.pyply.games.points.db.DBGames;
import ru.pyply.games.points.fragments.GameInfoFragment;
import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.models.MePlayer;
import ru.pyply.games.points.models.Player;
import ru.pyply.games.points.models.Point;
import ru.pyply.games.points.models.Team;
import ru.pyply.games.points.models.Wall;


public class GameActivity extends AppCompatActivity {

    DBGames DBConnector;
    Context mContext;

    public Team[] teams;
    public int team_move_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        prepareData();
        initDB();

        MePlayer me_player = new MePlayer("Permyak_Logy");
        Team team_pyply = new Team(new Player[]{me_player}, getResources().getColor(R.color.pyply_team), "PyPLy");
        Team team_nicktozz = new Team(new Player[]{new Player("NicktoZz")}, getResources().getColor(R.color.teal_200));


        teams = new Team[]{team_pyply, team_nicktozz};

        new Wall(me_player.createCamp(new Point(0, 0)), me_player.createCamp(new Point(0, 1)));
        new Wall(me_player.createCamp(new Point(2, 2)), me_player.createCamp(new Point(-3, -5)));

    }

    public void initDB() {
        DBConnector = new DBGames(this);
        mContext = this;
    }

    public void prepareData() {
        Camp.map_camps.clear();
        Wall.walls_map.clear();
    }

    public void nextMove() {
        team_move_i = (team_move_i + 1) % teams.length;

        FragmentManager fragmentManager = getFragmentManager();
        GameInfoFragment fragment = (GameInfoFragment) fragmentManager.findFragmentById(R.id.game_info);
        fragment.setCurrentTeam(teams[team_move_i]);


    }
}