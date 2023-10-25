package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static cz.muni.fi.pv168.project.ui.model.RecipeCategories.příloha;

public class AddAction extends AbstractAction {
    private final JTable recipeTable;

    public AddAction(JTable recipeTable) {
        super("Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.recipeTable = recipeTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) this.recipeTable.getModel();
        var dialog = new RecipeDialog(new Recipe("vomáčka", příloha, "00:10","4"));
        dialog.show(recipeTable, "Add recipe")
                .ifPresent(recipeTableModel::addRow);
    }
}
