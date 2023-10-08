package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {
    private final List<Recipe> recipes;
    private final List<Column<Recipe, ?>> columns = List.of(
            Column.changeToEditable("Name", String.class),
            Column.changeToEditable("Category", String.class),
            Column.changeToEditable("Time", String.class),
            Column.changeToEditable("Portions", Integer.class)
    );

    public RecipeTableModel(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    @Override
    public int getRowCount() {
        return recipes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }

    public void addRow(Recipe recipe) {
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }
}
