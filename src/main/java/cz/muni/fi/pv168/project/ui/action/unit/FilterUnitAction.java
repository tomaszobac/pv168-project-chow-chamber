package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.ui.dialog.FilterUnitDialog;
import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FilterUnitAction extends AbstractAction {
    private final UnitTable unitTable;
    private final UnitTableFilter filter;
    public FilterUnitAction(UnitTable unitTable, UnitTableFilter filter) {
        super("Filter", Icons.FILTER_ICON);
        putValue(SHORT_DESCRIPTION, "Filters recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
        this.unitTable = unitTable;
        this.filter = filter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new FilterUnitDialog(this.filter);
        dialog.show(unitTable, "Filter units");
    }
}
