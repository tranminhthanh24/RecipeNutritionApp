package vn.edu.usth.nutritionrecipe.Listeners;

import vn.edu.usth.nutritionrecipe.Models.RandomRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}
