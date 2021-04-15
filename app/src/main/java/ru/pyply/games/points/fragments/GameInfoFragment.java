package ru.pyply.games.points.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

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
        assert fragmentManager != null;
        CurrentPlayerFragment currentPlayerFragment = (CurrentPlayerFragment) fragmentManager.findFragmentById(R.id.currentPlayer);

        assert currentPlayerFragment != null;
        currentPlayerFragment.setPlayer(team.players.get(0));
        currentPlayerFragment.setTeam(team);
        currentPlayerFragment.setColor(team.paintPoints.getColor());
    }

    public void setValueTimer(int value) {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        TimerFragment timerFragment = (TimerFragment) fragmentManager.findFragmentById(R.id.timer);
        assert timerFragment != null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_info, container, false);
    }
}