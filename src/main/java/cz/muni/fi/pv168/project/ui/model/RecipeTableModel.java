package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel implements EntityTableModel<Recipe> {
    private List<Recipe> recipes;
    private final CrudService<Recipe> recipeCrudService;
    private final List<Column<Recipe, ?>> columns = List.of(
            Column.readonly("Recipe", Recipe.class, Recipe -> Recipe),
            Column.readonly("Name", String.class, Recipe::getName),
            Column.readonly("Category", String.class, Recipe::getCategoryName),
            Column.readonly("Time", LocalTime.class, Recipe::getTime),
            Column.readonly("Portions", Integer.class, Recipe::getPortions)
    );

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Recipe.class;
            case 1, 2 -> String.class;
            case 3 -> LocalTime.class;
            case 4 -> Integer.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

    public RecipeTableModel(CrudService<Recipe> recipeCrudService) {
        this.recipeCrudService = recipeCrudService;
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
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

    public void deleteRow(int rowIndex) {
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Recipe recipe) {
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Recipe recipe) {
        int rowIndex = recipes.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void refresh() {
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
        fireTableDataChanged();
    }

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }
}
