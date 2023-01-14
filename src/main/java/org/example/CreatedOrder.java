package org.example;
import java.util.List;

public class CreatedOrder {
    List<String> ingredients;

    public CreatedOrder() {}

    public CreatedOrder(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
