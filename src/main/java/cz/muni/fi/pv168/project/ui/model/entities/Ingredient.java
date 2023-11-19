package cz.muni.fi.pv168.project.ui.model.entities;

import java.util.Objects;

public class Ingredient {
    private String name;
    private double calories;
    private Unit unit;
    private double amount = 0;

    public Ingredient(String name, double calories, Unit unit) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
    }

    public Ingredient(String name, double calories, Unit unit, double amount) {
        this(name, calories, unit);
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getAmount() {
        return amount;
    }

    public double getCaloriesPerSetAmount() {
        return amount * calories;
    }

    @Override
    public String toString() {
        return String.format("Ingredient{name: %s; calories: %.2f; unit: %s; amount: %.2f}",
                name, calories, unit.getName(), amount);
    }
}