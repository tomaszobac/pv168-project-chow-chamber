package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class IngredientTableModel extends AbstractTableModel implements EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;
    private final List<Column<Ingredient, ?>> columns = List.of(
            Column.readonly("Ingredient", Ingredient.class, Ingredient -> Ingredient),
            Column.readonly("Name", String.class, Ingredient::getName),
            Column.readonly("Calories", Double.class, Ingredient::getCalories)
    );

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Recipe.class;
            case 1 -> String.class;
            case 2 -> Double.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    public IngredientTableModel(List<Ingredient> Ingredients) {
        this.ingredients = new ArrayList<>(Ingredients);
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

    public void addRow(Ingredient ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
