package cz.muni.fi.pv168.project.ui.filters.matchers.recipe;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.business.model.Recipe;

import java.time.LocalTime;

public class RecipeLocalTimeMatcher extends EntityMatcher<Recipe> {
    private final LocalTime timeFrom;
    private final LocalTime timeTo;

    public RecipeLocalTimeMatcher(LocalTime from, LocalTime to) {
        this.timeFrom = from;
        this.timeTo = to;
    }

    @Override
    public boolean evaluate(Recipe recipe) {
        return this.timeFrom.compareTo(recipe.getTime()) <= 0 && this.timeTo.compareTo(recipe.getTime()) >= 0;
    }
}
