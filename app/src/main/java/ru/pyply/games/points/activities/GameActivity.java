package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import ru.pyply.games.points.R;
import ru.pyply.games.points.db.DBGames;
import ru.pyply.games.points.fragments.GameInfoFragment;
import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.models.Land;
import ru.pyply.games.points.models.MePlayer;
import ru.pyply.games.points.models.Player;
import ru.pyply.games.points.models.Team;
import ru.pyply.games.points.models.Wall;


public class GameActivity extends AppCompatActivity {

    private static class MyTimer extends CountDownTimer {
        GameActivity activity;

        public MyTimer(long millisInFuture, long countDownInterval, GameActivity activity) {
            super(millisInFuture, countDownInterval);
            this.activity = activity;
        }

        @Override
        public void onFinish() {
            activity.nextMove();

            Toast.makeText(activity, R.string.time_is_up, Toast.LENGTH_SHORT).show();

            Log.i("TimerStep", "Время хода закончилось. Переход к следующему игроку");
        }


        public void onTick(long millisUntilFinished) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            GameInfoFragment gameInfoFragment = (GameInfoFragment) fragmentManager.findFragmentById(R.id.game_info);
            assert gameInfoFragment != null;
            gameInfoFragment.setValueTimer((int) Math.round(millisUntilFinished / 1000.));

        }

    }

    DBGames DBConnector;
    Context mContext;
    MyTimer timerStep;
    int target_camps;
    public boolean running;

    public Team[] teams;
    public int team_move_i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        prepareData();
        initDB();

        target_camps = 5;

        timerStep = new MyTimer(60 * 1000, 499, this);

        MePlayer me_player = new MePlayer("Permyak_Logy");
        Team team_pyply = new Team(new Player[]{me_player}, getResources().getColor(R.color.pyply_team), "PyPLy");
        Team team_nicktozz = new Team(new Player[]{new Player("NicktoZz")}, getResources().getColor(R.color.teal_200), "Immortal");


        teams = new Team[]{team_pyply, team_nicktozz};
        team_move_i = 0;

        this.startGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerStep.cancel();
    }

    public void initDB() {
        DBConnector = new DBGames(this);
        mContext = this;
    }

    public void prepareData() {
        Camp.map_camps.clear();
        Wall.map_walls.clear();
        Land.list_lands.clear();

    }

    public Team checkOnFinishGame() {
        long max_captured = 0;
        Team team_win = null;
        synchronized (Camp.map_camps) {
            Camp[] camps = Camp.map_camps.values().toArray(new Camp[0]);
            for (Team team : teams) {
                long captured = 0;
                for (Camp camp : camps) {
                    if (camp.captured == team) {
                        captured++;
                    }
                }
                if (captured > max_captured) {
                    max_captured = captured;
                    team_win = team;
                }
            }
        }
        if (max_captured >= target_camps) {
            return team_win;
        }
        return null;
    }

    public void startGame() {
        timerStep.cancel();
        timerStep.start();
        running = true;
    }

    public void showWinner(Team team) {
        Toast.makeText(
                this,
                String.format("Победила команда %s во главе с %s",
                        team.getName(), team.players.get(0).name), Toast.LENGTH_LONG).show();
    }

    public void stopGame() {
        timerStep.cancel();
        running = false;
    }

    public void nextMove() {
        timerStep.cancel();

        Team team_win = checkOnFinishGame();
        if (team_win != null) {
            showWinner(team_win);
            stopGame();
            return;
        }

        // Смена хода
        team_move_i = (team_move_i + 1) % teams.length;

        // Показываем на gameInfo
        FragmentManager fragmentManager = getSupportFragmentManager();
        GameInfoFragment gameInfoFragment = (GameInfoFragment) fragmentManager.findFragmentById(R.id.game_info);
        assert gameInfoFragment != null;
        gameInfoFragment.setCurrentTeam(teams[team_move_i]);

        timerStep.start();

    }
}