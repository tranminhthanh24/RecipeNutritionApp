package vn.edu.usth.nutritionrecipe.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import vn.edu.usth.nutritionrecipe.databinding.ActivityFavFoodDetailBinding;
import vn.edu.usth.nutritionrecipe.R;

public class FavFoodDetail extends AppCompatActivity {

    ActivityFavFoodDetailBinding binding;
    private Button favFoodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavFoodDetailBinding.inflate(getLayoutInflater()); //Inflate the layout
        setContentView(R.layout.activity_fav_food_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable the back button in the action bar
        favFoodButton = findViewById(R.id.favFoodButton); //Get the favFoodButton from the resource ID

        //Create an Intent to start the FavFoodDetail activity
        Intent intent = this.getIntent();
        if (intent != null) {
            //Retrieve food name, cooking time from the intent; Retrieve ingredients, description & image from resource ID
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            int ingredients = intent.getIntExtra("ingredients", R.string.maggiIngredients);
            int desc = intent.getIntExtra("desc", R.string.maggiDesc);
            int image = intent.getIntExtra("image", R.drawable.maggi);
            //Set the retrieved data to corresponding views
            binding.detailName.setText(name);
            binding.detailTime.setText(time);
            binding.detailDesc.setText(desc);
            binding.detailIngredients.setText(ingredients);
            binding.detailImage.setImageResource(image);
        }
    }

    //Return to FavoriteFragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Check if the home button was pressed
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); //Go back to the FavoriteFragment
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}