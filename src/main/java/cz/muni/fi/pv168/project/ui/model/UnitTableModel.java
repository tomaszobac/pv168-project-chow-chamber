package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UnitTableModel extends AbstractTableModel implements EntityTableModel<Unit> {
    private List<Unit> units;
    private final UnitCrudService unitCrudService;
    private final List<Column<Unit, ?>> columns = List.of(
            Column.readonly("Name", String.class, Unit::getName),
            Column.readonly("Type", UnitType.class, Unit::getType),
            Column.readonly("Conversion to base", Double.class, Unit::getConversionToBase)
    );

    public UnitTableModel(UnitCrudService unitCrudService) {
        this.unitCrudService = unitCrudService;
        this.units = new ArrayList<>(unitCrudService.findAll());
    }

    @Override
    public int getRowCount() {
        return units.size();
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
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Unit ingredient) {
        int newRowIndex = units.size();
        units.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Unit ingredient) {
        int rowIndex = units.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void refresh() {
        this.units = new ArrayList<>(unitCrudService.findAll());
        fireTableDataChanged();
    }

    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }
}
