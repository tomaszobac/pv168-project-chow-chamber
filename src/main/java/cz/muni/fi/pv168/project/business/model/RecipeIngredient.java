package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecipeIngredient extends Ingredient {
    Ingredient originalIngredient;
    Double amount;

    @JsonCreator
    public RecipeIngredient(@JsonProperty("name") String name,
                            @JsonProperty("calories") double calories,
                            @JsonProperty("unit") Unit unit,
                            @JsonProperty("amount") double amount) {
        super(name, calories, unit);
        this.originalIngredient = new Ingredient(name, calories, unit);
        this.amount = amount;
    }

    public RecipeIngredient(String name, double calories, Unit unit, double amount, Ingredient originalIngredient) {
        super(name, calories, unit);
        this.originalIngredient = originalIngredient;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public double getCaloriesPerSetAmount() {
        return getCalories() * (getUnit().convertToBase(amount) / originalIngredient.getUnit().convertToBase(1));
    }

    @Override
    public String toString() {
        return String.format("RecipeIngredient{name: %s; caloriesPerSet: %.2f; unit: %s; amount: %.2f}",
                this.getName(), this.getCaloriesPerSetAmount(), this.getUnit().getName(), amount);
    }
}
