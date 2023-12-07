package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RecipeIngredient extends Entity {
    String recipeGuid;
    String ingredientGuid;
    Recipe recipe;
    Ingredient ingredient;
    Unit unit;
    double amount;


    public RecipeIngredient(String guid, String recipeGuid, String ingredientGuid, Unit unit, double amount) {
        super(guid);
        this.recipeGuid = recipeGuid;
        this.ingredientGuid = ingredientGuid;
        this.unit = unit;
        this.amount = amount;
    }

    @JsonCreator
    public RecipeIngredient(@JsonProperty("recipeGuid") String recipeGuid,
                            @JsonProperty("ingredientGuid") String ingredientGuid,
                            @JsonProperty("unit") Unit unit,
                            @JsonProperty("amount") double amount) {
        this.recipeGuid = recipeGuid;
        this.ingredientGuid = ingredientGuid;
        this.unit = unit;
        this.amount = amount;
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Unit unit, double amount) {
        this.recipe = Objects.requireNonNull(recipe, "Recipe cannot be null");
        this.ingredient = Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        this.unit = unit;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public double getCaloriesPerSetAmount() {
        return ingredient.getCalories() * (amount * unit.getConversionToBase()) / ingredient.getUnit().getConversionToBase();
    }

    public String getRecipeGuid() {
        return recipeGuid;
    }

    public String getIngredientGuid() {
        return ingredientGuid;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return String.format("RecipeIngredient{recipeGuid: %s; ingredientGuid: %s; unit: %s; amount: %.2f}",
                recipe.getGuid(), ingredient.getGuid(), unit.getName(), amount);
    }
}
