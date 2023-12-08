package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {
    /**
     * Maps a RecipeEntity to a Recipe business object.
     *
     * @param entity the RecipeEntity to be mapped
     * @return the mapped Recipe business object
     */
    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        return new Recipe(
                entity.guid(),
                entity.name(),
                entity.category(),
                entity.time(),
                entity.portions(),
                entity.instructions()
        );

    }

    /**
     * Maps a new Recipe entity to a RecipeEntity for database storage.
     *
     * @param entity The Recipe entity to map.
     * @return The mapped RecipeEntity.
     */
    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        return getRecipeEntity(entity, null);
    }

    /**
     * Maps an existing Recipe entity to a RecipeEntity database entity.
     *
     * @param entity The existing Recipe entity to be mapped.
     * @param dbId   The database ID of the existing entity.
     *
     * @return The mapped RecipeEntity database entity.
     */
    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, Long dbId) {
        return getRecipeEntity(entity, dbId);
    }

    /**
     * Converts a Recipe object to a RecipeEntity object with the provided database id.
     *
     * @param entity The Recipe object to be converted.
     * @param dbId The database id to be assigned to the RecipeEntity object.
     * @return The converted RecipeEntity object.
     */
    private static RecipeEntity getRecipeEntity(Recipe entity, Long dbId) {
        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getCategory(),
                entity.getTime(),
                entity.getPortions(),
                entity.getInstructions()
        );
    }
}
