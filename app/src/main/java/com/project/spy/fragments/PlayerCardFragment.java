package com.project.spy.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.spy.R;
import com.project.spy.helpers.LocaleHelper;
import com.project.spy.viewmodel.GameViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerCardFragment extends Fragment {

    private boolean isCardFlipped = false;
    private boolean hasSeenCard = false;
    private ImageView searchIcon;
    private static final String TAG = "PlayerCardFragment";

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
        View view = inflater.inflate(R.layout.player_card, container, false);

        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        TextView playerNumText = view.findViewById(R.id.player_num);
        AppCompatButton cardButton = view.findViewById(R.id.card);
        AppCompatButton nextButton = view.findViewById(R.id.start);
        searchIcon = view.findViewById(R.id.icon);

        String category = gameViewModel.getSelectedCategory().getValue();
        Integer numberOfPlayers = gameViewModel.getNumberOfPlayers().getValue() != null ?
                gameViewModel.getNumberOfPlayers().getValue() : 3;
        Integer numberOfSpies = gameViewModel.getNumberOfSpies().getValue() != null ?
                gameViewModel.getNumberOfSpies().getValue() : 1;
        Integer gameTimeMinutes = gameViewModel.getGameTimeMinutes().getValue() != null ?
                gameViewModel.getGameTimeMinutes().getValue() : 2;

        numberOfSpies = Math.min(numberOfSpies, numberOfPlayers);

        Integer currentPlayerIndex = gameViewModel.getCurrentPlayerIndex().getValue() != null ?
                gameViewModel.getCurrentPlayerIndex().getValue() : 0;
        List<Integer> spyIndices = gameViewModel.getSpyIndices().getValue() != null ?
                gameViewModel.getSpyIndices().getValue() : new ArrayList<>();
        String selectedWord = gameViewModel.getSelectedWord().getValue();

        if (selectedWord == null) {
            String[] wordArray = getWordArray(category);
            selectedWord = wordArray[new Random().nextInt(wordArray.length)];
            gameViewModel.setSelectedWord(selectedWord);
            Log.d(TAG, "Selected word: " + selectedWord);
        }
        if (spyIndices.isEmpty()) {
            spyIndices = assignSpyRoles(numberOfSpies, numberOfPlayers);
            gameViewModel.setSpyIndices(spyIndices);
            Log.d(TAG, "Spy indices set: " + spyIndices);
        }

        updatePlayerNumberDisplay(playerNumText, numberOfPlayers, currentPlayerIndex);

        nextButton.setText(currentPlayerIndex < numberOfPlayers - 1 ? R.string.next_player : R.string.start_timer);

        cardButton.setOnClickListener(v -> {
            if (!isCardFlipped) {
                flipCard(cardButton, gameViewModel.getSelectedWord().getValue(), true);
                isCardFlipped = true;
                hasSeenCard = true;
            } else {
                flipCard(cardButton, "", false);
                isCardFlipped = false;
            }
        });

        nextButton.setOnClickListener(v -> {
            if (hasSeenCard) {
                int nextPlayerIndex = gameViewModel.getCurrentPlayerIndex().getValue() + 1;
                gameViewModel.setCurrentPlayerIndex(nextPlayerIndex);
                if (nextPlayerIndex < numberOfPlayers) {
                    isCardFlipped = false;
                    hasSeenCard = false;
                    cardButton.setText("");
                    searchIcon.setVisibility(View.VISIBLE);
                    updatePlayerNumberDisplay(playerNumText, numberOfPlayers, nextPlayerIndex);
                    nextButton.setText(nextPlayerIndex < numberOfPlayers - 1 ? R.string.next_player : R.string.start_timer);
                } else {
                    Log.d(TAG, "Navigating to MainGameFragment");
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new MainGameFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }

    private void flipCard(AppCompatButton cardButton, String selectedWord, boolean showContent) {
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(cardButton, "rotationX", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(cardButton, "rotationX", -90f, 0f);

        flipOut.setDuration(200);
        flipIn.setDuration(200);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
                List<Integer> spyIndices = gameViewModel.getSpyIndices().getValue();
                Integer currentPlayerIndex = gameViewModel.getCurrentPlayerIndex().getValue();
                String spy = getResources().getString(R.string.spy);
                if (showContent && spyIndices != null && currentPlayerIndex != null) {
                    cardButton.setText(spyIndices.contains(currentPlayerIndex) ? spy : selectedWord);
                    searchIcon.setVisibility(View.GONE);
                } else {
                    cardButton.setText("");
                    searchIcon.setVisibility(View.VISIBLE);
                }
                flipIn.start();
            }
        });

        flipOut.start();
    }

    private void updatePlayerNumberDisplay(TextView playerNumText, Integer numberOfPlayers, Integer currentPlayerIndex) {
        playerNumText.setText(getString(R.string.player_number) + " " + (currentPlayerIndex + 1) + "/" + numberOfPlayers);
    }

    private List<Integer> assignSpyRoles(Integer numberOfSpies, Integer totalPlayers) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        List<Integer> selectedSpies = indices.subList(0, Math.min(numberOfSpies, totalPlayers));
        Log.d(TAG, "Assigned spy indices: " + selectedSpies);
        return selectedSpies;
    }

    private String[] getWordArray(String category) {
        if (category == null) {
            Log.w(TAG, "Category is null, returning default word");
            return new String[]{"Unknown"};
        }
        switch (category) {
            case "locations":
                return getResources().getStringArray(R.array.locations);
            case "jobs":
                return getResources().getStringArray(R.array.jobs);
            case "animals":
                return getResources().getStringArray(R.array.animals);
            case "food":
                return getResources().getStringArray(R.array.foods);
            case "movies":
                return getResources().getStringArray(R.array.movies);
            case "shows":
                return getResources().getStringArray(R.array.shows);
            case "celebrities":
                return getResources().getStringArray(R.array.celebrities);
            case "footballers":
                return getResources().getStringArray(R.array.footballers);
            default:
                Log.w(TAG, "Unknown category: " + category);
                return new String[]{"Unknown"};
        }
    }
}