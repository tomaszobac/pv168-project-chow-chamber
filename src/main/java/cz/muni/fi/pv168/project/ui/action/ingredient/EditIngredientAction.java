package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditIngredientAction extends AbstractAction {
    private final JTable ingredientTable;

    public EditIngredientAction(JTable ingredientTable) {
        super("Edit", Icons.EDIT_ICON);
        this.ingredientTable = ingredientTable;
        putValue(SHORT_DESCRIPTION, "Edits selected ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = ingredientTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (ingredientTable.isEditing()) {
            ingredientTable.getCellEditor().cancelCellEditing();
        }
        var ingredientTableModel = (IngredientTableModel) ingredientTable.getModel();
        int modelRow = ingredientTable.convertRowIndexToModel(selectedRows[0]);
        var ingredient = ingredientTableModel.getEntity(modelRow);
        var dialog = new IngredientDialog(ingredient, null);
        dialog.show(ingredientTable, "Edit ingredient")
                .ifPresent(ingredientTableModel::updateRow);
    }
}
