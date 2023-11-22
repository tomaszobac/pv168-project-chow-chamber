package cz.muni.fi.pv168.project.ui.action.recipeIngredient;

import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteRecipeIngredientAction extends AbstractAction {
    private final JTable recipeIngredientTable;

    public DeleteRecipeIngredientAction(JTable recipeIngredientTable) {
        super("Delete", Icons.DELETE_ICON);
        this.recipeIngredientTable = recipeIngredientTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected ingredients");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeIngredientTableModel = (RecipeIngredientsTableModel) recipeIngredientTable.getModel();
        Arrays.stream(recipeIngredientTable.getSelectedRows())
                // view row index must be converted to model row index
                .map(recipeIngredientTable::convertRowIndexToModel).boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder()).forEach(recipeIngredientTableModel::deleteRow);
    }
}
