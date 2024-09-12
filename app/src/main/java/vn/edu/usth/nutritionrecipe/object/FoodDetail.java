package vn.edu.usth.nutritionrecipe.object;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

import vn.edu.usth.nutritionrecipe.databinding.ActivityFoodDetailBinding;
import vn.edu.usth.nutritionrecipe.R;

public class FoodDetail extends AppCompatActivity {

    private ActivityFoodDetailBinding binding;
    private SharedPreferences preferences;
    private Button favButton;
    private boolean isFavorite;
    private String name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Intent intent = this.getIntent();

            preferences = getSharedPreferences("favorites", MODE_PRIVATE);

            if (intent != null){
                String name = intent.getStringExtra("name");
                String time = intent.getStringExtra("time");
                int ingredients = intent.getIntExtra("ingredients",  R.string.maggiIngredients);
                int desc = intent.getIntExtra("desc", R.string.maggieDesc);
                int image = intent.getIntExtra("image", R.drawable.maggi);
                binding.detailName.setText(name);
                binding.detailTime.setText(time);
                binding.detailDesc.setText(desc);
                binding.detailIngredients.setText(ingredients);
                binding.detailImage.setImageResource(image);

                // Check favorite status
                isFavorite = preferences.getBoolean("favorite_" + name, false);
                updateFavoriteButton();
            }

            Button favButton = binding.favButton; // Initialize the button
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isFavorite = !isFavorite; // Toggle favorite status
                    preferences.edit().putBoolean("favorite_" + name, isFavorite).apply();
                    updateFavoriteButton();
                }
            });
        }

    private void updateFavoriteButton() {
            Button favButton = binding.favButton;
        if (isFavorite) {
            favButton.setBackgroundResource(R.drawable.baseline_favorite_24); // Filled icon
        } else {
            favButton.setBackgroundResource(R.drawable.baseline_favorite_border_24); // Outline icon

        }
        }
}