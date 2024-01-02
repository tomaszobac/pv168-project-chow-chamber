package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteRecipeAction extends AbstractAction {
    private final JTable recipeTable;
    public DeleteRecipeAction(JTable recipeTable) {
        super("Delete", Icons.DELETE_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) recipeTable.getModel();
        Arrays.stream(recipeTable.getSelectedRows())
                .map(recipeTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(recipeTableModel::deleteRow);
    }
}
