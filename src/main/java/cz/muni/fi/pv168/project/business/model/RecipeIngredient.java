package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RecipeIngredient{
    Ingredient originalIngredient;
    double amount;
    Ingredient newIngredient;

    @JsonCreator
    public RecipeIngredient(@JsonProperty("name") String name,
                            @JsonProperty("calories") double calories,
                            @JsonProperty("unit") Unit unit,
                            @JsonProperty("amount") double amount) {
        this.originalIngredient = new Ingredient(name, calories, unit);
        this.newIngredient = this.originalIngredient;
        this.amount = amount;
    }

    public RecipeIngredient(String name, double calories, Unit unit, double amount, Ingredient originalIngredient) {
        this.newIngredient = new Ingredient(name, calories, unit);
        this.originalIngredient = Objects.requireNonNull(originalIngredient) ;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public double getCaloriesPerSetAmount() {
        return newIngredient.getCalories() * (amount * newIngredient.getUnit().getConversionToBase()) / newIngredient.getUnit().getConversionToBase();
    }

    public String getName() {
        return newIngredient.getName();
    }

    public double getCalories() {
        return newIngredient.getCalories();
    }

    public Unit getUnit() {
        return newIngredient.getUnit();
    }

    @Override
    public String toString() {
        return String.format("RecipeIngredient{name: %s; caloriesPerSet: %.2f; unit: %s; amount: %.2f}",
                newIngredient.getName(), this.getCaloriesPerSetAmount(), newIngredient.getUnit().getName(), amount);
    }
}
