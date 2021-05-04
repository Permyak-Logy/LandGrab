package ru.pyply.games.points.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.pyply.games.points.R;

public class SettingsGameActivity extends AppCompatActivity {
    public static final String COUNT_PLAYERS_EXTRA = "count_player_extra";
    public static final String SECONDS_FOR_MOVE_EXTRA = "seconds_for_move_extra";
    public static final String TARGET_CAMPS_EXTRA = "target_camps_extra";

    static class InputFilterMinMax implements InputFilter {

        private int min, max; //paramets that you send to class

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
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
            } catch (NumberFormatException ignored) {}
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
        int players = i.getIntExtra(COUNT_PLAYERS_EXTRA, 2);
        int seconds = i.getIntExtra(SECONDS_FOR_MOVE_EXTRA, 0);
        int target = i.getIntExtra(TARGET_CAMPS_EXTRA, 1);

        SeekBar count_players = (SeekBar) findViewById(R.id.count_players);
        count_players.setProgress(players - 2);
        TextView count_players_view = (TextView) findViewById(R.id.count_players_view);
        count_players_view.setText(String.valueOf(players));

        Switch limit_time = (Switch) findViewById(R.id.switch_limit_time);
        limit_time.setChecked(seconds == 0);

        EditText seconds_for_move = (EditText) findViewById(R.id.seconds_for_move);
        seconds_for_move.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3600")});
        seconds_for_move.setText(String.valueOf(seconds));
        // TODO: visible

        EditText target_camps = (EditText) findViewById(R.id.target_camps);
        target_camps.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000")});
        target_camps.setText(String.valueOf(target));
    }

    public void saveSettings(View view) {
    }

    public void cancel(View view) {
    }
}