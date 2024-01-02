package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;

/**
 * Mapper from the {@link RecipeIngredientEntity} to {@link RecipeIngredient}.
 */
public final class RecipeIngredientMapper implements EntityMapper<RecipeIngredientEntity, RecipeIngredient> {

    /**
     * Maps an RecipeIngredientEntity object to a Business object of type RecipeIngredient.
     *
     * @param entity the RecipeIngredientEntity object to be mapped
     * @return a new RecipeIngredient object with attributes mapped from the RecipeIngredientEntity object
     */
    @Override
    public RecipeIngredient mapToBusiness(RecipeIngredientEntity entity) {
        return new RecipeIngredient(
                entity.guid(),
                entity.recipe(),
                entity.ingredient(),
                entity.unit(),
                entity.amount()
        );
    }

    /**
     * Maps a new RecipeIngredient entity to the corresponding RecipeIngredientEntity for database storage.
     *
     * @param entity The RecipeIngredient entity to be mapped.
     * @return The corresponding RecipeIngredientEntity for database storage.
     */
    @Override
    public RecipeIngredientEntity mapNewEntityToDatabase(RecipeIngredient entity) {
        return getRecipeIngredientEntity(entity, null);
    }

    /**
     * Maps an existing RecipeIngredient object to its corresponding RecipeIngredientEntity in the database.
     *
     * @param entity The existing RecipeIngredient object to be mapped.
     * @param dbId   The id of the existing database record. If null, a new record will be created.
     * @return The corresponding RecipeIngredientEntity object in the database.
     */
    @Override
    public RecipeIngredientEntity mapExistingEntityToDatabase(RecipeIngredient entity, Long dbId) {
        return getRecipeIngredientEntity(entity, dbId);
    }

    /**
     * Returns an instance of RecipeIngredientEntity based on the provided RecipeIngredient and dbId.
     *
     * @param entity The RecipeIngredient object to be mapped to RecipeIngredientEntity.
     * @param dbId   The id of the existing RecipeIngredientEntity.
     * @return The mapped RecipeIngredientEntity instance.
     */
    private static RecipeIngredientEntity getRecipeIngredientEntity(RecipeIngredient entity, Long dbId) {
        return new RecipeIngredientEntity(
                dbId,
                entity.getGuid(),
                entity.getRecipe(),
                entity.getIngredient(),
                entity.getUnit(),
                entity.getAmount()
        );
    }
}
