package vn.edu.usth.nutritionrecipe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import vn.edu.usth.nutritionrecipe.databinding.ActivityMainBinding;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.ui.explore.FoodDetail;
import vn.edu.usth.nutritionrecipe.ui.explore.FoodList;
import vn.edu.usth.nutritionrecipe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FoodAdapter foodAdapter;
    ArrayList<FoodList> dataArrayList = new ArrayList<>();
    FoodList foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_favorite)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //FoodDetail

        int[] imageList = {R.drawable.pasta,R.drawable.maggi_300x169,R.drawable.cake_300x169,R.drawable.pancake_300x169,R.drawable.pizza_300x169,R.drawable.burger_300x169,R.drawable.fries_300x169};
        int[] ingredientList = {R.string.pastaIngredients,R.string.maggiIngredients,R.string.cakeIngredients,R.string.pancakeIngredients,R.string.pizzaIngredients,R.string.burgerIngredients,R.string.friesIngredients};
        int[] descList = {R.string.pastaDesc,R.string.maggieDesc,R.string.cakeDesc,R.string.pancakeDesc,R.string.pizzaDesc,R.string.burgerDesc,R.string.friesDesc};
        String[] nameList = {"Pasta", "Maggi","Cake","Pancake","Pizza", "Hamburger","Fries"};
        String[] timeList = {"30 mins", "2 mins", "45 mins","10 mins", "60 mins", "45 mins", "30 mins"};

        for (int i = 0; i < imageList.length; i++){
            foodList = new FoodList(nameList[i], timeList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(foodList);
        }
        foodAdapter = new FoodAdapter(MainActivity.this, dataArrayList);
        binding.listView.setAdapter(foodAdapter);
        binding.listView.setClickable(true);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, FoodDetail.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("time", timeList[i]);
                intent.putExtra("ingredients", ingredientList[i]);
                intent.putExtra("desc", descList[i]);
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });
    }
}