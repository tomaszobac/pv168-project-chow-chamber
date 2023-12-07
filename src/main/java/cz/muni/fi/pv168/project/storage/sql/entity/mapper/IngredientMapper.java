package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;

/**
 * Mapper from the {@link IngredientEntity} to {@link Ingredient}.
 */
public final class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    /**
     * Maps an IngredientEntity object to a Business object of type Ingredient.
     *
     * @param entity the IngredientEntity object to be mapped
     * @return a new Ingredient object with attributes mapped from the IngredientEntity object
     */
    @Override
    public Ingredient mapToBusiness(IngredientEntity entity) {
        return new Ingredient(
                entity.guid(),
                entity.name(),
                entity.calories(),
                entity.unit()
        );
    }

    /**
     * Maps a new Ingredient entity to the corresponding IngredientEntity for database storage.
     *
     * @param entity The Ingredient entity to be mapped.
     * @return The corresponding IngredientEntity for database storage.
     */
    @Override
    public IngredientEntity mapNewEntityToDatabase(Ingredient entity) {
        return getIngredientEntity(entity, null);
    }

    /**
     * Maps an existing Ingredient object to its corresponding IngredientEntity in the database.
     *
     * @param entity The existing Ingredient object to be mapped.
     * @param dbId   The id of the existing database record. If null, a new record will be created.
     * @return The corresponding IngredientEntity object in the database.
     */
    @Override
    public IngredientEntity mapExistingEntityToDatabase(Ingredient entity, Long dbId) {
        return getIngredientEntity(entity, dbId);
    }

    /**
     * Returns an instance of IngredientEntity based on the provided Ingredient and dbId.
     *
     * @param entity The Ingredient object to be mapped to IngredientEntity.
     * @param dbId   The id of the existing IngredientEntity.
     * @return The mapped IngredientEntity instance.
     */
    private static IngredientEntity getIngredientEntity(Ingredient entity, Long dbId) {
        return new IngredientEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getCalories(),
                entity.getUnit()
        );
    }
}
