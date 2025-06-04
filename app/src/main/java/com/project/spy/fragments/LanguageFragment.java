package com.project.spy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.project.spy.helpers.LocaleHelper;
import com.project.spy.R;

public class LanguageFragment extends Fragment {

    TextView armenian, russian, english;
    ImageView check_arm, check_rus, check_eng;
    AppCompatButton next;

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
        View view = inflater.inflate(R.layout.languages_fragment, container, false);

        // Initialize UI elements
        armenian = view.findViewById(R.id.language_armenian);
        russian = view.findViewById(R.id.language_russian);
        english = view.findViewById(R.id.language_english);
        check_arm = view.findViewById(R.id.check_armenian);
        check_rus = view.findViewById(R.id.check_russian);
        check_eng = view.findViewById(R.id.check_english);
        next = view.findViewById(R.id.next);

        // Set checkmark based on current language
        String currentLanguage = LocaleHelper.getLanguage(requireContext());
        updateCheckmarks(currentLanguage);

        // Set click listeners for language selection
        english.setOnClickListener(v -> selectLanguage("en"));
        armenian.setOnClickListener(v -> selectLanguage("hy"));
        russian.setOnClickListener(v -> selectLanguage("ru"));

        next.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CategoriesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void selectLanguage(String languageCode) {
        if (!languageCode.equals(LocaleHelper.getLanguage(requireContext()))) {
            LocaleHelper.setLocale(requireContext(), languageCode);
            updateCheckmarks(languageCode);
            requireActivity().recreate();
        }
    }

    private void updateCheckmarks(String languageCode) {
        check_eng.setVisibility(languageCode.equals("en") ? View.VISIBLE : View.GONE);
        check_arm.setVisibility(languageCode.equals("hy") ? View.VISIBLE : View.GONE);
        check_rus.setVisibility(languageCode.equals("ru") ? View.VISIBLE : View.GONE);
    }
}