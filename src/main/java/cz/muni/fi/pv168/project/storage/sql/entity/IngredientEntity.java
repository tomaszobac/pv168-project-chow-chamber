package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.business.model.Unit;

import java.util.Objects;

/**
 * Representation of Ingredient entity in a SQL database.
 */
public record IngredientEntity(Long id, String guid, String name, double calories, Unit unit) {
    public IngredientEntity(Long id, String guid, String name, double calories, Unit unit) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.calories = calories;
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }
}
