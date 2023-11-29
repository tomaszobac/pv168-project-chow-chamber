package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final DataAccessObject<IngredientEntity> departmentDao;
    private final EntityMapper<IngredientEntity, Ingredient> departmentMapper;

    public RecipeMapper(
            DataAccessObject<IngredientEntity> departmentDao,
            EntityMapper<IngredientEntity, Ingredient> departmentMapper) {
        this.departmentDao = departmentDao;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        var department = departmentDao
                .findById(entity.departmentId())
                .map(departmentMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Department not found, id: " +
                        entity.departmentId()));

        return new Recipe(
                entity.guid(),
                entity.firstName(),
                entity.lastName(),
                entity.gender(),
                entity.birthDate(),
                department
        );
    }

    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        var departmentEntity = departmentDao
                .findByGuid(entity.getDepartment().getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
                        entity.getDepartment().getGuid()));

        return new RecipeEntity(
                entity.getGuid(),
                departmentEntity.id(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.getBirthDate()
        );
    }

    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, Long dbId) {
        var departmentEntity = departmentDao
                .findByGuid(entity.getDepartment().getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
                        entity.getDepartment().getGuid()));

        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                departmentEntity.id(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.getBirthDate()
        );
    }
}
