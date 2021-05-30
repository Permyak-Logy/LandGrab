package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import ru.pyply.games.points.R;
import ru.pyply.games.points.db.DBGames;
import ru.pyply.games.points.db.Game;
import ru.pyply.games.points.db.Result;
import ru.pyply.games.points.db.Results;
import ru.pyply.games.points.fragments.GameInfoFragment;
import ru.pyply.games.points.models.Camp;
import ru.pyply.games.points.models.Land;
import ru.pyply.games.points.models.Player;
import ru.pyply.games.points.models.Team;
import ru.pyply.games.points.models.Wall;
import ru.pyply.games.points.views.PlayerAdapter;


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

    private static class Stopwatch {
        private long time_start = -1;
        private long time_end = -1;

        public void start() {
            reset();
            time_start = System.currentTimeMillis();
        }

        public void stop() {
            time_end = System.currentTimeMillis();
        }

        public void reset() {
            time_start = -1;
            time_end = -1;
        }

        public long value() {
            if (time_end != -1 || time_start == -1) {
                return time_end - time_start;
            }
            return System.currentTimeMillis() - time_start;
        }
    }

    private static class DataMoves {
        public int moves = 0;
        public int total_time_moves = 0;
    }

    MyTimer timerStep;
    Stopwatch stopwatchStep;
    Stopwatch stopwatchGame;

    int target_camps;
    int limit_time;


    public boolean running;

    public Team[] teams;
    public int team_move_i = 0;

    public HashMap<Player, DataMoves> statsPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fm = getSupportFragmentManager();
        //noinspection ConstantConditions
        Button btn = fm.findFragmentById(R.id.game_info).getView().findViewById(R.id.button_exit);
        btn.setOnClickListener(v -> finish());

        Intent i = getIntent();
        prepareData();
        prepareTeams((PlayerAdapter.Player[]) i.getSerializableExtra(PlayOfflineActivity.EXTRA_TEAMS));

        target_camps = i.getIntExtra(PlayOfflineActivity.TARGET_CAMPS_EXTRA, 5);

        limit_time = i.getIntExtra(PlayOfflineActivity.SECONDS_FOR_MOVE_EXTRA, 0);
        if (limit_time != 0) {
            timerStep = new MyTimer((long) limit_time * 1000, 499, this);
        }
        stopwatchStep = new Stopwatch();
        stopwatchGame = new Stopwatch();

        updateTeamInfo();
        this.startGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerStep.cancel();
    }

    public DBGames initDB() {
        return new DBGames(this);
    }

    public void prepareData() {
        Camp.map_camps.clear();
        Wall.map_walls.clear();
        Land.list_lands.clear();
        Team.count_teams = 0;

        statsPlayers = new HashMap<>();
    }

    public void prepareTeams(PlayerAdapter.Player[] players) {
        teams = new Team[players.length];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = new Team(new Player[]{new Player(players[i].nickname)}, players[i].color);
            for (Player player : teams[i].players) {
                statsPlayers.put(player, new DataMoves());
            }
        }
    }

    public Team checkOnFinishGame() {
        long max_captured = 0;
        Team team_win = null;
        synchronized (Camp.map_camps) {
            for (Team team : teams) {
                long captured = team.getCampsData()[1];
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
        if (timerStep != null) {
            timerStep.cancel();
            timerStep.start();
        }
        running = true;

        stopwatchGame.start();
        stopwatchStep.start();
    }

    public void stopGame() {
        if (timerStep != null) {
            timerStep.cancel();
        }

        stopwatchStep.stop();
        stopwatchGame.stop();

        running = false;
    }

    public void showWinner(Team team) {
        Toast.makeText(
                this,
                String.format("Победила команда %s во главе с %s",
                        team.getName(), team.players.get(0).name), Toast.LENGTH_LONG).show();
    }


    public void writeResultsToDB(Team winner) {
        DBGames database = initDB();

        for (Player player: statsPlayers.keySet()) {
            if (database.selectPlayer(player.name) == null) {
                database.insertPlayer(player.name);
            }
        }

        //noinspection MathRandomCastToInt
        Game game = new Game((int) Math.random(), statsPlayers.keySet().size(), target_camps, limit_time, stopwatchGame.value());
        database.insertGame(game);

        for (Team team: teams) {
            int[] campsData = team.getCampsData();
            for (Player player: team.players) {
                DataMoves dataMoves = statsPlayers.get(player);
                assert dataMoves != null;
                database.insertResult(new Result(
                        database.selectPlayer(player.name).id,
                        game.id, winner.hasPlayer(player) ? Results.VICTORY : Results.DEFEAT,
                        campsData[0], campsData[1], campsData[2], dataMoves.moves, dataMoves.total_time_moves));
        }}
    }

    public void updateTeamInfo() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GameInfoFragment gameInfoFragment = (GameInfoFragment) fragmentManager.findFragmentById(R.id.game_info);
        assert gameInfoFragment != null;
        gameInfoFragment.setCurrentTeam(teams[team_move_i]);
    }

    public void nextMove() {
        if (timerStep != null) {
            timerStep.cancel();
        }
        stopwatchStep.stop();

        DataMoves dataMoves = statsPlayers.get(teams[team_move_i].players.get(0));
        assert dataMoves != null;
        dataMoves.moves++;
        dataMoves.total_time_moves += stopwatchStep.value();

        Team team_win = checkOnFinishGame();
        if (team_win != null) {
            showWinner(team_win);
            stopGame();
            writeResultsToDB(team_win);
            return;
        }

        // Смена хода
        team_move_i = (team_move_i + 1) % teams.length;

        // Показываем на gameInfo
        updateTeamInfo();

        if (timerStep != null) {
            timerStep.start();
        }
        stopwatchStep.start();
    }
}