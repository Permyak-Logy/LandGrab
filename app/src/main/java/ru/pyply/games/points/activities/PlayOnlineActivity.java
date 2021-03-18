package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.pyply.games.points.R;

public class PlayOnlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online);
    }

    public void createLobby(View view) {
        Intent i = new Intent(PlayOnlineActivity.this, LobbyActivity.class);
        startActivity(i);
    }
}