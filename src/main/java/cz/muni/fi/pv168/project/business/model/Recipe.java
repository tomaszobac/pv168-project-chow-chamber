package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe extends Entity {
    private String name;
    private String instructions;
    private RecipeCategory category;
    private LocalTime time;
    private int portions;
    private ArrayList<RecipeIngredient> ingredients;
    private int numberOfIngredients = 0;

    public Recipe(String guid, String name, RecipeCategory category, LocalTime time, int portions,
                  ArrayList<RecipeIngredient> ingredients, String instructions) {
        super(guid);
        this.name = name;
        this.category = category;
        this.time = time;
        this.portions = portions;
        this.ingredients = ingredients;
        this.numberOfIngredients = ingredients.size();
        this.instructions = instructions;
    }

    public Recipe(String name, RecipeCategory category, LocalTime time, int portions,
                  ArrayList<RecipeIngredient> ingredients, String instructions) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        this.portions = portions;
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.numberOfIngredients = ingredients.size();
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }

    @JsonCreator
    public Recipe(@JsonProperty("name") String name,
                  @JsonProperty("instructions") String instructions,
                  @JsonProperty("category") RecipeCategory category,
                  @JsonProperty("time") LocalTime time,
                  @JsonProperty("portions") int portions,
                  @JsonProperty("ingredients") ArrayList<RecipeIngredient> ingredients,
                  @JsonProperty("numberOfIngredients") int numberOfIngredients) {
        this(name, category, time, portions, ingredients, instructions);
        this.numberOfIngredients = numberOfIngredients;
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

    public String getCategoryName() {
        return category.getCategory();
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

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = Objects.requireNonNull(ingredients, "Ingredients must not be null");
        this.numberOfIngredients = ingredients.size();
    }

    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
        numberOfIngredients++;
    }

    public int getNumberOfIngredients() {
        return numberOfIngredients;
    }

    public Double getCalories() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }
        double calories = 0.0;

        for (RecipeIngredient recipeIngredient: ingredients) {
            calories += recipeIngredient.getCaloriesPerSetAmount();
        }
        return calories;
    }

    @Override
    public String toString() {
        return String.format("Recipe{category: %s; name: %s; time: %s; portions: %d, numberOfIngredients: %d}",
                category.getCategory(), name, time, portions, numberOfIngredients); // TODO: Decide whether to add instructions and ingredients
    }
}
