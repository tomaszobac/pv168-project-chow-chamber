package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents an ingredient used in a recipe.
 * <p>
 * An ingredient has a name, calories, and a unit of measurement.
 * The name and unit are required and must not be null.
 * The calories represent the amount of calories in the ingredient.
 *
 * @see Entity
 */
public class Ingredient extends Entity implements Serializable {
    private String name;
    private double calories;
    private Unit unit;

    /**
     *
     * Creates an Ingredient object with the provided attributes.
     *
     * @param guid the unique identifier for the Ingredient
     * @param name the name of the Ingredient (must not be null)
     * @param calories the number of calories in the Ingredient
     * @param unit the unit of measurement for the Ingredient (must not be null)
     */
    @JsonCreator
    public Ingredient(@JsonProperty("guid") String guid,
                      @JsonProperty("name") String name,
                      @JsonProperty("calories") double calories,
                      @JsonProperty("unit") Unit unit) throws NumberFormatException {
        super(guid);
        this.name = Objects.requireNonNull(name, "Name must not be null");
        if (calories < 0) {
            throw new NumberFormatException("Calories must be bigger or equal 0");
        }
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
    }

    /**
     * Creates a new Ingredient object with the specified properties.
     *
     * @param name the name of the ingredient (must not be null)
     * @param calories the number of calories in the ingredient
     * @param unit the unit of measurement for the ingredient (must not be null)
     */
    public Ingredient(String name, double calories, Unit unit) throws NumberFormatException {
        this.name = Objects.requireNonNull(name, "Name must not be null");
        if (calories < 0) {
            throw new NumberFormatException("Calories must be bigger or equal 0");
        }
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "Unit must not be null");
    }

    /**
     * Get the name of the Ingredient.
     *
     * @return The name of the Ingredient.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unit associated with this method.
     *
     * @return the unit associated with this method.
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the name of the object.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /**
     * Sets the unit for the object.
     *
     * @param unit the unit to be set
     * @throws NullPointerException if the unit is null
     */
    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }

    /**
     * Retrieves the number of calories.
     *
     * @return The number of calories as a double value.
     */
    public double getCalories() {
        return calories;
    }

    /**
     * Sets the number of calories.
     *
     * @param calories the number of calories to set
     */
    public void setCalories(double calories) throws NumberFormatException {
        if (calories < 0) {
            throw new NumberFormatException("Calories must be bigger or equal 0");
        }
        this.calories = calories;
    }

    /**
     * Returns a string representation of the Ingredient object.
     *
     * @return the string representation of the Ingredient object.
     */
    @Override
    public String toString() {
        return String.format("Ingredient{guid: %s; name: %s; calories: %.2f; unit: %s}",
                getGuid(), name, calories, unit.getName());
    }
}