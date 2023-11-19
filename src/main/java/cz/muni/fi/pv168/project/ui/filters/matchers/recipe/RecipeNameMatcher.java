package cz.muni.fi.pv168.project.ui.filters.matchers.recipe;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;

public class RecipeNameMatcher extends EntityMatcher<Recipe> {
    String name;

    public RecipeNameMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(Recipe recipe) {
        if (name.isEmpty()) {
            return true;
        }
        return recipe.getName().toLowerCase().contains(this.name.toLowerCase());
    }
}
