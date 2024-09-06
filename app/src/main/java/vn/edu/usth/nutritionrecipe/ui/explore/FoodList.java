package vn.edu.usth.nutritionrecipe.ui.explore;

public class FoodList {
    public String name;
    public String time;
    int ingredients, description;
    public int image;

    public FoodList(String name, String time, int ingredients, int description, int image) {
        this.name = name;
        this.time = time;
        this.ingredients = ingredients;
        this.description = description;
        this.image = image;
    }
}
