package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class IngredientTableModel extends AbstractTableModel implements EntityTableModel<Ingredient> {
    private  List<Ingredient> ingredients;
    private final CrudService<Ingredient> ingredientCrudService;
    private final List<Column<Ingredient, ?>> columns = List.of(
            Column.readonly("Ingredient", Ingredient.class, Ingredient -> Ingredient),
            Column.readonly("Name", String.class, Ingredient::getName),
            Column.readonly("Calories", Double.class, Ingredient::getCalories)
    );

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Ingredient.class;
            case 1 -> String.class;
            case 2 -> Double.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService) {
        this.ingredientCrudService = ingredientCrudService;
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
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
        ingredientCrudService.deleteByGuid(ingredients.get(rowIndex).getGuid());
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ingredient ingredient) {
        ingredientCrudService.create(ingredient).intoException();
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        ingredientCrudService.update(ingredient);
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteAll() {
        ingredientCrudService.deleteAll();
        refresh();
    }

    public void refresh() {
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
        fireTableDataChanged();
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
