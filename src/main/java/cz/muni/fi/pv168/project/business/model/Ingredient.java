package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Ingredient extends Entity {
    private String name;
    private double calories;
    private Unit unit;

    public Ingredient(String guid, String name, double calories, Unit unit) {
        super(guid);
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
    }

    @JsonCreator
    public Ingredient(@JsonProperty("name") String name,
                      @JsonProperty("calories") double calories,
                      @JsonProperty("unit") Unit unit) {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
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


    @Override
    public String toString() {
        return String.format("Ingredient{name: %s; calories: %.2f; unit: %s}",
                name, calories, unit.getName());
    }
}