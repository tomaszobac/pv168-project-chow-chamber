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
                .map(recipeIngredientTable::convertRowIndexToModel).boxed()
                .sorted(Comparator.reverseOrder()).forEach(recipeIngredientTableModel::deleteRow);
    }
}
