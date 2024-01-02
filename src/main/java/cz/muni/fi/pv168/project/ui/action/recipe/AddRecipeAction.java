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
        Recipe recipe = createPrefilledRecipe();
        recipeTableModel.addRowNoRefresh(recipe);
        var dialog = new RecipeDialog(recipe, ingredientTable, unitTable, recipeIngredientsTable, filter);
        dialog.show(recipeTable, "Add recipe");
        if (!dialog.getReturnedOK()) {
            clearAddedRecipeIngredients(recipe);
            deleteNewRecipe(recipeTableModel, recipe);
        } else {
            recipeTableModel.fireTableDataChanged();
            recipeTableModel.updateRow(recipe);
        }
        recipeTableModel.fireTableDataChanged();
    }

    private void clearAddedRecipeIngredients(Recipe recipe) {
        var model = (RecipeIngredientsTableModel) recipeIngredientsTable.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            RecipeIngredient recipeIngredient = (RecipeIngredient) model.getValueAt(i, 0);
            if (recipeIngredient.getRecipe().equals(recipe)) {
                model.deleteRow(i);
            }
        }
    }

    private void deleteNewRecipe(RecipeTableModel model, Recipe recipe) {
        for (int i = 0; i < model.getRowCount(); i++) {
            Recipe checking = (Recipe) model.getValueAt(i, 0);
            if (checking.getGuid().equals(recipe.getGuid())) {
                model.fireTableDataChanged();
                model.deleteRow(i);
                return;
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