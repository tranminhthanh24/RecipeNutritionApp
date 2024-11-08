package vn.edu.usth.nutritionrecipe;

import android.content.Context;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.usth.nutritionrecipe.Listeners.InstructionsListener;
import vn.edu.usth.nutritionrecipe.Listeners.RandomRecipeResponseListener;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeDetailsListener;
import vn.edu.usth.nutritionrecipe.Listeners.SimilarRecipesListener;
import vn.edu.usth.nutritionrecipe.Models.InstructionsResponse;
import vn.edu.usth.nutritionrecipe.Models.RandomRecipeApiResponse;
import vn.edu.usth.nutritionrecipe.Models.RecipeDetailsResponse;
import vn.edu.usth.nutritionrecipe.Models.SimilarRecipeResponse;


public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public  void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipes(context.getString(R.string.api_key), "20", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(String.valueOf(id), context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }else{
                    listener.didFetch(response.body(), response.message());
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
                return;
            }
        });
    }

    public void getSimilarRecipes(SimilarRecipesListener listener, int id){
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipeResponse>> call = callSimilarRecipes.callSimilarRecipes(String.valueOf(id), "5", context.getString(R.string.api_key));
        call.enqueue(new Callback<List<SimilarRecipeResponse>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipeResponse>> call, Response<List<SimilarRecipeResponse>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }else{
                    listener.didFetch(response.body(), response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SimilarRecipeResponse>> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getInstructions(InstructionsListener listener, int id){
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<InstructionsResponse>> call = callInstructions.callInstructions(String.valueOf(id), context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<InstructionsResponse>> call, Response<List<InstructionsResponse>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }else{
                    listener.didFetch(response.body(), response.message());

                }
            }

            @Override
            public void onFailure(Call<List<InstructionsResponse>> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void addFavoriteRecipe(int recipeId) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) return;  // Ensure the user is logged in

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", currentUser.getObjectId());
        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseObject favorite = objects.get(0);
                favorite.add("recipeIds", recipeId);  // Adding to list of favorite recipe IDs
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("AddFavorite", "Error adding recipe to favorites: " + e.getMessage());
                        } else {
                            Log.d("AddFavorite", "Recipe successfully added to favorites");
                        }
                    }
                });
            } else {
                // No favorites record, create a new one
                ParseObject favorite = new ParseObject("Favorites");
                favorite.put("userId", currentUser.getObjectId());
                favorite.add("recipeIds", recipeId);
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("AddFavorite", "Error creating new favorite record: " + e.getMessage());
                        } else {
                            Log.d("AddFavorite", "New favorite record created and recipe added");
                        }
                    }
                });
            }
        });
    }

    public void removeFavoriteRecipe(int recipeId) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) return;  // Ensure the user is logged in

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", currentUser.getObjectId());
        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseObject favorite = objects.get(0);
                // Remove the recipeId from the list of favorite recipes
                favorite.removeAll("recipeIds", List.of(recipeId));  // Ensure it's removing the ID properly from the list
                favorite.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("RemoveFavorite", "Error removing recipe from favorites: " + e.getMessage());
                        } else {
                            Log.d("RemoveFavorite", "Recipe successfully removed from favorites");
                        }
                    }
                });
            } else {
                Log.e("RemoveFavorite", "No favorites record found for the user");
            }
        });
    }


    public void loadFavoriteRecipes(RecipeDetailsListener listener) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) return;  // Ensure the user is logged in

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", currentUser.getObjectId());
        query.include("recipeIds");  // Assuming "recipeIds" is a list field in the Favorites class
        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseObject favorite = objects.get(0);
                List<Integer> recipeIds = favorite.getList("recipeIds");
                if (recipeIds != null) {
                    for (int id : recipeIds) {
                        getRecipeDetails(listener, id); // Use existing method to fetch recipe details for each favorite
                    }
                }
            } else {
                listener.didError(e != null ? e.getMessage() : "Failed to load favorites.");
            }
        });
    }

    private interface CallRandomRecipes {
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipes(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }

    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") String id,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
        Call<List<SimilarRecipeResponse>> callSimilarRecipes(
                @Path("id") String id,
                @Query("number") String number,
                @Query("apiKey") String apiKey
        );
    }

    private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsResponse>> callInstructions(
                @Path("id") String id,
                @Query("apiKey") String apiKey
        );
    }
}
