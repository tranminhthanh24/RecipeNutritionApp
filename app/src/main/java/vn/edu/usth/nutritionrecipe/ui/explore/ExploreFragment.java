package vn.edu.usth.nutritionrecipe.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.Adapter.FoodAdapter;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;
    private final ArrayList<FoodList> dataArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.listView;

        int[] imageList = {
                R.drawable.maggi,
                R.drawable.makizushi,
                R.drawable.penang_asam_laksa,
                R.drawable.sauerbraten,
                R.drawable.red_velvet_cake,
                R.drawable.crepe,
                R.drawable.turkey_burger,
                R.drawable.crinklecut_fries,
                R.drawable.arepas,
                R.drawable.butter_garlic_crab,
                R.drawable.chicken_parm,
                R.drawable.chicken_rice,
                R.drawable.chili_crab,
                R.drawable.croissant,
                R.drawable.fajitas,
                R.drawable.fish_n_chips,
                R.drawable.goi_cuon,
                R.drawable.hummus,
                R.drawable.lasagna,
                R.drawable.masala_dosa,
                R.drawable.pasta,
                R.drawable.pastel_de_nata,
                R.drawable.pierogi,
                R.drawable.piri_piri_chicken,
                R.drawable.pizza,
                R.drawable.poke,
                R.drawable.potato_chips,
                R.drawable.rendang,
                R.drawable.seafood_paella,
                R.drawable.som_tam
        };

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

        String[] nameList = {
                "Maggi",
                "Makizushi",
                "Penang Asam Laksa",
                "Sauerbraten",
                "Red Velvet Cake",
                "Crêpe",
                "Turkey Hamburger",
                "Crinkle-cut Fries",
                "Arepas",
                "Butter Garlic Crab",
                "Chicken Parm",
                "Chicken Rice",
                "Chili Crab",
                "Croissant",
                "Fajitas",
                "Fish and Chips",
                "Gỏi Cuốn",
                "Hummus",
                "Lasagna",
                "Masala Dosa",
                "Pasta",
                "Pastel de nata",
                "Pierogi",
                "Piri Piri Chicken",
                "Pizza",
                "Poke",
                "Potato Chips",
                "Rendang",
                "Seafood Paella",
                "Som Tam"
        };

        String[] timeList = {
                "2 mins",
                "45 mins",
                "1 hour",
                "3-4 hours",
                "45 mins",
                "10 mins",
                "45 mins",
                "30 mins",
                "30 mins",
                "2 hours",
                "2 hours",
                "1 hour",
                "1 hour",
                "30 mins",
                "10 mins",
                "2 hours",
                "1 hour",
                "40 mins",
                "45 mins",
                "1 hour",
                "30 mins",
                "45 mins",
                "90 mins",
                "1 hour",
                "90 mins",
                "15 mins",
                "40 mins",
                "3-4 hours",
                "50 mins",
                "20 mins"
        };

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
