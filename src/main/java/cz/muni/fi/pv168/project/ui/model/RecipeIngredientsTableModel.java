package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeIngredientsTableModel extends AbstractTableModel implements EntityTableModel<RecipeIngredient> {
    private List<RecipeIngredient> ingredients;
    private final CrudService<RecipeIngredient> recipeIngredientCrudService;
    private final List<Column<RecipeIngredient, ?>> columns;

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> RecipeIngredient.class;
            case 1, 3 -> String.class;
            case 2, 4 -> Double.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    public RecipeIngredientsTableModel(CrudService<RecipeIngredient> recipeIngredientCrudService) {
        this.recipeIngredientCrudService = Objects.requireNonNull(recipeIngredientCrudService, "recipeIngredientCrudService cannot be null");
        this.ingredients = new ArrayList<>(recipeIngredientCrudService.findAll());
        this.columns = List.of(
                Column.readonly("RecipeIngredient", RecipeIngredient.class, recipeIngredient -> recipeIngredient),
                Column.readonly("Name", String.class, recipeIngredient -> recipeIngredient.getIngredient().getName()),
                Column.readonly("Amount", Double.class, RecipeIngredient::getAmount),
                Column.readonly("Unit", String.class, recipeIngredient -> recipeIngredient.getUnit().getName()),
                Column.readonly("Calories", Double.class, recipeIngredient -> recipeIngredient.getCaloriesPerSetAmount(recipeIngredient.getIngredient().getUnit(), recipeIngredient.getIngredient().getCalories()))
        );
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
        recipeIngredientCrudService.deleteByGuid(ingredients.get(rowIndex).getGuid());
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(RecipeIngredient ingredient) {
        recipeIngredientCrudService.create(ingredient).intoException();
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void deleteAll() {
        recipeIngredientCrudService.deleteAll();
        refresh();
    }

    public void refresh() {
        this.ingredients = new ArrayList<>(recipeIngredientCrudService.findAll());
        fireTableDataChanged();
    }

    public RecipeIngredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
