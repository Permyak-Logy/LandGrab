package ru.pyply.games.points.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import ru.pyply.games.points.R;


public class TimerFragment extends Fragment {
    int color_from_10_sec = Color.GRAY;
    int color_up_to_10_sec = Color.RED;

    {

    }

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("DefaultLocale")
    public void setValueTimer(int valueTimer) {
        TextView timer = Objects.requireNonNull(getView()).findViewById(R.id.timer_view);
        int minutes = valueTimer / 60, seconds = valueTimer % 60;

        if (valueTimer > 10)
            timer.setTextColor(color_from_10_sec);
        else
            timer.setTextColor(color_up_to_10_sec);

        timer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }
}