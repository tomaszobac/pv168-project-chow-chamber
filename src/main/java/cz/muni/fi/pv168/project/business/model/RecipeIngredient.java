package cz.muni.fi.pv168.project.ui.model.entities;

public class RecipeIngredient extends Ingredient {
    Ingredient originalIngredient;
    Double amount;

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
