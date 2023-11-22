package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.dialog.IngredientDialog;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddIngredientAction extends AbstractAction {
    private final JTable ingredientTable;
    private final JTable unitTable;

    public AddIngredientAction(JTable ingredientTable, JTable unitTable) {
        super("Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.ingredientTable = ingredientTable;
        this.unitTable = unitTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var ingredientTableModel = (IngredientTableModel) this.ingredientTable.getModel();
        var dialog = new IngredientDialog(new Ingredient("Flour", 250, TestTable.getTableTwo().get(1)), unitTable);
        dialog.show(ingredientTable, "Add ingredient")
                .ifPresent(ingredientTableModel::addRow);
    }
}
