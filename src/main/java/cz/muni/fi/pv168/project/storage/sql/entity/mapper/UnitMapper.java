package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

/**
 * Mapper from the {@link UnitEntity} to {@link Unit}.
 */
public final class UnitMapper implements EntityMapper<UnitEntity, Unit> {
    /**
     * Maps a UnitEntity object to a Unit object in the business logic layer.
     * <p>
     * The method takes a UnitEntity object and creates a new Unit object using
     * the guid, name, type, and conversionToBase properties of the UnitEntity object.
     *
     * @param entity the UnitEntity object to map to a Unit object
     * @return the mapped Unit object
     */
    @Override
    public Unit mapToBusiness(UnitEntity entity) {
        return new Unit(
                entity.guid(),
                entity.name(),
                entity.type(),
                entity.conversionToBase()
        );
    }

    /**
     * Maps a Unit object to its corresponding UnitEntity object to be saved in the database.
     *
     * @param entity The Unit object to be mapped.
     * @return The corresponding UnitEntity object.
     */
    @Override
    public UnitEntity mapNewEntityToDatabase(Unit entity) {
        return getUnitEntity(entity, null);
    }

    /**
     * Maps an existing Unit entity to a database UnitEntity.
     *
     * @param entity The existing Unit entity to be mapped.
     * @param dbId   The ID of the existing UnitEntity in the database.
     * @return The mapped UnitEntity representing the existing entity in the database.
     */
    @Override
    public UnitEntity mapExistingEntityToDatabase(Unit entity, Long dbId) {
        return getUnitEntity(entity, dbId);
    }

    /**
     * Retrieves the UnitEntity by mapping the provided Unit object to a UnitEntity.
     *
     * @param entity The Unit object to be mapped to a UnitEntity.
     * @param dbId   The ID of the database for the UnitEntity.
     * @return The UnitEntity mapped from the Unit object.
     */
    private static UnitEntity getUnitEntity(Unit entity, Long dbId) {
        return new UnitEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getType(),
                entity.getConversionToBase()
        );
    }
}
