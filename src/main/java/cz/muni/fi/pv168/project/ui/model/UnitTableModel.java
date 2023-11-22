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
            Column.readonly("Unit", Unit.class, Unit -> Unit),
            Column.readonly("Name", String.class, Unit::getName),
            Column.readonly("Type", UnitType.class, Unit::getType),
            Column.readonly("Conversion to base", Double.class, Unit::getConversionToBase)
    );

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> Unit.class;
            case 1 -> String.class;
            case 2 -> UnitType.class;
            case 3 -> Double.class;
            default -> super.getColumnClass(columnIndex);
        };
    }

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
        var unit = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(unit);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        var unit = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, unit);
    }

    public void deleteRow(int rowIndex) {
        unitCrudService.deleteByGuid(units.get(rowIndex).getGuid());
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Unit unit) {
        unitCrudService.create(unit).intoException();
        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Unit unit) {
        unitCrudService.update(unit);
        int rowIndex = units.indexOf(unit);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteAll() {
        unitCrudService.deleteAll();
        refresh();
    }

    public void refresh() {
        this.units = new ArrayList<>(unitCrudService.findAll());
        fireTableDataChanged();
    }

    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }
}
