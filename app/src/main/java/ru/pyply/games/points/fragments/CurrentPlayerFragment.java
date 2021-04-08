package ru.pyply.games.points.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.pyply.games.points.R;
import ru.pyply.games.points.models.Player;
import ru.pyply.games.points.models.Team;

public class CurrentPlayerFragment extends Fragment {

    public CurrentPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setPlayer(Player player) {
        //noinspection ConstantConditions
        TextView player_name = getView().findViewById(R.id.player_name_game_info);
        if (player_name != null)
            player_name.setText(player.name);
    }

    public void setTeam(Team team) {
        //noinspection ConstantConditions
        TextView team_name = getView().findViewById(R.id.team_name_game_info);
        if (team_name != null)
            team_name.setText(team.getName());
    }

    public void setColor(int color) {
        //noinspection ConstantConditions
        TextView player_name = getView().findViewById(R.id.player_name_game_info);
        if (player_name != null)
            player_name.setTextColor(color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_player, container, false);
    }
}