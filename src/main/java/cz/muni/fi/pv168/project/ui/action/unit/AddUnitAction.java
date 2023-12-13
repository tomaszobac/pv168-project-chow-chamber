package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.dialog.UnitDialog;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddUnitAction extends AbstractAction {
    private final JTable unitTable;

    public AddUnitAction(JTable unitTable) {
        super("Add Unit", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new unit");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.unitTable = unitTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var unitTableModel = (UnitTableModel) this.unitTable.getModel();
        var dialog = new UnitDialog(new Unit("Liter", UnitType.Volume,1.0));
        dialog.show(unitTable, "Add unit")
                .ifPresent(unitTableModel::addRow);
    }
}
