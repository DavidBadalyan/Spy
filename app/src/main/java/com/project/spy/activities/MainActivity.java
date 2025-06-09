package com.project.spy.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.project.spy.R;
import com.project.spy.fragments.CategoriesFragment;
import com.project.spy.fragments.LanguageFragment;
import com.project.spy.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SpyGamePrefs";
    private static final String KEY_THEME = "theme";

    ImageButton back;
    SwitchCompat themeToggle;
    FragmentContainerView fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = findViewById(R.id.back_button);
        themeToggle = findViewById(R.id.theme_toggle);
        fragmentContainer = findViewById(R.id.fragment_container);

        boolean isDarkMode = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .getBoolean(KEY_THEME, false);
        themeToggle.setChecked(isDarkMode);

        themeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_THEME, isChecked);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        back.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment instanceof SettingsFragment) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CategoriesFragment())
                        .commit();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                        super.onFragmentResumed(fm, f);
                        back.setVisibility(f instanceof LanguageFragment ? View.GONE : View.VISIBLE);
                    }
                }, true
        );

        if (savedInstanceState == null) {
            back.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LanguageFragment())
                    .commit();
        }
    }

    private void applySavedTheme() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_THEME, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}