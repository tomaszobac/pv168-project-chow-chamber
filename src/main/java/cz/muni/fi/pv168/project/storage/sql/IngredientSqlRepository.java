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
 *
 * @author Vojtech Sassmann
 */
public class IngredientSqlRepository implements Repository<Ingredient> {

    private final DataAccessObject<IngredientEntity> departmentDao;
    private final EntityMapper<IngredientEntity, Ingredient> departmentMapper;

    public IngredientSqlRepository(
            DataAccessObject<IngredientEntity> departmentDao,
            EntityMapper<IngredientEntity, Ingredient> departmentMapper) {
        this.departmentDao = departmentDao;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Ingredient> findAll() {
        return departmentDao
                .findAll()
                .stream()
                .map(departmentMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Ingredient newEntity) {
        departmentDao.create(departmentMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Ingredient entity) {
        var existingDepartment = departmentDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " + entity.getGuid()));
        var updatedDepartment = departmentMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        departmentDao.update(updatedDepartment);
    }

    @Override
    public void deleteByGuid(String guid) {
        departmentDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        departmentDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return departmentDao.existsByGuid(guid);
    }

    @Override
    public Optional<Ingredient> findByGuid(String guid) {
        return departmentDao
                .findByGuid(guid)
                .map(departmentMapper::mapToBusiness);
    }
}
