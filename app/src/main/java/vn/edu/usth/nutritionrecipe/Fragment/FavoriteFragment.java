package vn.edu.usth.nutritionrecipe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.Activity.FavFoodDetail;
import vn.edu.usth.nutritionrecipe.Item.FoodList;
import vn.edu.usth.nutritionrecipe.Adapter.FoodAdapter;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.databinding.FragmentFavoriteBinding;

public class FavoriteFragment extends Fragment {

    private FragmentFavoriteBinding binding;
    private final ArrayList<FoodList> dataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false); //Inflate the layout
        View root = binding.getRoot();

        ListView listView = binding.listView; //Get the listView

        //Retrieve image from resource
        TypedArray images = getResources().obtainTypedArray(R.array.image_list);
        //Create an array to hold image resource IDs
        int[] imageList = new int[images.length()];
        // Retrieve drawable resource ID
        for (int i = 0; i < images.length(); i++) {
            imageList[i] = images.getResourceId(i, -1);
        }
        images.recycle();  //Free up memory

        //Retrieve ingredient resource ID
        int[] ingredientList = {
                R.string.maggiIngredients,
                R.string.makizushiIngredients,
                R.string.penangAsamLaksaIngredients,
                R.string.sauerbratenIngredients,
                R.string.redVelvetCakeIngredients,
                R.string.crepeIngredients,
                R.string.turkeyBurgerIngredients,
                R.string.crinkleCutFriesIngredients,
                R.string.arepasIngredients,
                R.string.butterGarlicCrabIngredients,
                R.string.chickenParmIngredients,
                R.string.chickenRiceIngredients,
                R.string.chiliCrabIngredients,
                R.string.croissantIngredients,
                R.string.fajitasIngredients,
                R.string.fishAndChipsIngredients,
                R.string.goiCuonIngredients,
                R.string.hummusIngredients,
                R.string.lasagnaIngredients,
                R.string.masalaDosaIngredients,
                R.string.pastaIngredients,
                R.string.pastelDeNataIngredients,
                R.string.pierogiIngredients,
                R.string.piriPiriChickenIngredients,
                R.string.pizzaIngredients,
                R.string.pokeIngredients,
                R.string.potatoChipsIngredients,
                R.string.rendangIngredients,
                R.string.seafoodPaellaIngredients,
                R.string.somTamIngredients
        };

        //Retrieve description resource ID
        int[] descList = {
                R.string.maggiDesc,
                R.string.makizushiDesc,
                R.string.penangAsamLaksaDesc,
                R.string.sauerbratenDesc,
                R.string.redVelvetCakeDesc,
                R.string.crepeDesc,
                R.string.turkeyBurgerDesc,
                R.string.crinkleCutFriesDesc,
                R.string.arepasDesc,
                R.string.butterGarlicCrabDesc,
                R.string.chickenParmDesc,
                R.string.chickenRiceDesc,
                R.string.chiliCrabDesc,
                R.string.croissantDesc,
                R.string.fajitasDesc,
                R.string.fishAndChipsDesc,
                R.string.goiCuonDesc,
                R.string.hummusDesc,
                R.string.lasagnaDesc,
                R.string.masalaDosaDesc,
                R.string.pastaDesc,
                R.string.pastelDeNataDesc,
                R.string.pierogiDesc,
                R.string.piriPiriChickenDesc,
                R.string.pizzaDesc,
                R.string.pokeDesc,
                R.string.potatoChipsDesc,
                R.string.rendangDesc,
                R.string.seafoodPaellaDesc,
                R.string.somTamDesc
        };

        //Retrieve food name resource ID
        String[] nameList = getResources().getStringArray(R.array.food_names);

        //Retrieve cooking time resource ID
        String[] timeList = getResources().getStringArray(R.array.cooking_times);

        for (int i = 0; i < imageList.length; i++) {
            FoodList foodList = new FoodList(nameList[i], timeList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(foodList); //Add each food item to the array list
        }

        FoodAdapter foodAdapter = new FoodAdapter(getActivity(), dataArrayList);
        listView.setAdapter(foodAdapter);

        //Set click on an item
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getActivity(), FavFoodDetail.class); // Create an Intent to start the FoodDetail activity
            //Pass data to the FoodDetail activity
            intent.putExtra("name", nameList[i]);
            intent.putExtra("time", timeList[i]);
            intent.putExtra("ingredients", ingredientList[i]);
            intent.putExtra("desc", descList[i]);
            intent.putExtra("image", imageList[i]);
            startActivity(intent); //Start the FoodDetail activity
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}