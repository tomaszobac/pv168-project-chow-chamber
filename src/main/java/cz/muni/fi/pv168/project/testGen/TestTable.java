package cz.muni.fi.pv168.project.testGen;

import cz.muni.fi.pv168.project.ui.MyTable;
import cz.muni.fi.pv168.project.ui.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import java.util.List;

/**
 *
 * Class for simple testing of recipe app.
 *
 */
public class TestTable {
    private static final List<Recipe> tableOne = List.of(
            new Recipe("vomáčka", "příloha", "00:10","4"),
            new Recipe("polívka", "hlavní chod", "02:00","20"),
            new Recipe("chleba", "příloha", "00:30","6"),
            new Recipe("maso", "hlavní chod", "00:20","4"),
            new Recipe("dort", "zákusek", "01:00","8"),
            new Recipe("nevim", "všechno", "99:00","99"));

    public static List<Recipe> getTableOne() {
        return tableOne;
    }
}
