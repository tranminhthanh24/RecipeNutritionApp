package vn.edu.usth.nutritionrecipe.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
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
import vn.edu.usth.nutritionrecipe.UserSessionManager;

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
    Button favoriteButton;


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

        favoriteButton.setOnClickListener(view -> addRecipeToFavorites(String.valueOf(id)));
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
        favoriteButton = findViewById(R.id.favButton);
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

    private void addRecipeToFavorites(String recipeId) {
        // Use UserSessionManager to get the current user's ID from session
        UserSessionManager sessionManager = new UserSessionManager(this);
        String userId = sessionManager.getUserIdFromStorage();

        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query for the Favorites object linked to the user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", userId);

        // Retrieve the first matching document
        query.getFirstInBackground((favoriteDoc, e) -> {
            if (e == null) {
                // If Favorites document exists, add the recipeId to the list
                List<String> recipeIds = favoriteDoc.getList("recipeIds");
                if (!recipeIds.contains(recipeId)) {
                    recipeIds.add(recipeId);
                    favoriteDoc.put("recipeIds", recipeIds);
                    favoriteDoc.saveInBackground(e1 -> {
                        if (e1 == null) {
                            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            favoriteButton.setText("Favorited");
                        } else {
                            Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Already in favorites", Toast.LENGTH_SHORT).show();
                }
            } else {
                // If no existing Favorites document, create a new one
                ParseObject newFavorite = new ParseObject("Favorites");
                newFavorite.put("userId", userId);
                newFavorite.put("recipeIds", Arrays.asList(recipeId));
                newFavorite.saveInBackground(e1 -> {
                    if (e1 == null) {
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        favoriteButton.setText("Favorited");
                    } else {
                        Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    //Toggle favButton
    public void onDefaultToggleClick(View view) {
    }
}