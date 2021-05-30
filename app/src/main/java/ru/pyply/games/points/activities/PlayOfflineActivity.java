package ru.pyply.games.points.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

import ru.pyply.games.points.R;
import ru.pyply.games.points.views.PlayerListAdapter;

public class PlayOfflineActivity extends AppCompatActivity {
    public static final String EXTRA_TEAMS = "EXTRA_TEAMS";
    public static final String COUNT_PLAYERS_EXTRA = "count_player_extra";
    public static final String SECONDS_FOR_MOVE_EXTRA = "seconds_for_move_extra";
    public static final String TARGET_CAMPS_EXTRA = "target_camps_extra";

    int target_camps = 10;
    int count_players = 2;
    int seconds_for_move = 60;

    PlayerListAdapter.Player[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_offline);
        resetPlayers();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            target_camps = data.getIntExtra(TARGET_CAMPS_EXTRA, 10);
            count_players = data.getIntExtra(COUNT_PLAYERS_EXTRA, 2);
            seconds_for_move = data.getIntExtra(SECONDS_FOR_MOVE_EXTRA, 0);
            resetPlayers();
        }
    }

    public void resetPlayers() {
        players = PlayerListAdapter.makePlayers((byte) count_players);
        PlayerListAdapter adapter = new PlayerListAdapter(this, players);
        ListView lv = findViewById(R.id.players_list);
        lv.setAdapter(adapter);
    }

    public void startOfflineGame(View view) {

        HashSet<Integer> colors = new HashSet<>();
        for (PlayerListAdapter.Player player : players) {
            if (player.nickname.equals("")) {
                Toast.makeText(getApplicationContext(), "Заполнены не все имена игроков", Toast.LENGTH_LONG).show();
                return;
            }
            colors.add(player.color);
        }
        if (colors.size() != players.length) {
            Toast.makeText(getApplicationContext(), "Цвет команды для  каждого игрока должен быть разным", Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(PlayOfflineActivity.this, GameActivity.class);
        i.putExtra(EXTRA_TEAMS, players);
        i.putExtra(TARGET_CAMPS_EXTRA, target_camps);
        i.putExtra(SECONDS_FOR_MOVE_EXTRA, seconds_for_move);
        startActivity(i);
    }

    public void showSettingsOffline(View view) {
        Intent i = new Intent(PlayOfflineActivity.this, SettingsGameActivity.class);
        i.putExtra(TARGET_CAMPS_EXTRA, target_camps);
        i.putExtra(COUNT_PLAYERS_EXTRA, count_players);
        i.putExtra(SECONDS_FOR_MOVE_EXTRA, seconds_for_move);
        startActivityForResult(i, 0);
    }
}