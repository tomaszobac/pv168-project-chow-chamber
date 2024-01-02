package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;

import java.util.Objects;

/**
 * Representation of RecipeIngredient entity in a SQL database.
 */
public record RecipeIngredientEntity(Long id, String guid, Recipe recipe, Ingredient ingredient, Unit unit, double amount) {
    public RecipeIngredientEntity(Long id, String guid, Recipe recipe, Ingredient ingredient, Unit unit, double amount) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.recipe = Objects.requireNonNull(recipe, "recipe must not be null");
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient must not be null");
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
        this.amount = amount;
    }
}
