package vn.edu.usth.nutritionrecipe.Listeners;

import java.util.List;

import vn.edu.usth.nutritionrecipe.Models.SimilarRecipeResponse;

public interface SimilarRecipesListener {
    void didFetch(List<SimilarRecipeResponse> response, String message);
    void didError(String message);
}
