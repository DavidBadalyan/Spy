package com.project.spy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.spy.helpers.LocaleHelper;
import com.project.spy.R;
import com.project.spy.viewmodel.GameViewModel;

public class CategoriesFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.categories_fragment, container, false);

        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        AppCompatButton locations, jobs, animals, food, movies, shows, celebrities, footballers;

        locations = view.findViewById(R.id.locations);
        jobs = view.findViewById(R.id.jobs);
        animals = view.findViewById(R.id.animals);
        food = view.findViewById(R.id.food);
        movies = view.findViewById(R.id.movies);
        shows = view.findViewById(R.id.shows);
        celebrities = view.findViewById(R.id.celebrities);
        footballers = view.findViewById(R.id.footballers);

        View.OnClickListener categoryClickListener = v -> {
            String category = "";
            if (v.getId() == R.id.locations) {
                category = "locations";
            } else if (v.getId() == R.id.jobs) {
                category = "jobs";
            } else if (v.getId() == R.id.animals) {
                category = "animals";
            } else if (v.getId() == R.id.food) {
                category = "food";
            } else if (v.getId() == R.id.movies) {
                category = "movies";
            } else if (v.getId() == R.id.shows) {
                category = "shows";
            } else if (v.getId() == R.id.celebrities) {
                category = "celebrities";
            } else if (v.getId() == R.id.footballers) {
                category = "footballers";
            }

            gameViewModel.setSelectedCategory(category);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        };

        locations.setOnClickListener(categoryClickListener);
        jobs.setOnClickListener(categoryClickListener);
        animals.setOnClickListener(categoryClickListener);
        food.setOnClickListener(categoryClickListener);
        movies.setOnClickListener(categoryClickListener);
        shows.setOnClickListener(categoryClickListener);
        celebrities.setOnClickListener(categoryClickListener);
        footballers.setOnClickListener(categoryClickListener);

        return view;
    }
}