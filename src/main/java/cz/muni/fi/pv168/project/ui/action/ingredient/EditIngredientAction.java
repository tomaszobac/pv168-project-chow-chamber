package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditIngredientAction extends AbstractAction {
    private final JTable recipeTable;

    public EditIngredientAction(JTable recipeTable) {
        super("Edit", Icons.EDIT_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Edits selected recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = recipeTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (recipeTable.isEditing()) {
            recipeTable.getCellEditor().cancelCellEditing();
        }
        var recipeTableModel = (RecipeTableModel) recipeTable.getModel();
        int modelRow = recipeTable.convertRowIndexToModel(selectedRows[0]);
        var recipe = recipeTableModel.getEntity(modelRow);
        var dialog = new RecipeDialog(recipe);
        dialog.show(recipeTable, "Edit Employee")
                .ifPresent(recipeTableModel::updateRow);
    }
}
