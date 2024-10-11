package vn.edu.usth.nutritionrecipe.Listeners;

import vn.edu.usth.nutritionrecipe.Models.RecipeDetailsResponse;

public interface RecipeDetailsListener {
    void didFetch(RecipeDetailsResponse response, String message);
    void didError(String message);

}
