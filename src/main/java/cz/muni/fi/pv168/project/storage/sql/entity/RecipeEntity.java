package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Representation of Recipe entity in a SQL database.
 */
public record RecipeEntity(
        Long id,
        String guid,
        String name,
        RecipeCategory category,
        LocalTime time,
        int portions,
        String instructions) {
    public RecipeEntity(
            Long id,
            String guid,
            String name,
            RecipeCategory category,
            LocalTime time,
            int portions,
            String instructions) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        this.portions = portions;
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }
}
