package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteUnitAction extends AbstractAction {
    private final JTable unitTable;
    public DeleteUnitAction(JTable unitTable) {
        super("Delete", Icons.DELETE_ICON);
        this.unitTable = unitTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected units");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var unitTableModel = (UnitTableModel) unitTable.getModel();
        Arrays.stream(unitTable.getSelectedRows())
                .map(unitTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(unitTableModel::deleteRow);
    }
}
