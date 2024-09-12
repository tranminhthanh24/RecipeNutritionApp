package vn.edu.usth.nutritionrecipe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.usth.nutritionrecipe.databinding.FragmentFavoriteBinding;
import vn.edu.usth.nutritionrecipe.viewmodel.FavoriteViewModel;

import android.content.SharedPreferences;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.edu.usth.nutritionrecipe.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private LinearLayout favoritesContainer;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoritesContainer = view.findViewById(R.id.favoritesContainer);
        preferences = getActivity().getSharedPreferences("favorites", getContext().MODE_PRIVATE);
        loadFavorites();
        return view;
    }

    private void loadFavorites() {
        List<String> favorites = new ArrayList<>();
        for (String key : preferences.getAll().keySet()) {
            if (key.startsWith("favorite_") && preferences.getBoolean(key, false)) {
                favorites.add(key.replace("favorite_", "")); // Extract recipe name
            }
        }

        // Display favorites
        for (String favorite : favorites) {
            TextView textView = new TextView(getContext());
            textView.setText(favorite);
            favoritesContainer.addView(textView);
        }
    }
}

