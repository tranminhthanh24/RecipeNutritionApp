package vn.edu.usth.nutritionrecipe;

import android.content.Context;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;


public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    FirebaseFirestore firestore;
    String userId;

    public RequestManager(Context context) {
        this.context = context;

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        firestore.collection("favorites").document(userId)
                .update("id", FieldValue.arrayUnion(recipeId))
                .addOnSuccessListener(aVoid -> {
                    // Successfully added to favorites
                })
                .addOnFailureListener(e -> {
                    // Handle error, e.g., log or display error message
                });
    }

    public void removeFavoriteRecipe(int recipeId) {
        firestore.collection("favorites").document(userId)
                .update("id", FieldValue.arrayRemove(recipeId))
                .addOnSuccessListener(aVoid -> {
                    // Successfully removed from favorites
                })
                .addOnFailureListener(e -> {
                    // Handle error, e.g., log or display error message
                });
    }

    public void loadFavoriteRecipes(RecipeDetailsListener listener) {
        firestore.collection("favorites").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("id")) {
                        List<Integer> favoriteIds = (List<Integer>) documentSnapshot.get("id");
                        for (int id : favoriteIds) {
                            getRecipeDetails(listener, id); // Use existing method to fetch details
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    listener.didError(e.getMessage());
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
