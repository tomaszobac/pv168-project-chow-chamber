package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RecipeUnitTest {

    @Test
    public void testConstructorAndGetters() {
        String guid = "123";
        String name = "TestRecipe";
        RecipeCategory category = RecipeCategory.HLAVNI_JIDLO;
        LocalTime time = LocalTime.of(1, 30);
        int portions = 4;
        String instructions = "Test instructions";

        Recipe recipe = new Recipe(guid, name, category, time, portions, instructions);

        assertEquals(guid, recipe.getGuid());
        assertEquals(name, recipe.getName());
        assertEquals(category, recipe.getCategory());
        assertEquals(time, recipe.getTime());
        assertEquals(portions, recipe.getPortions());
        assertEquals(instructions, recipe.getInstructions());
    }

    @Test
    public void testConstructorWithInvalidPortions() {
        assertThrows(NumberFormatException.class, () -> {
            new Recipe("TestRecipe", "Test instructions", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 0);
        });
    }

    @Test
    public void testConstructorWithNullName() {
        assertThrows(NullPointerException.class, () -> {
            new Recipe(null, "Test instructions", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4);
        });
    }

    @Test
    public void testConstructorWithNullCategory() {
        assertThrows(NullPointerException.class, () -> {
            new Recipe("TestRecipe", "Test instructions", null, LocalTime.of(1, 30), 4);
        });
    }

    @Test
    public void testConstructorWithNullTime() {
        assertThrows(NullPointerException.class, () -> {
            new Recipe("TestRecipe", "Test instructions", RecipeCategory.HLAVNI_JIDLO, null, 4);
        });
    }

    @Test
    public void testConstructorWithNullInstructions() {
        assertThrows(NullPointerException.class, () -> {
            new Recipe("TestRecipe", null, RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4);
        });
    }

    @Test
    public void testSetterMethods() {
        Recipe recipe = new Recipe("TestRecipe", "Test instructions", RecipeCategory.HLAVNI_JIDLO, LocalTime.of(1, 30), 4);

        String newName = "UpdatedRecipe";
        RecipeCategory newCategory = RecipeCategory.ZAKUSEK;
        LocalTime newTime = LocalTime.of(0, 45);
        int newPortions = 2;
        String newInstructions = "Updated instructions";

        recipe.setName(newName);
        recipe.setCategory(newCategory);
        recipe.setTime(newTime);
        recipe.setPortions(newPortions);
        recipe.setInstructions(newInstructions);

        assertEquals(newName, recipe.getName());
        assertEquals(newCategory, recipe.getCategory());
        assertEquals(newTime, recipe.getTime());
        assertEquals(newPortions, recipe.getPortions());
        assertEquals(newInstructions, recipe.getInstructions());
    }

    @Test
    public void testToStringMethod() {
        String guid = "123";
        String name = "TestRecipe";
        RecipeCategory category = RecipeCategory.HLAVNI_JIDLO;
        LocalTime time = LocalTime.of(1, 30);
        int portions = 4;

        Recipe recipe = new Recipe(guid, name, category, time, portions, "Test instructions");

        String expectedToString = String.format("Recipe{guid: %s; category: %s; name: %s; time: %s; portions: %d}",
                guid, category.getCategory(), name, time, portions);

        assertEquals(expectedToString, recipe.toString());
    }
}
