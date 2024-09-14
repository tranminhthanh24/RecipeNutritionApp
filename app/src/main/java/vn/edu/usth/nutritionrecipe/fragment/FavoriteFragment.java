package vn.edu.usth.nutritionrecipe.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.adapter.FoodAdapter;
import vn.edu.usth.nutritionrecipe.object.FoodList;
import vn.edu.usth.nutritionrecipe.object.FoodDetail;

public class FavoriteFragment extends Fragment {

    private ListView favoritesListView;
    private SharedPreferences preferences;
    private ArrayList<FoodList> favoriteFoodList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoritesListView = view.findViewById(R.id.favoritesListView);
        preferences = getActivity().getSharedPreferences("favorites", getContext().MODE_PRIVATE);
        favoriteFoodList = new ArrayList<>();

        loadFavorites();
        setupItemClickListener();
        return view;
    }

    private void loadFavorites() {
        int[] imageList = {R.drawable.pasta, R.drawable.maggi, R.drawable.cake, R.drawable.pancake, R.drawable.pizza, R.drawable.burger, R.drawable.fries};
        int[] ingredientList = {R.string.pastaIngredients, R.string.maggiIngredients, R.string.cakeIngredients, R.string.pancakeIngredients, R.string.pizzaIngredients, R.string.burgerIngredients, R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc, R.string.maggieDesc, R.string.cakeDesc, R.string.pancakeDesc, R.string.pizzaDesc, R.string.burgerDesc, R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi", "Cake", "Pancake", "Pizza", "Hamburger", "Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins", "10 mins", "60 mins", "45 mins", "30 mins"};

        for (int i = 0; i < nameList.length; i++) {
            if (preferences.getBoolean("favorite_" + nameList[i], false)) {
                favoriteFoodList.add(new FoodList(nameList[i], "Time", ingredientList[i], descList[i], imageList[i]));
            }
        }

        if (!favoriteFoodList.isEmpty()) {
            FoodAdapter foodAdapter = new FoodAdapter(getActivity(), favoriteFoodList);
            favoritesListView.setAdapter(foodAdapter);
        }
    }

    private void setupItemClickListener() {
        favoritesListView.setOnItemClickListener((parent, view, position, id) -> {
            FoodList selectedFood = favoriteFoodList.get(position);
            Intent intent = new Intent(getActivity(), FoodDetail.class);
            intent.putExtra("name", selectedFood.name);
            intent.putExtra("time", selectedFood.time);
            intent.putExtra("ingredients", selectedFood.getIngredients());
            intent.putExtra("desc", selectedFood.getDescription());
            intent.putExtra("image", selectedFood.image);
            startActivity(intent);
        });
    }
}