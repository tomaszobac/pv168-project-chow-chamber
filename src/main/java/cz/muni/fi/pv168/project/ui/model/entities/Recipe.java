package cz.muni.fi.pv168.project.ui.model.entities;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe {
    private String name;
    private String instructions;
    private RecipeCategory category;
    private LocalTime time;
    private int portions;
    private ArrayList<Ingredient> ingredients;
    private int numberOfIngredients = 0;

    public Recipe(String name, RecipeCategory category, LocalTime time, int portions,
                  ArrayList<Ingredient> ingredients, String instructions) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        this.portions = portions;
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name,"name must not be null");
    }

    public RecipeCategory getCategory() {
        return category;
    }

    public void setCategory(RecipeCategory category) {
        this.category = Objects.requireNonNull(category,"Category must not be null");
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = Objects.requireNonNull(time,"Time must not be null");
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = Objects.requireNonNull(instructions, "Instructions must not be null");
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = Objects.requireNonNull(ingredients, "Ingredients must not be null");
    }
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        numberOfIngredients++;
    }

    @Override
    public String toString() {
        return String.format("Recipe{category: %s; name: %s; time: %s; portions: %d, numberOfIngredients: %d}",
                category.getCategory(), name, time, portions, numberOfIngredients); // TODO: Decide whether to add instructions and ingredients
    }
}
