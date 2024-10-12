package vn.edu.usth.nutritionrecipe.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import vn.edu.usth.nutritionrecipe.Adapter.IngredientsAdapter;
import vn.edu.usth.nutritionrecipe.Adapter.InstructionsAdapter;
import vn.edu.usth.nutritionrecipe.Adapter.SimilarRecipeAdapter;
import vn.edu.usth.nutritionrecipe.Listeners.InstructionsListener;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeClickListener;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeDetailsListener;
import vn.edu.usth.nutritionrecipe.Listeners.SimilarRecipesListener;
import vn.edu.usth.nutritionrecipe.Models.InstructionsResponse;
import vn.edu.usth.nutritionrecipe.Models.RecipeDetailsResponse;
import vn.edu.usth.nutritionrecipe.Models.SimilarRecipeResponse;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.RequestManager;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView detailName, detailSource, detailSummary, detailTime;
    ImageView detailImage, detailNutritionLabel;
    RecyclerView recycler_detailIngredients, recycler_detailSimilarRecipe, recycler_detailInstructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("id")));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getSimilarRecipes(similarRecipesListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.show();
    }

    private void findViews() {
        detailName = findViewById(R.id.detailName);
        detailSource = findViewById(R.id.detailSource);
        detailSummary = findViewById(R.id.detailSummary);
        detailTime = findViewById(R.id.detailTime);
        detailImage = findViewById(R.id.detailImage);
        detailNutritionLabel = findViewById(R.id.detailNutritionLabel);
        recycler_detailIngredients = findViewById(R.id.recycler_detailIngredients);
        recycler_detailSimilarRecipe = findViewById(R.id.recycler_detailSimilarRecipe);
        recycler_detailInstructions = findViewById(R.id.recycler_detailInstructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            detailName.setText(response.title);
            detailSource.setText(response.sourceName);
            detailSummary.setText(Html.fromHtml(response.summary, Html.FROM_HTML_MODE_LEGACY));
            detailTime.setText(response.readyInMinutes + " Minutes");
            Picasso.get().load(response.image).into(detailImage);

            String apiKey = getString(R.string.api_key);
            String imageUrl = "https://api.spoonacular.com/recipes/" + response.id + "/nutritionLabel.png?apiKey=" + apiKey;
            Picasso.get().load(imageUrl).into(detailNutritionLabel);

            recycler_detailIngredients.setHasFixedSize(true);
            recycler_detailIngredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_detailIngredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipeResponse> response, String message) {
            recycler_detailSimilarRecipe.setHasFixedSize(true);
            recycler_detailSimilarRecipe.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailsActivity.this, response, recipeClickListener);
            recycler_detailSimilarRecipe.setAdapter(similarRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivities(new Intent[]{new Intent(RecipeDetailsActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id", id)});
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_detailInstructions.setHasFixedSize(true);
            recycler_detailInstructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));

            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_detailInstructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };



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