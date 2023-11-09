package cz.muni.fi.pv168.project.business.model;

public class Ingredient extends Entity {
    private String name;
    private double calories;
    private Unit unit;
    private double amount = 0;

    public Ingredient(String guid, String name, double calories, Unit unit) {
        super(guid);
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }
    public Ingredient(String name, double calories, Unit unit) {
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }

    public Ingredient(String name, double calories, Unit unit, double amount) {
        this.name = name;
        this.calories = calories;
        this.unit = unit;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getUnitName() {
        return unit.getName();
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return name;
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
}