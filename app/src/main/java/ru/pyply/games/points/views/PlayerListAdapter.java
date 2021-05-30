package ru.pyply.games.points.views;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

import ru.pyply.games.points.R;

public class PlayerListAdapter extends ArrayAdapter<PlayerListAdapter.Player> {
    static final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.WHITE,
            Color.YELLOW, Color.CYAN, Color.MAGENTA};

    public static class Player implements Serializable {
        private String[] nicks = {"Вася", "Петя", "Шнюк", "Лунтик", "Сапёр", "Ноидор", "Синед",
                "Ятеп", "Ясав", "Анкл", "Пидав", "Шлякер", "Коря", "ХХХ", "Пятка", "Утка", "Дед",
                "Оброей", "Йеахю", "Линдер", "Мзесо", "Депутат"};

        public String nickname = "";
        int color_i = 0;
        public int color = colors[color_i];

        public String setRandNick() {
            nickname = nicks[(int) (Math.random() * 1000) % nicks.length];
            return nickname;
        }
    }

    public PlayerListAdapter(Context context, Player[] arr) {
        super(context, R.layout.adapter_player, arr);
    }

    public static Player[] makePlayers(byte count) {
        PlayerListAdapter.Player[] players = new PlayerListAdapter.Player[count];
        for (int i = 0; i < players.length; i++) {
            players[i] = new PlayerListAdapter.Player();
        }
        return players;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Player player = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_player, null);
        }

        EditText editText = (EditText) convertView.findViewById(R.id.player_name);
        editText.setText(player.nickname);
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                player.nickname = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        Button color = (Button) convertView.findViewById(R.id.player_color);
        color.setBackgroundColor(player.color);
        color.setOnClickListener(v -> {
            player.color = colors[(++player.color_i) % colors.length];
            v.setBackgroundColor(player.color);
        });

        Button rand_nickname = (Button) convertView.findViewById(R.id.rand_nickname);
        rand_nickname.setOnClickListener(v -> editText.setText(player.setRandNick()));

        return convertView;
    }
}