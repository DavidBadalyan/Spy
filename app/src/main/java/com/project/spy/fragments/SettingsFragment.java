package com.project.spy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.spy.helpers.LocaleHelper;
import com.project.spy.R;
import com.project.spy.viewmodel.GameViewModel;

public class SettingsFragment extends Fragment {

    private GameViewModel gameViewModel;
    private int numberOfPlayers = 3;
    private int numberOfSpies = 1;
    private int gameTimeMinutes = 2;
    private static final int MIN_PLAYERS = 3;
    private static final int MIN_SPIES = 0;
    private static final int MIN_TIME = 1;
    private static final int MAX_TIME = 15;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocaleHelper.setLocale(context, LocaleHelper.getLanguage(context));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        TextView playersCount = view.findViewById(R.id.players_count);
        TextView spiesCount = view.findViewById(R.id.spies_count);
        TextView timeCount = view.findViewById(R.id.time_count);
        ImageButton minusPlayers = view.findViewById(R.id.minus_players);
        ImageButton plusPlayers = view.findViewById(R.id.plus_players);
        ImageButton minusSpies = view.findViewById(R.id.minus_spies);
        ImageButton plusSpies = view.findViewById(R.id.plus_spies);
        ImageButton minusTime = view.findViewById(R.id.minus_time);
        ImageButton plusTime = view.findViewById(R.id.plus_time);
        AppCompatButton startButton = view.findViewById(R.id.start);

        playersCount.setText(String.valueOf(numberOfPlayers));
        spiesCount.setText(String.valueOf(numberOfSpies));
        timeCount.setText(String.valueOf(gameTimeMinutes));

        minusPlayers.setOnClickListener(v -> {
            if(numberOfPlayers > MIN_PLAYERS) {
                numberOfPlayers--;
                playersCount.setText(String.valueOf(numberOfPlayers));
            }
        });

        plusPlayers.setOnClickListener(v -> {
            numberOfPlayers++;
            playersCount.setText(String.valueOf(numberOfPlayers));
        });

        minusSpies.setOnClickListener(v -> {
            if(numberOfSpies > MIN_SPIES) {
                numberOfSpies--;
                spiesCount.setText(String.valueOf(numberOfSpies));
            }
        });

        plusSpies.setOnClickListener(v -> {
            if(numberOfSpies < numberOfPlayers) {
                numberOfSpies++;
                spiesCount.setText(String.valueOf(numberOfSpies));
            }
        });

        minusTime.setOnClickListener(v -> {
            if (gameTimeMinutes > MIN_TIME) {
                gameTimeMinutes--;
                timeCount.setText(String.valueOf(gameTimeMinutes));
            }
        });

        plusTime.setOnClickListener(v -> {
            if (gameTimeMinutes < MAX_TIME) {
                gameTimeMinutes++;
                timeCount.setText(String.valueOf(gameTimeMinutes));
            }
        });

        startButton.setOnClickListener(v -> {
            gameViewModel.setNumberOfPlayers(numberOfPlayers);
            gameViewModel.setNumberOfSpies(numberOfSpies);
            gameViewModel.setGameTimeMinutes(gameTimeMinutes);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new PlayerCardFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}