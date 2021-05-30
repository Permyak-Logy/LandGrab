package ru.pyply.games.points.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import ru.pyply.games.points.R;

public class SettingsGameActivity extends AppCompatActivity {

    static class InputFilterMinMax implements InputFilter {

        private final int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this(Integer.parseInt(min), Integer.parseInt(max));
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String startString = dest.toString().substring(0, dstart);
                String insert = source.toString();
                String endString = dest.toString().substring(dend);
                String parseThis = startString + insert + endString;
                int input = Integer.parseInt(parseThis);
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException ignored) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_game);
        Intent i = getIntent();
        int players = i.getIntExtra(PlayOfflineActivity.COUNT_PLAYERS_EXTRA, 2);
        int seconds = i.getIntExtra(PlayOfflineActivity.SECONDS_FOR_MOVE_EXTRA, 0); // TODO: Что то не так
        int target = i.getIntExtra(PlayOfflineActivity.TARGET_CAMPS_EXTRA, 1);

        TextView count_players_view = findViewById(R.id.count_players_view);
        count_players_view.setText(String.valueOf(players));

        LinearLayout editor_seconds_for_move = findViewById(R.id.editor_seconds_for_move);

        SeekBar count_players = findViewById(R.id.count_players);
        count_players.setProgress(players - 2);
        count_players.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                count_players_view.setText(String.valueOf(progress + 2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        EditText seconds_for_move = findViewById(R.id.seconds_for_move);
        seconds_for_move.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3600")});
        seconds_for_move.setText(String.valueOf(seconds == 0 ? 60 : seconds));

        SwitchCompat limit_time = findViewById(R.id.switch_limit_time);
        limit_time.setChecked(seconds != 0);
        limit_time.setOnCheckedChangeListener((buttonView, isChecked) -> editor_seconds_for_move.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE));

        EditText target_camps = findViewById(R.id.target_camps);
        target_camps.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000")});
        target_camps.setText(String.valueOf(target));
    }

    public void saveSettings(View view) {
        int count_players = ((SeekBar) findViewById(R.id.count_players)).getProgress() + 2;
        String target_camps = ((EditText) findViewById(R.id.target_camps)).getText().toString();
        String seconds_for_move = ((EditText) findViewById(R.id.seconds_for_move)).getText().toString();
        boolean limit_time = ((SwitchCompat) findViewById(R.id.switch_limit_time)).isChecked();


        if (target_camps.equals("") || (seconds_for_move.equals("") && limit_time)) {
            Toast.makeText(getApplicationContext(), "Поля должны быть не пустыми", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent();
        i.putExtra(PlayOfflineActivity.COUNT_PLAYERS_EXTRA, count_players);
        i.putExtra(PlayOfflineActivity.SECONDS_FOR_MOVE_EXTRA, limit_time ? Integer.parseInt(seconds_for_move) : 0);
        i.putExtra(PlayOfflineActivity.TARGET_CAMPS_EXTRA, Integer.parseInt(target_camps));

        setResult(RESULT_OK, i);
        finish();
    }

    public void cancel(View view) {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }
}