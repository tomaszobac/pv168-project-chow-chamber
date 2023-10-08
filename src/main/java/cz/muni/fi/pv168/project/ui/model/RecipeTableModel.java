package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {
    private final List<Recipe> recipes;
    private final List<Column<Recipe, ?>> columns = List.of(
            Column.editable("Name", String.class, Recipe::getName, Recipe::setName),
            Column.editable("Category", String.class, Recipe::getCategory, Recipe::setCategory),
            Column.editable("Time", String.class, Recipe::getTime, Recipe::setTime),
            Column.editable("Portions", String.class, Recipe::getPortions, Recipe::setPortions)
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, recipe);
    }

    public void addRow(Recipe recipe) {
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }
}
