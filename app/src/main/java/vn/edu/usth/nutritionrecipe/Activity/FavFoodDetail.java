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
        binding = ActivityFavFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_fav_food_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        favFoodButton = findViewById(R.id.favFoodButton);

        Intent intent = this.getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String time = intent.getStringExtra("time");
            int ingredients = intent.getIntExtra("ingredients", R.string.maggiIngredients);
            int desc = intent.getIntExtra("desc", R.string.maggiDesc);
            int image = intent.getIntExtra("image", R.drawable.maggi);
            binding.detailName.setText(name);
            binding.detailTime.setText(time);
            binding.detailDesc.setText(desc);
            binding.detailIngredients.setText(ingredients);
            binding.detailImage.setImageResource(image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}