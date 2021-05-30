package ru.pyply.games.points.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

import ru.pyply.games.points.R;
import ru.pyply.games.points.db.DBGames;
import ru.pyply.games.points.db.Player;
import ru.pyply.games.points.db.Result;
import ru.pyply.games.points.db.ResultType;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        DBGames dbGames = new DBGames(this);

        ArrayList<Player> players = dbGames.selectAllPlayers();
        String[] names = new String[players.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = players.get(i).name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        Spinner players_list = findViewById(R.id.players_list);
        players_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateStatsFor(names[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        players_list.setAdapter(adapter);

        ListView statisticList = findViewById(R.id.staticList);
        statisticList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));

        Button btnClearStats = findViewById(R.id.btn_clear_statistic);
        SwitchCompat lock = findViewById(R.id.lock);
        lock.setOnCheckedChangeListener((buttonView, isChecked) -> btnClearStats.setEnabled(!isChecked));
    }

    public void updateStatsFor(String name) {
        DBGames dbGames = new DBGames(this);
        ArrayList<Result> results = dbGames.selectAllResultsWithPlayer(name);

        int count_games = 0;
        int count_victories = 0;
        int total_m_points = 0;
        int total_captured_points = 0;
        int total_lost_points = 0;
        int total_count_moves = 0;
        int total_time_moves = 0;

        for (Result result : results) {
            count_games++;
            if (result.result == ResultType.VICTORY) {
                count_victories++;
            }
            total_m_points += result.m_points;
            total_captured_points += result.captured_points;
            total_lost_points += result.lost_points;
            total_count_moves += result.count_moves;
            total_time_moves += result.total_time_moves;
        }

        ListView listView = findViewById(R.id.staticList);
        //noinspection unchecked
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();
        adapter.clear();
        adapter.add(String.format("Всего игр: %s", count_games));
        adapter.add(String.format("Победы/Поражения: %s/%s", count_victories, count_games - count_victories));
        adapter.add(String.format("Установлено точек: %s (%s в ср.)", total_m_points, Math.round((double) total_m_points / count_games * 100) / 100.));
        adapter.add(String.format("Захвачено точек: %s (%s в ср.)", total_captured_points, Math.round((double) total_captured_points / count_games * 100) / 100.));
        adapter.add(String.format("Потеряно точек: %s (%s в ср.)", total_lost_points, Math.round((double) total_captured_points / count_games * 100) / 100.));
        adapter.add(String.format("Среднее время хода (в сек.): %s", total_time_moves / total_count_moves / 1000.));
    }

    public void clearStatistic(View view) {
        DBGames dbGames = new DBGames(this);
        dbGames.deleteAllResults();
        dbGames.deleteAllGames();
        dbGames.deleteAllPlayers();
        finish();
    }

    public void exit(View view) {
        finish();
    }
}