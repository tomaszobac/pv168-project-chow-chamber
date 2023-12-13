package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RecipeIngredient extends Entity {
    final private String recipeGuid;
    final private String ingredientGuid;
    final private Unit unit;
    final private double amount;

    @JsonCreator
    public RecipeIngredient(@JsonProperty("guid") String guid,
                            @JsonProperty("recipeGuid") String recipeGuid,
                            @JsonProperty("ingredientGuid") String ingredientGuid,
                            @JsonProperty("unit") Unit unit,
                            @JsonProperty("amount") double amount) {
        super(Objects.requireNonNull(guid));
        this.recipeGuid = Objects.requireNonNull(recipeGuid);
        this.ingredientGuid = Objects.requireNonNull(ingredientGuid);
        this.unit = Objects.requireNonNull(unit);
        this.amount = amount;
    }

    public RecipeIngredient(String recipeGuid, String ingredientGuid, Unit unit, double amount) {
        this.recipeGuid = Objects.requireNonNull(recipeGuid);
        this.ingredientGuid = Objects.requireNonNull(ingredientGuid);
        this.unit = Objects.requireNonNull(unit);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public double getCaloriesPerSetAmount(Unit inUnit, double inCalories) {
        return inCalories * (amount * unit.getConversionToBase()) / inUnit.getConversionToBase();
    }

    public String getRecipeGuid() {
        return recipeGuid;
    }

    public String getIngredientGuid() {
        return ingredientGuid;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return String.format("RecipeIngredient{guid: %s; recipeGuid: %s; ingredientGuid: %s; unit: %s; amount: %.2f}",
                getGuid(), recipeGuid, ingredientGuid, unit.getName(), amount);
    }
}
