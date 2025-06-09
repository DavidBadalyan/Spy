package com.project.spy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GameViewModel extends ViewModel {
    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>();
    private final MutableLiveData<Integer> numberOfPlayers = new MutableLiveData<>();
    private final MutableLiveData<Integer> numberOfSpies = new MutableLiveData<>();
    private final MutableLiveData<Integer> gameTimeMinutes = new MutableLiveData<>();
    private final MutableLiveData<String> selectedWord = new MutableLiveData<>();
    private final MutableLiveData<Long> remainingTimeMillis = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPlayerIndex = new MutableLiveData<>(0);
    private final MutableLiveData<List<Integer>> spyIndices = new MutableLiveData<>(new ArrayList<>());
    private final List<Integer> eliminatedPlayers = new ArrayList<>();

    public LiveData<String> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String category) {
        selectedCategory.setValue(category);
    }

    public LiveData<Integer> getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int players) {
        numberOfPlayers.setValue(players);
    }

    public LiveData<Integer> getNumberOfSpies() {
        return numberOfSpies;
    }

    public void setNumberOfSpies(int spies) {
        numberOfSpies.setValue(spies);
    }

    public LiveData<Integer> getGameTimeMinutes() {
        return gameTimeMinutes;
    }

    public void setGameTimeMinutes(int minutes) {
        gameTimeMinutes.setValue(minutes);
    }

    public LiveData<String> getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(String word) {
        selectedWord.setValue(word);
    }

    public LiveData<Long> getRemainingTimeMillis() {
        return remainingTimeMillis;
    }

    public void setRemainingTimeMillis(long millis) {
        remainingTimeMillis.setValue(millis);
    }

    public LiveData<Integer> getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int index) {
        currentPlayerIndex.setValue(index);
    }

    public LiveData<List<Integer>> getSpyIndices() {
        return spyIndices;
    }

    public void setSpyIndices(List<Integer> indices) {
        spyIndices.setValue(new ArrayList<>(indices));
    }

    public void eliminatePlayer(int playerIndex) {
        if (!eliminatedPlayers.contains(playerIndex)) {
            eliminatedPlayers.add(playerIndex);
        }
    }

    public void restorePlayer(int playerIndex) {
        eliminatedPlayers.remove(Integer.valueOf(playerIndex));
    }

    public boolean isPlayerEliminated(int playerIndex) {
        return eliminatedPlayers.contains(playerIndex);
    }

    public boolean areAllSpiesEliminated() {
        List<Integer> spies = spyIndices.getValue();
        if (spies == null || spies.isEmpty()) return false;
        for (int spyIndex : spies) {
            if (!eliminatedPlayers.contains(spyIndex)) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getEliminatedPlayers() {
        return new ArrayList<>(eliminatedPlayers);
    }

    public void resetGameState() {
        eliminatedPlayers.clear();
        currentPlayerIndex.setValue(0);
        spyIndices.setValue(new ArrayList<>());
        selectedWord.setValue(null);
        remainingTimeMillis.setValue(0L);
    }
}