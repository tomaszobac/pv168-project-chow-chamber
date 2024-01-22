package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RecipeIngredientUnitTest {
    @Test
    public void testConstructorAndGetters() {
        String guid = "123";
        Recipe recipe = new Recipe("Recipe", "Instructions", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4);
        Ingredient ingredient = new Ingredient("Ingredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);
        double amount = 200.0;

        RecipeIngredient recipeIngredient = new RecipeIngredient(guid, recipe, ingredient, unit, amount);

        assertEquals(guid, recipeIngredient.getGuid());
        assertEquals(recipe, recipeIngredient.getRecipe());
        assertEquals(ingredient, recipeIngredient.getIngredient());
        assertEquals(unit, recipeIngredient.getUnit());
        assertEquals(amount, recipeIngredient.getAmount(), 0);
    }

    @Test
    public void testConstructorWithInvalidAmount() {
        Recipe recipe = new Recipe("guidRecipe", "Recipe", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4, "Instructions");
        Ingredient ingredient = new Ingredient("guidIngredient", "Ingredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);

        assertThrows(NumberFormatException.class, () -> new RecipeIngredient(recipe, ingredient, unit, -100.0));
    }

    @Test
    public void testSetterMethods() {
        Recipe recipe = new Recipe("guidRecipe", "Recipe", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4, "Instructions");
        Ingredient ingredient = new Ingredient("guidIngredient", "Ingredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);
        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, unit, 200.0);

        Unit newUnit = new Unit("Milligram", UnitType.Weight, 0.1);
        double newAmount = 300.0;

        recipeIngredient.setUnit(newUnit);
        recipeIngredient.setAmount(newAmount);

        assertEquals(newUnit, recipeIngredient.getUnit());
        assertEquals(newAmount, recipeIngredient.getAmount(), 0);
    }

    @Test
    public void testGetCaloriesPerSetAmount() {
        Recipe recipe = new Recipe("guidRecipe", "Recipe", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4, "Instructions");
        Ingredient ingredient = new Ingredient("guidIngredient", "Ingredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);
        RecipeIngredient recipeIngredient = new RecipeIngredient(recipe, ingredient, unit, 200.0);

        Unit inUnit = new Unit("Milligram", UnitType.Weight, 0.1);
        double inCalories = 50.0;
        double expectedCalories = inCalories * (200.0 * unit.getConversionToBase()) / inUnit.getConversionToBase(); // Conversion from Grams to Milligrams

        double resultCalories = recipeIngredient.getCaloriesPerSetAmount(inUnit, inCalories);

        assertEquals(expectedCalories, resultCalories, 0);
    }

    @Test
    public void testToStringMethod() {
        String guid = "123";
        Recipe recipe = new Recipe("Recipe", "Instructions", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4);
        Ingredient ingredient = new Ingredient("Ingredient", 100.0, new Unit("Gram", UnitType.Weight, 1.0));
        Unit unit = new Unit("Gram", UnitType.Weight, 1.0);
        double amount = 200.0;

        RecipeIngredient recipeIngredient = new RecipeIngredient(guid, recipe, ingredient, unit, amount);

        String expectedToString = String.format("RecipeIngredient{guid: %s; recipeGuid: %s; ingredientGuid: %s; unit: %s; amount: %.2f}",
                guid, recipe.getGuid(), ingredient.getGuid(), unit.getName(), amount);

        assertEquals(expectedToString, recipeIngredient.toString());
    }
}
