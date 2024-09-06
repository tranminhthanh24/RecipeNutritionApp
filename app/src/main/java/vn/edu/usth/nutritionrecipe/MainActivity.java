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

    int[] imageList = {R.drawable.pasta};
    int[] ingredientList = {R.string.pastaIngredients,R.string.pizzaIngredients};
    int[] descList = {R.string.pastaDescription,R.string.pizzaDescription};
    String[] nameList = {"Pasta", "Pepperoni Pizza", "Hamburger"};
    String[] timeList = {"30 mins", "30 mins", "15 mins"};

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