package ru.pyply.games.points;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlayOfflineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_offline);
    }

    public void startOfflineGame(View view) {
        Intent i = new Intent(PlayOfflineActivity.this, GameActivity.class);
        startActivity(i);
    }

    public void showSettingsOffline(View view) {
        Intent i = new Intent(PlayOfflineActivity.this, SettingsGameActivity.class);
        startActivityForResult(i, 0);
    }
}