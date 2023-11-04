package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.ui.dialog.UnitDialog;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditUnitAction extends AbstractAction {
    private final JTable unitTable;

    public EditUnitAction(JTable unitTable) {
        super("Edit", Icons.EDIT_ICON);
        this.unitTable = unitTable;
        putValue(SHORT_DESCRIPTION, "Edits selected unit");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = unitTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (unitTable.isEditing()) {
            unitTable.getCellEditor().cancelCellEditing();
        }
        var unitTableModel = (UnitTableModel) unitTable.getModel();
        int modelRow = unitTable.convertRowIndexToModel(selectedRows[0]);
        var unit = unitTableModel.getEntity(modelRow);
        var dialog = new UnitDialog(unit);
        dialog.show(unitTable, "Edit unit")
                .ifPresent(unitTableModel::updateRow);
    }
}
