package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
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
                // view row index must be converted to model row index
                .map(recipeTable::convertRowIndexToModel)
                .boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder())
                .forEach(recipeTableModel::deleteRow);
    }
}
