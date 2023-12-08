package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Unit} entity using SQL database.
 * <p>
 * Original by:
 * @author Vojtech Sassmann //TODO: Delete this xD
 */
public class UnitSqlRepository implements Repository<Unit> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    /**
     * Constructs a new UnitSqlRepository with the given DataAccessObject and EntityMapper.
     *
     * @param unitDao The DataAccessObject used to access the UnitEntity data.
     * @param unitMapper The EntityMapper used to map UnitEntity objects to Unit objects.
     */
    public UnitSqlRepository(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, Unit> unitMapper) {
        this.unitDao = unitDao;
        this.unitMapper = unitMapper;
    }

    /**
     * Returns a list of all units.
     *
     * @return a list of Unit objects representing all the units available in the repository.
     */
    @Override
    public List<Unit> findAll() {
        return unitDao
                .findAll()
                .stream()
                .map(unitMapper::mapToBusiness)
                .toList();
    }

    /**
     * Creates a new Unit entity.
     *
     * @param newEntity the Unit entity to be created.
     */
    @Override
    public void create(Unit newEntity) {
        unitDao.create(unitMapper.mapNewEntityToDatabase(newEntity));
    }

    /**
     * Updates an existing Unit entity in the database.
     *
     * @param entity The Unit entity to be updated.
     * @throws DataStorageException if the specified Unit entity does not exist in the database.
     */
    @Override
    public void update(Unit entity) {
        var existingUnit = unitDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Unit not found, guid: " + entity.getGuid()));
        var updatedUnit = unitMapper.mapExistingEntityToDatabase(entity, existingUnit.id());

        unitDao.update(updatedUnit);
    }

    /**
     * Deletes a unit by its GUID.
     *
     * @param guid the GUID of the unit to be deleted
     */
    @Override
    public void deleteByGuid(String guid) {
        unitDao.deleteByGuid(guid);
    }

    /**
     * Deletes all units from the database.
     */
    @Override
    public void deleteAll() {
        unitDao.deleteAll();
    }

    /**
     * Checks if a record exists in the database with the given GUID.
     *
     * @param guid The GUID of the record to check.
     * @return {@code true} if a record with the given GUID exists, {@code false} otherwise.
     */
    @Override
    public boolean existsByGuid(String guid) {
        return unitDao.existsByGuid(guid);
    }

    /**
     * Finds a Unit by its GUID.
     *
     * @param guid The GUID of the Unit to find.
     * @return An Optional containing the Unit if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Unit> findByGuid(String guid) {
        return unitDao
                .findByGuid(guid)
                .map(unitMapper::mapToBusiness);
    }
}
