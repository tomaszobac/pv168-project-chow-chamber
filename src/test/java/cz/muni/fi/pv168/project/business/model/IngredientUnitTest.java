package cz.muni.fi.pv168.project.business.model;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class IngredientUnitTest {

    @Test
    public void testConstructorAndGetters() {
        String guid = "123";
        String name = "TestIngredient";
        double calories = 100.0;
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);

        Ingredient ingredient = new Ingredient(guid, name, calories, unit);

        assertEquals(guid, ingredient.getGuid());
        assertEquals(name, ingredient.getName());
        assertEquals(calories, ingredient.getCalories(), 0);
        assertEquals(unit, ingredient.getUnit());
    }

    @Test
    public void testConstructorWithInvalidCalories() {
        assertThrows(NumberFormatException.class, () -> {
            new Ingredient("TestIngredient", -50.0, new Unit("Gram", UnitType.Weight, 1.0));
        });
    }

    @Test
    public void testConstructorWithNullName() {
        assertThrows(NullPointerException.class, () -> {
            new Ingredient(null, 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        });
    }

    @Test
    public void testConstructorWithNullUnit() {
        assertThrows(NullPointerException.class, () -> {
            new Ingredient("TestIngredient", 100.0, null);
        });
    }

    @Test
    public void testSetterMethods() {
        Ingredient ingredient = new Ingredient("TestIngredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));

        String newName = "UpdatedIngredient";
        double newCalories = 150.0;
        Unit newUnit = new Unit("Milligram", UnitType.Weight, 0.1);

        ingredient.setName(newName);
        ingredient.setCalories(newCalories);
        ingredient.setUnit(newUnit);

        assertEquals(newName, ingredient.getName());
        assertEquals(newCalories, ingredient.getCalories(), 0);
        assertEquals(newUnit, ingredient.getUnit());
    }

    @Test
    public void testToStringMethod() {
        String guid = "123";
        String name = "TestIngredient";
        double calories = 100.0;
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);

        Ingredient ingredient = new Ingredient(guid, name, calories, unit);

        String expectedToString = String.format("Ingredient{guid: %s; name: %s; calories: %.2f; unit: %s}",
                guid, name, calories, unit.getName());

        assertEquals(expectedToString, ingredient.toString());
    }
}