package cz.muni.fi.pv168.project.ui.filters.matchers.ingredient;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;

public class IngredientNameMatcher extends EntityMatcher<Ingredient> {
    String name;

    public IngredientNameMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate(Ingredient ingredient) {
        if (name.equals("")) {
            return true;
        }
        return ingredient.getName().toLowerCase().contains(this.name.toLowerCase());
    }
}
