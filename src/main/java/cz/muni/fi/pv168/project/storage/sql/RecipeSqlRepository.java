package cz.muni.fi.pv168.project.storage.sql;

import java.util.List;
import java.util.Optional;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

/**
 * Implementation of {@link Repository} for {@link Recipe} entity using SQL database.
 * <p>
 * Original by:
 * @author Vojtech Sassmann
 */
public class RecipeSqlRepository implements Repository<Recipe> {

    private final DataAccessObject<RecipeEntity> recipeDao;
    private final EntityMapper<RecipeEntity, Recipe> recipeMapper;

    /**
     * Creates a new instance of RecipeSqlRepository.
     *
     * @param recipeDao The data access object for RecipeEntity.
     * @param recipeMapper The entity mapper to map RecipeEntity to Recipe.
     */
    public RecipeSqlRepository(
            DataAccessObject<RecipeEntity> recipeDao,
            EntityMapper<RecipeEntity, Recipe> recipeMapper) {
        this.recipeDao = recipeDao;
        this.recipeMapper = recipeMapper;
    }


    /**
     * Returns a list of all recipes.
     *
     * @return a list of all recipes
     */
    @Override
    public List<Recipe> findAll() {
        return recipeDao
                .findAll()
                .stream()
                .map(recipeMapper::mapToBusiness)
                .toList();
    }

    /**
     * Creates a new recipe in the database.
     *
     * @param newEntity The recipe to create.
     */
    @Override
    public void create(Recipe newEntity) {
        recipeDao.create(recipeMapper.mapNewEntityToDatabase(newEntity));
    }

    /**
     * Updates an existing Recipe entity.
     *
     * @param entity The Recipe entity to be updated.
     * @throws DataStorageException If the Recipe entity with the specified GUID is not found.
     */
    @Override
    public void update(Recipe entity) {
        var existingRecipe = recipeDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, guid: " + entity.getGuid()));
        var updatedRecipeEntity = recipeMapper
                .mapExistingEntityToDatabase(entity, existingRecipe.id());

        recipeDao.update(updatedRecipeEntity);
    }

    /**
     * Deletes a recipe based on its GUID.
     *
     * @param guid The GUID of the recipe to be deleted.
     */
    @Override
    public void deleteByGuid(String guid) {
        recipeDao.deleteByGuid(guid);
    }

    /**
     * Deletes all recipes from the database.
     */
    @Override
    public void deleteAll() {
        recipeDao.deleteAll();
    }

    /**
     * Checks if a recipe exists in the database based on its GUID.
     *
     * @param guid The GUID of the recipe to check.
     * @return true if a recipe with the given GUID exists, false otherwise.
     */
    @Override
    public boolean existsByGuid(String guid) {
        return recipeDao.existsByGuid(guid);
    }

    /**
     * Finds a recipe by its unique GUID.
     *
     * @param guid the GUID of the recipe to find
     * @return an Optional containing the found Recipe, or empty if no recipe is found
     */
    @Override
    public Optional<Recipe> findByGuid(String guid) {
       return recipeDao
            .findByGuid(guid)
            .map(recipeMapper::mapToBusiness);
    }
}
