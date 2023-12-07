package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.business.model.Unit;

import java.util.Objects;

/**
 * Representation of RecipeIngredient entity in a SQL database.
 */
public record RecipeIngredientEntity(Long id, String guid, String recipeGuid, String ingredientGuid, Unit unit, double amount) {
    public RecipeIngredientEntity(Long id, String guid, String recipeGuid, String ingredientGuid, Unit unit, double amount) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.recipeGuid = Objects.requireNonNull(recipeGuid, "recipeGuid must not be null");
        this.ingredientGuid = Objects.requireNonNull(ingredientGuid, "ingredientGuid must not be null");
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
        this.amount = amount;
    }
}
