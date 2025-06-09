package com.project.spy.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.spy.R;
import com.project.spy.adapters.PlayerAdapter;
import com.project.spy.helpers.LocaleHelper;
import com.project.spy.viewmodel.GameViewModel;

public class MainGameFragment extends Fragment implements PlayerAdapter.OnAllSpiesEliminatedListener {

    private GameViewModel gameViewModel;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    private static final String TAG = "MainGameFragment";

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
        View view = inflater.inflate(R.layout.main_game_fragment, container, false);

        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        timerText = view.findViewById(R.id.timer_text);
        RecyclerView playerRecyclerView = view.findViewById(R.id.player_recycler_view);

        int numberOfPlayers = gameViewModel.getNumberOfPlayers().getValue() != null ?
                gameViewModel.getNumberOfPlayers().getValue() : 3;
        PlayerAdapter adapter = new PlayerAdapter(numberOfPlayers, gameViewModel, this);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        playerRecyclerView.setAdapter(adapter);

        Long remainingTime = gameViewModel.getRemainingTimeMillis().getValue();
        long millis = remainingTime != null && remainingTime > 0 ?
                remainingTime : (gameViewModel.getGameTimeMinutes().getValue() != null ?
                gameViewModel.getGameTimeMinutes().getValue() : 2) * 60 * 1000;
        Log.d(TAG, "Starting timer with millis: " + millis);
        startTimer(millis);

        return view;
    }

    private void startTimer(long millis) {
        countDownTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameViewModel.setRemainingTimeMillis(millisUntilFinished);
                long minutesLeft = millisUntilFinished / 1000 / 60;
                long secondsLeft = (millisUntilFinished / 1000) % 60;
                timerText.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
            }

            @Override
            public void onFinish() {
                gameViewModel.setRemainingTimeMillis(0L);
                timerText.setText("00:00");
                Log.d(TAG, "Timer finished, areAllSpiesEliminated: " + gameViewModel.areAllSpiesEliminated());
                if (gameViewModel.areAllSpiesEliminated()) {
                    showSpiesLostDialog();
                } else {
                    showSpyGuessDialog();
                }
            }
        }.start();
    }

    @Override
    public void onAllSpiesEliminated() {
        Log.d(TAG, "onAllSpiesEliminated called");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        showSpiesLostDialog();
    }

    private void showSpiesLostDialog() {
        Log.d(TAG, "Showing spies lost dialog");
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_spies_lost, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.ok_button).setOnClickListener(v -> {
            dialog.dismiss();
            gameViewModel.resetGameState();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        dialog.show();
    }

    private void showSpyGuessDialog() {
        Log.d(TAG, "Showing spy guess dialog");
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_spy_guess, null);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.ok_button).setOnClickListener(v -> {
            dialog.dismiss();
            gameViewModel.resetGameState(); // Reset game state before navigating
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}