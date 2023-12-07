package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// TODO: I don't know if we should implement EntityTableModel and method refresh here as well
public class RecipeIngredientsTableModel extends AbstractTableModel {
    private final List<RecipeIngredient> ingredients;
    private final List<Column<RecipeIngredient, ?>> columns = List.of(
            Column.readonly("Name", String.class, RecipeIngredient::getName),
            Column.readonly("Amount", Double.class, RecipeIngredient::getAmount),
            Column.readonly("Unit", String.class, recipeIngredient -> recipeIngredient.getUnit().getName()),
            Column.readonly("Calories", Double.class, RecipeIngredient::getCaloriesPerSetAmount)
    );

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 2 -> String.class;
            case 1, 3 -> Double.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    public RecipeIngredientsTableModel(List<RecipeIngredient> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
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
        var ingredient = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(ingredient);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var ingredient = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, ingredient);
    }

    public void deleteRow(int rowIndex) {
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(RecipeIngredient ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public RecipeIngredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
