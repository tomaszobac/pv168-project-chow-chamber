package cz.muni.fi.pv168.project.ui.filters.matchers.recipeIngredient;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

public class RecipeGuidMatcher extends EntityMatcher<RecipeIngredient> {
    String guid;

    public RecipeGuidMatcher(String guid) {
        this.guid = guid;
    }

    @Override
    public boolean evaluate(RecipeIngredient recipeIngredient) {
        if (guid.isEmpty()) {
            return true;
        }
        return recipeIngredient.getRecipeGuid().equals(this.guid);
    }
}
