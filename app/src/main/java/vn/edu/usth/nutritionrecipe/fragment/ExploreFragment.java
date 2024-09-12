package vn.edu.usth.nutritionrecipe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.adapter.FoodAdapter;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.databinding.FragmentExploreBinding;
import vn.edu.usth.nutritionrecipe.object.FoodDetail;
import vn.edu.usth.nutritionrecipe.object.FoodList;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private final ArrayList<FoodList> dataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.listView;

        int[] imageList = {R.drawable.pasta, R.drawable.maggi, R.drawable.cake, R.drawable.pancake, R.drawable.pizza, R.drawable.burger, R.drawable.fries};
        int[] ingredientList = {R.string.pastaIngredients, R.string.maggiIngredients, R.string.cakeIngredients, R.string.pancakeIngredients, R.string.pizzaIngredients, R.string.burgerIngredients, R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc, R.string.maggieDesc, R.string.cakeDesc, R.string.pancakeDesc, R.string.pizzaDesc, R.string.burgerDesc, R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi", "Cake", "Pancake", "Pizza", "Hamburger", "Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins", "10 mins", "60 mins", "45 mins", "30 mins"};

        for (int i = 0; i < imageList.length; i++) {
            FoodList foodList = new FoodList(nameList[i], timeList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(foodList);
        }

        FoodAdapter foodAdapter = new FoodAdapter(getActivity(), dataArrayList);
        listView.setAdapter(foodAdapter);

        // Move the onItemClick code here
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getActivity(), FoodDetail.class);
            intent.putExtra("name", nameList[i]);
            intent.putExtra("time", timeList[i]);
            intent.putExtra("ingredients", ingredientList[i]);
            intent.putExtra("desc", descList[i]);
            intent.putExtra("image", imageList[i]);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
