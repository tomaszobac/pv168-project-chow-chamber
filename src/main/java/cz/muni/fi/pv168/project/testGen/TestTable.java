package cz.muni.fi.pv168.project.testGen;

import cz.muni.fi.pv168.project.ui.model.*;

import java.util.List;

import static cz.muni.fi.pv168.project.ui.model.RecipeCategories.*;

/**
 *
 * Class for simple testing of recipe app.
 *
 */
public class TestTable {
    private static final List<Recipe> tableOne = List.of(
            new Recipe("vomáčka", příloha, "00:10","4"),
            new Recipe("polívka", hlavní_jídlo, "02:00","20"),
            new Recipe("chleba", příloha, "00:30","6"),
            new Recipe("maso", hlavní_jídlo, "00:20","4"),
            new Recipe("dort", všechno, "01:00","8"),
            new Recipe("nevim", všechno, "99:00","99"));

    private static final List<Unit> tableTwo = List.of(
            new Unit("Liter", UnitType.Volume, 1.0),
            new Unit("Kilogram", UnitType.Weight, 1.0),
            new Unit("Cup", UnitType.Volume, 0.25), // 1 cup = 0.25 liters
            new Unit("Milliliter", UnitType.Volume, 0.001)); // 1 milliliter = 0.001 liters

    private static final List<Ingredient> tableThree = List.of(
        new Ingredient("Water", 0, tableTwo.get(0)),
        new Ingredient("Flour", 250, tableTwo.get(1)));

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
