package cz.muni.fi.pv168.project.storage.sql.entity;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.time.LocalTime;
import java.util.ArrayList;
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
        ArrayList<RecipeIngredient> ingredients,
        String instructions) {
    public RecipeEntity(
            Long id,
            String guid,
            String name,
            RecipeCategory category,
            LocalTime time,
            int portions,
            ArrayList<RecipeIngredient> ingredients,
            String instructions) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        this.portions = portions;
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }

    public RecipeEntity(
            String guid,
            String name,
            RecipeCategory category,
            LocalTime time,
            int portions,
            ArrayList<RecipeIngredient> ingredients,
            String instructions) {
        this(null, guid, name, category, time, portions, ingredients, instructions);
    }
}
