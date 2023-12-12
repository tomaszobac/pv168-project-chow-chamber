package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class ClearFilterIngredientAction extends AbstractAction {
    private final IngredientTableFilter filter;

    public ClearFilterIngredientAction(IngredientTableFilter filter) {
        super("Clear filter", Icons.CLEAR_FIlTER_ICON);
        putValue(SHORT_DESCRIPTION, "Clears filters for ingredients");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift F"));
        this.filter = Objects.requireNonNull(filter);
        if (isNotFiltered()) {
            setEnabled(false);
        }
    }

    private boolean isNotFiltered() {
        String name = filter.getName();
        double caloriesFrom = filter.getCaloriesFrom();
        double caloriesTo = filter.getCaloriesTo();

        return name.isEmpty()
                && caloriesFrom == 0.0
                && caloriesTo == Double.MAX_VALUE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        filter.resetFilter();
        setEnabled(false);
    }
}
