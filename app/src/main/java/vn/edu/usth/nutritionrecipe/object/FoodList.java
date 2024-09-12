package vn.edu.usth.nutritionrecipe.object;
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

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getIngredients() {
        return ingredients;
    }

    public int getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
