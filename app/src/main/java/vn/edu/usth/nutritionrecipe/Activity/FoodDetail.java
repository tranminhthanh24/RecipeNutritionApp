package vn.edu.usth.nutritionrecipe.Activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import vn.edu.usth.nutritionrecipe.databinding.ActivityFoodDetailBinding;
import vn.edu.usth.nutritionrecipe.R;

public class FoodDetail extends AppCompatActivity {

    ActivityFoodDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodDetailBinding.inflate(getLayoutInflater()); //Inflate the layout
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Enable the back button in the action bar

        //Create an Intent to start the FoodDetail activity
        Intent intent = this.getIntent();
        if (intent != null){
            //Retrieve food name, cooking time from the intent; Retrieve ingredients, description & image from resource ID
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            int ingredients = intent.getIntExtra("ingredients",  R.string.maggiIngredients);
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

    //Return to ExploreFragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Check if the home button was pressed
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); //Go back to the ExploreFragment
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Toggle favButton
    public void onDefaultToggleClick(View view) {
    }
}