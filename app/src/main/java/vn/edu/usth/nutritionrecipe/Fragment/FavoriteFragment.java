package vn.edu.usth.nutritionrecipe.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

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
    private FirebaseFirestore firestore;
    private String userId;
    private RequestManager requestManager;
    private List<Integer> favoriteRecipeIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recycler_favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        requestManager = new RequestManager(getContext());

        adapter = new RandomRecipeAdapter(getContext(), new ArrayList<>(), recipeClickListener);
        recyclerView.setAdapter(adapter);

        loadFavoriteRecipes();

        return view;
    }

    private void loadFavoriteRecipes() {
        firestore.collection("favorites").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("recipeIds")) {
                        // Type safety: Ensure IDs are castable to Integer
                        List<?> ids = (List<?>) documentSnapshot.get("recipeIds");
                        favoriteRecipeIds.clear();
                        for (Object id : ids) {
                            if (id instanceof Integer) {
                                favoriteRecipeIds.add((Integer) id);
                            } else if (id instanceof Long) {
                                favoriteRecipeIds.add(((Long) id).intValue());
                            }
                        }
                        fetchFavoriteRecipesDetails();
                    } else {
                        Toast.makeText(getContext(), "No favorite recipes found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show());
    }

    private void fetchFavoriteRecipesDetails() {
        List<Recipe> favoriteRecipes = new ArrayList<>();

        for (int recipeId : favoriteRecipeIds) {
            requestManager.getRecipeDetails(new RecipeDetailsListener() {
                @Override
                public void didFetch(RecipeDetailsResponse response, String message) {
                    // Create a Recipe object based on RecipeDetailsResponse data
                    Recipe recipe = new Recipe();
                    recipe.id = response.id;
                    recipe.title = response.title;
                    recipe.image = response.image;
                    recipe.servings = response.servings;
                    recipe.readyInMinutes = response.readyInMinutes;

                    favoriteRecipes.add(recipe);

                    // Option 1: Set all recipes at once after all details are fetched
                    if (favoriteRecipes.size() == favoriteRecipeIds.size()) {
                        adapter.setRecipes(favoriteRecipes);
                    }

                    // Option 2: Incrementally add each recipe to the adapter (uncomment if preferred)
                    // adapter.addRecipe(recipe);
                }

                @Override
                public void didError(String message) {
                    Toast.makeText(getContext(), "Error fetching recipe: " + message, Toast.LENGTH_SHORT).show();
                }
            }, recipeId);
        }
    }

    private final RecipeClickListener recipeClickListener = id -> {
        // Handle recipe click (e.g., open detail activity)
        Toast.makeText(getContext(), "Recipe ID: " + id, Toast.LENGTH_SHORT).show();
    };
}