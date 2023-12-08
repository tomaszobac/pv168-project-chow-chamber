package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Ingredient} entity using SQL database.
 */
public class IngredientSqlRepository implements Repository<Ingredient> {

    private final DataAccessObject<IngredientEntity> ingredientDao;
    private final EntityMapper<IngredientEntity, Ingredient> ingredientMapper;

    /**
     * Creates a new instance of IngredientSqlRepository with the given DataAccessObject and EntityMapper.
     *
     * @param ingredientDao The DataAccessObject used to access the IngredientEntity data.
     * @param ingredientMapper The EntityMapper used to map IngredientEntity objects to Ingredient objects.
     */
    public IngredientSqlRepository(
            DataAccessObject<IngredientEntity> ingredientDao,
            EntityMapper<IngredientEntity, Ingredient> ingredientMapper) {
        this.ingredientDao = ingredientDao;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * Returns a list of all Ingredients.
     *
     * @return a List of Ingredient objects.
     */
    @Override
    public List<Ingredient> findAll() {
        return ingredientDao
                .findAll()
                .stream()
                .map(ingredientMapper::mapToBusiness)
                .toList();
    }

    /**
     * Creates a new Ingredient entity in the database.
     *
     * @param newEntity the Ingredient object to be created
     */
    @Override
    public void create(Ingredient newEntity) {
        ingredientDao.create(ingredientMapper.mapNewEntityToDatabase(newEntity));
    }

    /**
     * Updates an existing Ingredient in the data storage.
     *
     * @param entity The Ingredient that needs to be updated.
     * @throws DataStorageException if the Ingredient with the specified GUID is not found in the data storage.
     */
    @Override
    public void update(Ingredient entity) {
        var existingIngredient = ingredientDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " + entity.getGuid()));
        var updatedIngredient = ingredientMapper.mapExistingEntityToDatabase(entity, existingIngredient.id());

        ingredientDao.update(updatedIngredient);
    }

    /**
     * Deletes an ingredient by its GUID.
     *
     * @param guid The GUID of the ingredient to be deleted.
     */
    @Override
    public void deleteByGuid(String guid) {
        ingredientDao.deleteByGuid(guid);
    }

    /**
     * Deletes all ingredients from the database.
     */
    @Override
    public void deleteAll() {
        ingredientDao.deleteAll();
    }

    /**
     * Check if an ingredient exists by its GUID.
     *
     * @param guid the GUID of the ingredient to be checked
     * @return true if the ingredient exists, false otherwise
     */
    @Override
    public boolean existsByGuid(String guid) {
        return ingredientDao.existsByGuid(guid);
    }

    /**
     * Finds an {@link Ingredient} by its GUID.
     *
     * @param guid the GUID of the ingredient to find
     * @return an {@link Optional} containing the found ingredient, or an empty optional if no ingredient is found
     */
    @Override
    public Optional<Ingredient> findByGuid(String guid) {
        return ingredientDao
                .findByGuid(guid)
                .map(ingredientMapper::mapToBusiness);
    }
}
