package cz.muni.fi.pv168.project.ui.action.unit;

import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterUnitTypeValues;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Optional;

public class ClearFilterUnitAction extends AbstractAction {
    private final UnitTableFilter filter;

    public ClearFilterUnitAction(UnitTableFilter filter) {
        super("Clear filter", Icons.CLEAR_FIlTER_ICON);
        putValue(SHORT_DESCRIPTION, "Clears filters for units");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift F"));
        this.filter = Objects.requireNonNull(filter);

        if (isNotFiltered()) {
            setEnabled(false);
        }
    }

    private boolean isNotFiltered() {
        Optional<SpecialFilterUnitTypeValues> optional = filter.getSelectedUnitType().getLeft();
        return filter.getName().isEmpty()
                && optional.isPresent()
                && optional.get().equals(SpecialFilterUnitTypeValues.ALL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        filter.resetFilter();
        setEnabled(false);
    }
}
