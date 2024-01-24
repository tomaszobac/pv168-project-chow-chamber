package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditIngredientAction extends AbstractAction {
    private final JTable ingredientTable;
    private final JTable unitTable;

    public EditIngredientAction(JTable ingredientTable, JTable unitTable) {
        super("Edit", Icons.EDIT_ICON);
        this.ingredientTable = ingredientTable;
        this.unitTable = unitTable;
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
        var dialog = new IngredientDialog(ingredient, unitTable);
        var result = dialog.show(ingredientTable, "Edit ingredient");
        while(dialog.getReturnedOK() && result.isEmpty()) {
            dialog = new IngredientDialog(ingredient, unitTable);
            result = dialog.show(ingredientTable, "Edit ingredient");
        }
        result.ifPresent(ingredientTableModel::updateRow);
    }
}
