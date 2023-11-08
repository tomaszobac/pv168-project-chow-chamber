package cz.muni.fi.pv168.project.ui.filters.matchers.recipe;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;

public class RecipePortionsMatcher extends EntityMatcher<Recipe> {
    private final Integer portionsFrom;
    private final Integer portionsTo;

    public RecipePortionsMatcher(Integer from, Integer to) {
        this.portionsFrom = from;
        this.portionsTo = to;
    }

    @Override
    public boolean evaluate(Recipe recipe) {
        return recipe.getPortions() >= this.portionsFrom && recipe.getPortions() <= this.portionsTo;
    }
}
