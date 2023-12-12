package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.ui.dialog.FilterRecipeDialog;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeTable;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class FilterRecipeAction extends AbstractAction {
    private final RecipeTableFilter filter;
    private final Action clearFilterAction;
    private final RecipeTable recipeTable;
    public FilterRecipeAction(RecipeTable recipeTable, RecipeTableFilter filter, Action clearFilterAction) {
        super("Filter", Icons.FILTER_ICON);
        putValue(SHORT_DESCRIPTION, "Filters recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl F"));
        this.filter = filter;
        this.clearFilterAction = clearFilterAction;
        this.recipeTable = recipeTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = new FilterRecipeDialog(this.filter);
        dialog.show(recipeTable, "Filter recipes");
        clearFilterAction.setEnabled(true);
    }
}
