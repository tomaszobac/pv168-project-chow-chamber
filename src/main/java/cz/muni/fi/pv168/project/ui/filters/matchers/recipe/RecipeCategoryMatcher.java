package cz.muni.fi.pv168.project.ui.filters.matchers.recipe;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

public class RecipeCategoryMatcher extends EntityMatcher<Recipe> {
    private final RecipeCategory category;

    public RecipeCategoryMatcher(RecipeCategory category) {
        this.category = category;
    }

    @Override
    public boolean evaluate(Recipe recipe) {
        return recipe.getCategory() == category;
    }
}
