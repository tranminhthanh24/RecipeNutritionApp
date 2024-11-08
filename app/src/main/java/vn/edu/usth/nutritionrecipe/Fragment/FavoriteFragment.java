package vn.edu.usth.nutritionrecipe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.nutritionrecipe.Activity.RecipeDetailsActivity;
import vn.edu.usth.nutritionrecipe.Adapter.RandomRecipeAdapter;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeClickListener;
import vn.edu.usth.nutritionrecipe.Models.Recipe;
import vn.edu.usth.nutritionrecipe.Models.RecipeDetailsResponse;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.RequestManager;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeDetailsListener;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RandomRecipeAdapter adapter;
    private RequestManager requestManager;
    private List<String> favoriteRecipeIds = new ArrayList<>();
    private FloatingActionButton fabRefresh;
    private List<Recipe> favoriteRecipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));


        requestManager = new RequestManager(getContext());

        adapter = new RandomRecipeAdapter(getContext(), new ArrayList<>(), recipeClickListener);
        recyclerView.setAdapter(adapter);

        // Initialize FloatingActionButton and set click listener
        fabRefresh = view.findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(v -> refreshFavoriteRecipes());

        loadFavoriteRecipes();

        return view;
    }

    private void loadFavoriteRecipes() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", currentUser.getObjectId());
        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseObject favorite = objects.get(0);
                favoriteRecipeIds = favorite.getList("recipeIds");

                if (favoriteRecipeIds != null && !favoriteRecipeIds.isEmpty()) {
                    fetchFavoriteRecipesDetails();
                } else {
                    Toast.makeText(getContext(), "No favorite recipes found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFavoriteRecipesDetails() {
        List<Recipe> favoriteRecipes = new ArrayList<>();

        for (String recipeId : favoriteRecipeIds) {
            requestManager.getRecipeDetails(new RecipeDetailsListener() {
                @Override
                public void didFetch(RecipeDetailsResponse response, String message) {
                    Recipe recipe = new Recipe();
                    recipe.id = response.id;
                    recipe.title = response.title;
                    recipe.image = response.image;
                    recipe.servings = response.servings;
                    recipe.readyInMinutes = response.readyInMinutes;

                    favoriteRecipes.add(recipe);

                    if (favoriteRecipes.size() == favoriteRecipeIds.size()) {
                        adapter.setRecipes(favoriteRecipes);
                    }
                }

                @Override
                public void didError(String message) {
                    Toast.makeText(getContext(), "Error fetching recipe: " + message, Toast.LENGTH_SHORT).show();
                }
            }, Integer.parseInt(recipeId));
        }
    }

    private final RecipeClickListener recipeClickListener = id -> {
        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    };

    // Method to refresh favorite recipes in the fragment
    private void refreshFavoriteRecipes() {
        // Reload favorite recipes from Firestore
        loadFavoriteRecipes();
    }
}
