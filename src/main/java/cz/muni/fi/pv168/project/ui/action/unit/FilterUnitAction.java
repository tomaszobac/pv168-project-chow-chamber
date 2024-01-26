package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.ui.dialog.FilterUnitDialog;
import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FilterUnitAction extends AbstractAction {
    private final UnitTable unitTable;
    private final UnitTableFilter filter;
    private final Action clearFilterAction;
    public FilterUnitAction(UnitTable unitTable, UnitTableFilter filter, Action clearFilterAction) {
        super("Filter units", Icons.FILTER_ICON);
        putValue(SHORT_DESCRIPTION, "Filters units");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
        this.unitTable = unitTable;
        this.filter = filter;
        this.clearFilterAction = clearFilterAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new FilterUnitDialog(this.filter);
        dialog.show(unitTable, "Filter units");
        clearFilterAction.setEnabled(true);
    }
}
