package vn.edu.usth.nutritionrecipe.Listeners;

import java.util.List;

import vn.edu.usth.nutritionrecipe.Models.InstructionsResponse;

public interface InstructionsListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}
