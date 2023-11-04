package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.ArrayList;

import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories.PRILOHA;

public class AddRecipeAction extends AbstractAction {
    private final JTable recipeTable;
    private final JTable ingredientTable;
    private final JTable unitTable;

    public AddRecipeAction(JTable recipeTable, JTable ingredientTable, JTable unitTable) {
        super("Add", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.recipeTable = recipeTable;
        this.ingredientTable = ingredientTable;
        this.unitTable = unitTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) this.recipeTable.getModel();
        var dialog = new RecipeDialog(new Recipe("vomáčka", PRILOHA, LocalTime.parse("00:10"),4, new ArrayList<>(), "The missile knows where it is at all times. It knows this because it knows where it isn't."), ingredientTable, unitTable);
        dialog.show(recipeTable, "Add recipe")
                .ifPresent(recipeTableModel::addRow);
    }
}
