package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.pyply.games.points.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViewById(R.id.linear_layout_game_activity);
    }
}