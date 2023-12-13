package cz.muni.fi.pv168.project.ui.action.recipe;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.dialog.RecipeDialog;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;

import static cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory.PRILOHA;

public class AddRecipeAction extends AbstractAction {
    private final JTable recipeTable;
    private final JTable ingredientTable;
    private final JTable unitTable;
    private final JTable recipeIngredientsTable;
    private final RecipeIngredientTableFilter filter;

    public AddRecipeAction(JTable recipeTable, JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        super("Add Recipe", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
        this.recipeTable = recipeTable;
        this.ingredientTable = ingredientTable;
        this.unitTable = unitTable;
        this.recipeIngredientsTable = recipeIngredientsTable;
        this.filter = filter;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) this.recipeTable.getModel();
        var dialog = new RecipeDialog(createPrefilledRecipe(), ingredientTable, unitTable, recipeIngredientsTable, filter);
        dialog.show(recipeTable, "Add recipe")
                .ifPresent(recipeTableModel::addRow);
        if (!dialog.getReturnedOK()) {
            clearAddedRecipeIngredients(recipeIngredientsTable, dialog.getRecipeGuid());
        }
    }

    private void clearAddedRecipeIngredients(JTable recipeIngredientsTable, String guid) {
        for (int i = recipeIngredientsTable.getRowCount() - 1; i >= 0; i--) {
            RecipeIngredient recipeIngredient = (RecipeIngredient) recipeIngredientsTable.getModel().getValueAt(i, 0);
            if (recipeIngredient.getRecipeGuid().equals(guid)) {
                ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).deleteRow(i);
            }
        }
    }

    private Recipe createPrefilledRecipe() {
        Recipe recipe = new Recipe(
                "Vomáčka",
                "",
                PRILOHA,
                LocalTime.parse("00:10"),
                4);

        recipe.setGuid(((RecipeTableModel) recipeTable.getModel()).getNewGuid());
        return recipe;
    }
}