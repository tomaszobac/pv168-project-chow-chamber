package cz.muni.fi.pv168.project.testGen;

import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories.*;

/**
 *
 * Class for simple testing of recipe app.
 *
 */
public class TestTable {
    private static final String instructionsTemplate = "The missile knows where it is at all times. It knows this because it knows where it isn't.";
    private static final List<Recipe> tableOne = List.of(
            new Recipe("vomáčka", PRILOHA, LocalTime.parse("00:10"),4, new ArrayList<>(), instructionsTemplate),
            new Recipe("polívka", HLAVNI_JIDLO, LocalTime.parse("02:00"),20, new ArrayList<>(), instructionsTemplate),
            new Recipe("chleba", PRILOHA, LocalTime.parse("00:30"),6, new ArrayList<>(), instructionsTemplate),
            new Recipe("maso", HLAVNI_JIDLO, LocalTime.parse("00:20"),4, new ArrayList<>(), instructionsTemplate),
            new Recipe("dort", NONE, LocalTime.parse("01:00"),8, new ArrayList<>(), instructionsTemplate),
            new Recipe("nevim", NONE, LocalTime.parse("23:59"),99, new ArrayList<>(), instructionsTemplate));

    private static final List<Unit> tableTwo = List.of(
            new Unit("Liter", UnitType.Volume, 1.0),
            new Unit("Kilogram", UnitType.Weight, 1.0),
            new Unit("Cup", UnitType.Volume, 0.25), // 1 cup = 0.25 liters
            new Unit("Milliliter", UnitType.Volume, 0.001)); // 1 milliliter = 0.001 liters

    private static final List<Ingredient> tableThree = List.of(
        new Ingredient("Water", 0, tableTwo.get(0)),
        new Ingredient("Flour", 250, tableTwo.get(1)),
        new Ingredient("Sugar", 100, tableTwo.get(1)),
        new Ingredient("Salt", 5, tableTwo.get(1)));

    public static List<Recipe> getTableOne() {
        return tableOne;
    }
    public static List<Unit> getTableTwo() {
        return tableTwo;
    }
    public static List<Ingredient> getTableThree() {
        return tableThree;
    }
}
