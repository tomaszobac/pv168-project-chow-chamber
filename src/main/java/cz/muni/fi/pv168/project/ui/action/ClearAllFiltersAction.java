package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

public class ClearAllFiltersAction extends AbstractAction {
    private final RecipeTableFilter recipeFilter;
    private final Action clearFilterRecipe;
    private final IngredientTableFilter ingredientFilter;
    private final Action clearFilterIngredient;
    private final UnitTableFilter unitFilter;
    private final Action clearFilterUnit;

    public ClearAllFiltersAction(RecipeTableFilter recipeFilter,
                                 IngredientTableFilter ingredientFilter,
                                 UnitTableFilter unitFilter,
                                 Action clearFilterRecipe,
                                 Action clearFilterIngredient,
                                 Action clearFilterUnit) {
        super("Clear filters", null);
        putValue(SHORT_DESCRIPTION, "Clears all filters");

        this.recipeFilter = recipeFilter;
        this.clearFilterRecipe = clearFilterRecipe;
        this.ingredientFilter = ingredientFilter;
        this.clearFilterIngredient = clearFilterIngredient;
        this.unitFilter = unitFilter;
        this.clearFilterUnit = clearFilterUnit;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        recipeFilter.resetFilter();
        ingredientFilter.resetFilter();
        unitFilter.resetFilter();
        clearFilterRecipe.setEnabled(false);
        clearFilterIngredient.setEnabled(false);
        clearFilterUnit.setEnabled(false);
    }
}
