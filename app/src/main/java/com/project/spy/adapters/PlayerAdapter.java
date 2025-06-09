package com.project.spy.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.spy.R;
import com.project.spy.viewmodel.GameViewModel;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final int numberOfPlayers;
    private final GameViewModel gameViewModel;
    private final OnAllSpiesEliminatedListener listener;
    private static final String TAG = "PlayerAdapter";

    public interface OnAllSpiesEliminatedListener {
        void onAllSpiesEliminated();
    }

    public PlayerAdapter(int numberOfPlayers, GameViewModel gameViewModel, OnAllSpiesEliminatedListener listener) {
        this.numberOfPlayers = numberOfPlayers;
        this.gameViewModel = gameViewModel;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberOfPlayers;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerText;
        private final ImageButton toggleButton;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerText = itemView.findViewById(R.id.player_text);
            toggleButton = itemView.findViewById(R.id.toggle_button);
        }

        void bind(int position) {
            playerText.setText(itemView.getContext().getString(R.string.player_number) + " " + (position + 1));
            boolean isEliminated = gameViewModel.isPlayerEliminated(position);
            playerText.setAlpha(isEliminated ? 0.5f : 1.0f);
            toggleButton.setImageResource(isEliminated ? R.drawable.ic_plus : R.drawable.ic_minus);

            toggleButton.setOnClickListener(v -> {
                Log.d(TAG, "Toggling player " + position + ", isEliminated: " + isEliminated);
                if (isEliminated) {
                    gameViewModel.restorePlayer(position);
                    playerText.setAlpha(1.0f);
                    toggleButton.setImageResource(R.drawable.ic_minus);
                    Log.d(TAG, "Restored player " + position + ", eliminatedPlayers: " + gameViewModel.getEliminatedPlayers());
                } else {
                    gameViewModel.eliminatePlayer(position);
                    playerText.setAlpha(0.5f);
                    toggleButton.setImageResource(R.drawable.ic_plus);
                    Log.d(TAG, "Eliminated player " + position + ", spyIndices: " + gameViewModel.getSpyIndices() + ", eliminatedPlayers: " + gameViewModel.getEliminatedPlayers());
                    if (gameViewModel.areAllSpiesEliminated()) {
                        Log.d(TAG, "All spies eliminated, triggering callback");
                        listener.onAllSpiesEliminated();
                    }
                }
                notifyItemChanged(position);
            });
        }
    }
}