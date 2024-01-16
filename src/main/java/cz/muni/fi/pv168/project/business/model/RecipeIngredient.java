package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RecipeIngredient extends Entity {
    final private Recipe recipe;
    final private Ingredient ingredient;
    private Unit unit;
    private double amount;

    @JsonCreator
    public RecipeIngredient(@JsonProperty("guid") String guid,
                            @JsonProperty("recipe") Recipe recipe,
                            @JsonProperty("ingredient") Ingredient ingredient,
                            @JsonProperty("unit") Unit unit,
                            @JsonProperty("amount") double amount) throws NumberFormatException {
        super(guid);
        this.recipe = Objects.requireNonNull(recipe);
        this.ingredient = Objects.requireNonNull(ingredient);
        this.unit = Objects.requireNonNull(unit);
        if (amount <= 0) {
            throw new NumberFormatException("Amount must be more than 0");
        }
        this.amount = amount;
    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Unit unit, double amount) throws NumberFormatException {
        Objects.requireNonNull(recipe.getGuid());
        Objects.requireNonNull(ingredient.getGuid());
        this.recipe = Objects.requireNonNull(recipe);
        this.ingredient = Objects.requireNonNull(ingredient);
        this.unit = Objects.requireNonNull(unit);
        if (amount <= 0) {
            throw new NumberFormatException("Amount must be more than 0");
        }
        this.amount = amount;
    }

    public double getCaloriesPerSetAmount(Unit inUnit, double inCalories) {
        return inCalories * (amount * unit.getConversionToBase()) / inUnit.getConversionToBase();
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

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) throws NumberFormatException {
        if (amount <= 0) {
            throw new NumberFormatException("Amount must be more than 0");
        }
        this.amount = amount;
    }
    @Override
    public String toString() {
        return String.format("RecipeIngredient{guid: %s; recipeGuid: %s; ingredientGuid: %s; unit: %s; amount: %.2f}",
                getGuid(), recipe.getGuid(), ingredient.getGuid(), unit.getName(), amount);
    }
}
