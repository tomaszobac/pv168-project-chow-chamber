package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

public class ClearFilterRecipeAction extends AbstractAction {
    private final RecipeTableFilter filter;

    public ClearFilterRecipeAction(RecipeTableFilter filter) {
        super("Clear filter", Icons.CLEAR_FIlTER_ICON);
        putValue(SHORT_DESCRIPTION, "Clears filters for recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift F"));
        this.filter = Objects.requireNonNull(filter);
        if (isNotFiltered()) {
            setEnabled(false);
        }
    }

    private boolean isNotFiltered() {
        String name = filter.getName();
        Optional<SpecialFilterCategoryValues> optionalAll = filter.getSelectedCategoryValue().getLeft();
        int portionsFrom = filter.getPortionsFrom();
        int portionsTo = filter.getPortionsTo();
        LocalTime timeFrom = filter.getTimeFrom();
        LocalTime timeTo = filter.getTimeTo();

        return name.isEmpty()
                && optionalAll.isPresent()
                && optionalAll.get().equals(SpecialFilterCategoryValues.ALL)
                && portionsFrom == 0.0
                && portionsTo == Integer.MAX_VALUE
                && timeFrom.equals(LocalTime.of(0, 0))
                && timeTo.equals(LocalTime.of(23, 59));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        filter.resetFilter();
        setEnabled(false);
    }
}
