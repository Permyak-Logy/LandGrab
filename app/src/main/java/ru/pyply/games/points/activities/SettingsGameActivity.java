package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import ru.pyply.games.points.R;

public class SettingsGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_game);
    }

    public void saveSettings(View view) {
    }

    public void cancel(View view) {
    }
}