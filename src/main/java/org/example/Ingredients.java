package org.example;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {
    private List<String> ingredients = new ArrayList<>();

    public void addIngredient(String ingredient){
        this.ingredients.add(ingredient);
    }
    public List<String> getIngredients(){
        return ingredients;
    }
}
