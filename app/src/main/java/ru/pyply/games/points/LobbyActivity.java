package ru.pyply.games.points;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }

    public void startOnlineGame(View view) {
        Intent i = new Intent(LobbyActivity.this, GameActivity.class);
        startActivity(i);
    }

    public void showSettingsOnline(View view) {
        Intent i = new Intent(LobbyActivity.this, SettingsGameActivity.class);
        startActivityForResult(i, 0);
    }

    public void leaveLobby(View view) {
        finish();
    }
}