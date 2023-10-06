package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {

    // TODO: Change Object everywhere into Recipe class
    private final List<Object> recipes;
    private final List<Column<Object, ?>> columns = List.of(
            Column.changeToEditable("Category", String.class),
            Column.changeToEditable("Name", String.class),
            Column.changeToEditable("Time", String.class),
            Column.changeToEditable("Portions", Integer.class)
    );

    public RecipeTableModel(List<Object> recipes) {
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
}
