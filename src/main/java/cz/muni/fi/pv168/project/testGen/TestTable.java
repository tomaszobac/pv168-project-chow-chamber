package cz.muni.fi.pv168.project.testGen;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory.HLAVNI_JIDLO;
import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory.NONE;
import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory.PRILOHA;


/**
 *
 * Class for simple testing of recipe app.
 *
 */
public class TestTable {
    private static final String instructionsTemplate = "The missile knows where it is at all times. It knows this because it knows where it isn't.";
    private static final List<Recipe> tableOne = List.of(
            new Recipe("guid1", "vomáčka", PRILOHA, LocalTime.parse("00:10"),4, new ArrayList<>(), instructionsTemplate),
            new Recipe("guid2", "polívka", HLAVNI_JIDLO, LocalTime.parse("02:00"),20, new ArrayList<>(), instructionsTemplate),
            new Recipe("guid3", "chleba", PRILOHA, LocalTime.parse("00:30"),6, new ArrayList<>(), instructionsTemplate),
            new Recipe("guid4", "maso", HLAVNI_JIDLO, LocalTime.parse("00:20"),4, new ArrayList<>(), instructionsTemplate),
            new Recipe("guid5", "dort", NONE, LocalTime.parse("01:00"),8, new ArrayList<>(), instructionsTemplate),
            new Recipe("guid6", "nevim", NONE, LocalTime.parse("23:59"),99, new ArrayList<>(), instructionsTemplate));

    private static final List<Unit> tableTwo = List.of(
            new Unit("guid1", "Liter", UnitType.Volume, 1.0),
            new Unit("guid2", "Kilogram", UnitType.Weight, 1.0),
            new Unit("guid3", "Cup", UnitType.Volume, 0.25), // 1 cup = 0.25 liters
            new Unit("guid4", "Milliliter", UnitType.Volume, 0.001)); // 1 milliliter = 0.001 liters

    private static final List<Ingredient> tableThree = List.of(
        new Ingredient("guid1", "Water", 0, tableTwo.get(0)),
        new Ingredient("guid2", "Flour", 250, tableTwo.get(1)),
        new Ingredient("guid3", "Sugar", 100, tableTwo.get(1)),
        new Ingredient("guid4", "Salt", 5, tableTwo.get(1)));

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
