package vn.edu.usth.nutritionrecipe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.nutritionrecipe.Activity.LoginActivity;
import vn.edu.usth.nutritionrecipe.Activity.MainActivity;
import vn.edu.usth.nutritionrecipe.Activity.RecipeDetailsActivity;
import vn.edu.usth.nutritionrecipe.Adapter.RandomRecipeAdapter;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeClickListener;
import vn.edu.usth.nutritionrecipe.Models.Recipe;
import vn.edu.usth.nutritionrecipe.Models.RecipeDetailsResponse;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.RequestManager;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeDetailsListener;
import vn.edu.usth.nutritionrecipe.UserSessionManager;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private RandomRecipeAdapter adapter;
    private RequestManager requestManager;
    private List<String> favoriteRecipeIds = new ArrayList<>();
    private ImageButton fabRefresh;

    private UserSessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        sessionManager = new UserSessionManager(getContext());

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
        String userId = sessionManager.getUserIdFromStorage();
        if (userId == null) {
            Toast.makeText(getContext(), "Please log in to view favorites", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class)); // Redirect to login
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", userId);
        query.findInBackground((objects, e) -> {
            if (e == null && !objects.isEmpty()) {
                ParseObject favorite = objects.get(0);
                favoriteRecipeIds = favorite.getList("recipeIds");

                if (favoriteRecipeIds != null && !favoriteRecipeIds.isEmpty()) {
                    fetchFavoriteRecipesDetails();
                } else {
                    Toast.makeText(getContext(), "No favorite recipes found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to load favorites: " + (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFavoriteRecipesDetails() {
        if (favoriteRecipeIds.isEmpty()) {
            Toast.makeText(getContext(), "No favorite recipes available", Toast.LENGTH_SHORT).show();
            return;
        }

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
        loadFavoriteRecipes();
    }

    // Method to clear cache and restart the activity
    private void clearCacheAndRestart() {
        try {
            // Clear WebView cache
            WebView webView = new WebView(getContext());
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroy();

            // Clear app cache
            deleteDir(getContext().getCacheDir());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Restart main activity
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish(); // Close current activity
    }

    // Helper method to delete cache directory recursively
    private boolean deleteDir(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new java.io.File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
