package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import ru.pyply.games.points.R;
import ru.pyply.games.points.views.PlayerAdapter;

public class PlayOfflineActivity extends AppCompatActivity {
    public static final String EXTRA_TEAMS = "EXTRA_TEAMS";

    PlayerAdapter.Player[] players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_offline);

        players = PlayerAdapter.makePlayers((byte) 2);
        PlayerAdapter adapter = new PlayerAdapter(this, players);
        ListView lv = (ListView) findViewById(R.id.players_list);
        lv.setAdapter((ListAdapter) adapter);
    }

    public void startOfflineGame(View view) {

        HashSet<Integer> colors = new HashSet<>();
        for (PlayerAdapter.Player player: players) {
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
        startActivity(i);
    }

    public void showSettingsOffline(View view) {
        Intent i = new Intent(PlayOfflineActivity.this, SettingsGameActivity.class);
        startActivityForResult(i, 0);
    }
}