package cz.muni.fi.pv168.project.business.service.import_export.batch;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Ingredient;

import java.util.Collection;

public record Batch(
        Collection<Unit> units,
        Collection<Ingredient> ingredients,
        Collection<Recipe> recipes,
        Collection<RecipeIngredient> recipeIngredients) {
}
