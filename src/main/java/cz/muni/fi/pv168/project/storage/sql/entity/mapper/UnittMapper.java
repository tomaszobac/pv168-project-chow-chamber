package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

/**
 * Mapper from the {@link UnitEntity} to {@link Unit}.
 */
public class UnittMapper implements EntityMapper<UnitEntity, Unit> {
    /**
     * Maps a UnitEntity to a Unit business object.
     *
     * @param entity the UnitEntity to be mapped
     * @return the mapped Unit business object
     */
    @Override
    public Unit mapToBusiness(UnitEntity entity) {
        return new Unit(
                entity.name(),
                entity.type(),
                entity.conversionToBase()
        );

    }

    /**
     * Maps a new Unit entity to a UnitEntity for database storage.
     *
     * @param entity The Unit entity to map.
     * @return The mapped UnitEntity.
     */
    @Override
    public UnitEntity mapNewEntityToDatabase(Unit entity) {
        return getUnitEntity(entity, null);
    }

    /**
     * Maps an existing Unit entity to a UnitEntity database entity.
     *
     * @param entity The existing Unit entity to be mapped.
     * @param dbId   The database ID of the existing entity.
     *
     * @return The mapped UnitEntity database entity.
     */
    @Override
    public UnitEntity mapExistingEntityToDatabase(Unit entity, Long dbId) {
        return getUnitEntity(entity, dbId);
    }

    /**
     * Converts a Unit object to a UnitEntity object with the provided database id.
     *
     * @param entity The Unit object to be converted.
     * @param dbId The database id to be assigned to the UnitEntity object.
     * @return The converted UnitEntity object.
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
