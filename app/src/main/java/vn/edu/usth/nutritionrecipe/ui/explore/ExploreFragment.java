package vn.edu.usth.nutritionrecipe.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import vn.edu.usth.nutritionrecipe.Adapter.FoodAdapter;
import vn.edu.usth.nutritionrecipe.databinding.FragmentExploreBinding;
import vn.edu.usth.nutritionrecipe.R;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private FoodAdapter foodAdapter;
    private ArrayList<FoodList> dataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the ListView
        ListView listView = binding.listView;

        // Sample data
        int[] imageList = {R.drawable.pasta, R.drawable.maggi_300x169, R.drawable.cake_300x169, R.drawable.pancake_300x169, R.drawable.pizza_300x169, R.drawable.burger_300x169, R.drawable.fries_300x169};
        int[] ingredientList = {R.string.pastaIngredients, R.string.maggiIngredients, R.string.cakeIngredients, R.string.pancakeIngredients, R.string.pizzaIngredients, R.string.burgerIngredients, R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc, R.string.maggieDesc, R.string.cakeDesc, R.string.pancakeDesc, R.string.pizzaDesc, R.string.burgerDesc, R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi", "Cake", "Pancake", "Pizza", "Hamburger", "Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins", "10 mins", "60 mins", "45 mins", "30 mins"};

        // Populate the dataArrayList
        for (int i = 0; i < imageList.length; i++) {
            FoodList foodList = new FoodList(nameList[i], timeList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(foodList);
        }

        // Set up the adapter
        foodAdapter = new FoodAdapter(getActivity(), dataArrayList);
        listView.setAdapter(foodAdapter);

        // Optionally handle item clicks
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            // Handle item click here
        });

        // Update the TextView if needed
        ExploreViewModel exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        final TextView textView = binding.textExplore;
        exploreViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}