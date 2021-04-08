package ru.pyply.games.points.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pyply.games.points.R;
import ru.pyply.games.points.models.Team;

public class GameInfoFragment extends Fragment {

    public GameInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCurrentTeam(Team team) {
        FragmentManager fragmentManager = getFragmentManager();
        CurrentPlayerFragment fragment = (CurrentPlayerFragment) fragmentManager.findFragmentById(R.id.currentTeam);
        assert fragment != null;
        fragment.setPlayer(team.players.get(0));
        fragment.setTeam(team);
        fragment.setColor(team.paintPoints.getColor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_info, container, false);
    }
}