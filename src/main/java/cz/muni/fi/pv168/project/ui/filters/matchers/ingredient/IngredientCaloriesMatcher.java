package cz.muni.fi.pv168.project.ui.filters.matchers.ingredient;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.business.model.Ingredient;

public class IngredientCaloriesMatcher extends EntityMatcher<Ingredient> {
    private final Double caloriesFrom;
    private final Double caloriesTo;

    public IngredientCaloriesMatcher(Double from, Double to) {
        this.caloriesFrom = from;
        this.caloriesTo = to;
    }

    @Override
    public boolean evaluate(Ingredient ingredient) {
        return ingredient.getCalories() >= this.caloriesFrom && ingredient.getCalories() <= this.caloriesTo;
    }
}
