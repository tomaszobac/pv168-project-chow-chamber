package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.dialog.FilterIngredientDialog;
import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.tables.IngredientsTable;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FilterIngredientAction extends AbstractAction {
    private final IngredientsTable ingredientTable;
    private final IngredientTableFilter filter;
    private final Action clearFilterAction;

    public FilterIngredientAction(IngredientsTable ingredientTable, IngredientTableFilter filter, Action clearFilterAction) {
        super("Filter ingredients", Icons.FILTER_ICON);
        putValue(SHORT_DESCRIPTION, "Filters ingredients");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
        this.ingredientTable = ingredientTable;
        this.filter = filter;
        this.clearFilterAction = clearFilterAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new FilterIngredientDialog(this.filter);
        dialog.show(ingredientTable, "Filter recipes");
        clearFilterAction.setEnabled(true);
    }
}
