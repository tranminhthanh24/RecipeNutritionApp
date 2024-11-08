package vn.edu.usth.nutritionrecipe;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    private UserSessionManager sessionManager;

    public RequestManager(Context context) {

        this.context = context;
        this.sessionManager = new UserSessionManager(context);
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


    public void loadFavoriteRecipes(RecipeDetailsListener listener) {
        String userId = sessionManager.getUserIdFromStorage();
        if (userId == null) return;  // Ensure the user is logged in

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("userId", userId);
        query.include("recipeIds");  // Assuming you have a list of recipeIds in the Favorites class
        query.findInBackground((objects, e) -> {
            if (e == null && objects.size() > 0) {
                ParseObject favorite = objects.get(0);
                List<Integer> recipeIds = favorite.getList("recipeIds");

                if (recipeIds != null && !recipeIds.isEmpty()) {
                    // Fetch the details for each favorite recipe
                    fetchRecipeDetailsForIds(recipeIds, listener);
                } else {
                    listener.didError("No favorite recipes found");
                }
            } else {
                listener.didError("Failed to load favorite recipes: " + (e != null ? e.getMessage() : "Unknown error"));
            }
        });
    }

    private void fetchRecipeDetailsForIds(List<Integer> recipeIds, RecipeDetailsListener listener) {
        for (int recipeId : recipeIds) {
            getRecipeDetails(listener, recipeId);  // Using your existing getRecipeDetails method
        }
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
