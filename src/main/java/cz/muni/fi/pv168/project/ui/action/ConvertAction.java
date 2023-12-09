package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.dialog.ConverterDialog;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ConvertAction extends AbstractAction {
    private final UnitTable unitTable;

    public ConvertAction(UnitTable unitTable) {
        super("Convert", Icons.BALANCE_ICON);
        putValue(SHORT_DESCRIPTION, "Converting calculator");
        putValue(MNEMONIC_KEY, KeyEvent.VK_K);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl K"));

        this.unitTable = unitTable;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new ConverterDialog(unitTable);
        dialog.show(unitTable, "Converter");
    }
}
