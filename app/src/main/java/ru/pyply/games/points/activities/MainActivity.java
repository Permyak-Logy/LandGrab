package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.pyply.games.points.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playOffline(View view) {
        Intent i = new Intent(MainActivity.this, PlayOfflineActivity.class);
        startActivity(i);
    }

    public void playOnline(View view) {
        Intent i = new Intent(MainActivity.this, PlayOnlineActivity.class);
        startActivity(i);
    }

    public void showAccount(View view) {
        Intent i = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(i);
    }
}