package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.pyply.games.points.R;
import ru.pyply.games.points.db.DBGames;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button btn = findViewById(R.id.show_statistic_btn);

        DBGames dbGames = new DBGames(this);
        btn.setEnabled(dbGames.selectAllPlayers().size() != 0);
    }

    public void playOffline(View view) {
        Intent i = new Intent(MainActivity.this, PlayOfflineActivity.class);
        startActivity(i);
    }

    public void playOnline(View view) {
        Intent i = new Intent(MainActivity.this, PlayOnlineActivity.class);
        startActivity(i);
    }

    public void showStatistic(View view) {
        Intent i = new Intent(MainActivity.this, StatisticActivity.class);
        startActivity(i);
    }
}