package cz.muni.fi.pv168.project.ui.filters.matchers.recipeIngredient;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Objects;

public class RecipeGuidMatcher extends EntityMatcher<RecipeIngredient> {
    final private String guid;

    public RecipeGuidMatcher(String guid) {
        this.guid = guid;
    }

    @Override
    public boolean evaluate(RecipeIngredient recipeIngredient) {
        if (guid.isEmpty() || Objects.isNull(guid)) {
            return true;
        }
        return recipeIngredient.getRecipe().getGuid().equals(this.guid);
    }
}
