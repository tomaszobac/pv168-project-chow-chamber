package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link RecipeIngredient} entity using SQL database.
 * <p>
 * Original by:
 * @author Vojtech Sassmann
 */
public class RecipeIngredientSqlRepository implements Repository<RecipeIngredient> {

    private final DataAccessObject<RecipeIngredientEntity> recipeIngredientDao;
    private final EntityMapper<RecipeIngredientEntity, RecipeIngredient> recipeIngredientMapper;

    /**
     * Creates a new instance of RecipeIngredientSqlRepository with the given DataAccessObject and EntityMapper.
     *
     * @param recipeIngredientDao The DataAccessObject used to access the RecipeIngredientEntity data.
     * @param recipeIngredientMapper The EntityMapper used to map RecipeIngredientEntity objects to RecipeIngredient objects.
     */
    public RecipeIngredientSqlRepository(
            DataAccessObject<RecipeIngredientEntity> recipeIngredientDao,
            EntityMapper<RecipeIngredientEntity, RecipeIngredient> recipeIngredientMapper) {
        this.recipeIngredientDao = recipeIngredientDao;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    /**
     * Returns a list of all RecipeIngredients.
     *
     * @return a List of RecipeIngredient objects.
     */
    @Override
    public List<RecipeIngredient> findAll() {
        return recipeIngredientDao
                .findAll()
                .stream()
                .map(recipeIngredientMapper::mapToBusiness)
                .toList();
    }

    /**
     * Creates a new RecipeIngredient entity in the database.
     *
     * @param newEntity the RecipeIngredient object to be created
     */
    @Override
    public void create(RecipeIngredient newEntity) {
        recipeIngredientDao.create(recipeIngredientMapper.mapNewEntityToDatabase(newEntity));
    }

    /**
     * Updates an existing RecipeIngredient in the data storage.
     *
     * @param entity The RecipeIngredient that needs to be updated.
     * @throws DataStorageException if the RecipeIngredient with the specified GUID is not found in the data storage.
     */
    @Override
    public void update(RecipeIngredient entity) {
        var existingRecipeIngredient = recipeIngredientDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("RecipeIngredient not found, guid: " + entity.getGuid()));
        var updatedRecipeIngredient = recipeIngredientMapper.mapExistingEntityToDatabase(entity, existingRecipeIngredient.id());

        recipeIngredientDao.update(updatedRecipeIngredient);
    }

    /**
     * Deletes an recipeIngredient by its GUID.
     *
     * @param guid The GUID of the recipeIngredient to be deleted.
     */
    @Override
    public void deleteByGuid(String guid) {
        recipeIngredientDao.deleteByGuid(guid);
    }

    /**
     * Deletes all recipeIngredients from the database.
     */
    @Override
    public void deleteAll() {
        recipeIngredientDao.deleteAll();
    }

    /**
     * Check if an recipeIngredient exists by its GUID.
     *
     * @param guid the GUID of the recipeIngredient to be checked
     * @return true if the recipeIngredient exists, false otherwise
     */
    @Override
    public boolean existsByGuid(String guid) {
        return recipeIngredientDao.existsByGuid(guid);
    }

    /**
     * Finds an {@link RecipeIngredient} by its GUID.
     *
     * @param guid the GUID of the recipeIngredient to find
     * @return an {@link Optional} containing the found recipeIngredient, or an empty optional if no recipeIngredient is found
     */
    @Override
    public Optional<RecipeIngredient> findByGuid(String guid) {
        return recipeIngredientDao
                .findByGuid(guid)
                .map(recipeIngredientMapper::mapToBusiness);
    }
}
