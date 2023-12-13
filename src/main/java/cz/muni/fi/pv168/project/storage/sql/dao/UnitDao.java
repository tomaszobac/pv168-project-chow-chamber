package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link UnitEntity} entity.
 */
public final class UnitDao implements DataAccessObject<UnitEntity> {

    private final Supplier<ConnectionHandler> connections;

    /**
     * Constructs a new UnitDao with the given Supplier of ConnectionHandlers.
     *
     * @param connections the Supplier of ConnectionHandlers used to obtain database connections
     */
    public UnitDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    /**
     * Creates a new UnitEntity and stores it in the database.
     *
     * @param newUnit the UnitEntity to be created and stored
     * @return the created UnitEntity
     * @throws DataStorageException if there is an error while storing the newUnit
     */
    @Override
    public UnitEntity create(UnitEntity newUnit) {
        var sql = "INSERT INTO Unit (guid, name, type, conversionToBase) VALUES (?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newUnit.guid());
            statement.setString(2, newUnit.name());
            statement.setString(3, newUnit.type().toString());
            statement.setDouble(4, newUnit.conversionToBase());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long unitId;

                if (keyResultSet.next()) {
                    unitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newUnit);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newUnit);
                }

                return findById(unitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newUnit, ex);
        }
    }

    /**
     * Retrieves all units from the database.
     *
     * @return a collection of {@link UnitEntity} objects representing the retrieved units.
     * @throws DataStorageException if an error occurs while retrieving the units from the database.
     */
    @Override
    public Collection<UnitEntity> findAll() {
        var sql = """
                SELECT id, guid, name, type, conversionToBase
                FROM Unit
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<UnitEntity> units = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = unitFromResultSet(resultSet);
                    units.add(unit);
                }
            }

            return units;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    /**
     * Retrieves a UnitEntity with the specified id.
     *
     * @param id the id of the UnitEntity to find
     * @return an Optional containing the UnitEntity if found, otherwise an empty Optional
     * @throws DataStorageException if there is an error in the data storage
     */
    @Override
    public Optional<UnitEntity> findById(long id) {
        var sql = """
                SELECT id, guid, name, type, conversionToBase
                FROM Unit
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id: " + id, ex);
        }
    }

    /**
     * Retrieves a unit entity from the database based on the given GUID.
     *
     * @param guid the GUID of the unit to retrieve
     * @return an Optional containing the unit entity if found, or an empty Optional if not found
     * @throws DataStorageException if there is an error accessing the database
     */
    @Override
    public Optional<UnitEntity> findByGuid(String guid) {
        var sql = """
                SELECT id, guid, name, type, conversionToBase
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {

                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by guid: " + guid, ex);
        }
    }

    /**
     * Updates a UnitEntity in the database.
     *
     * @param entity The UnitEntity to be updated.
     * @return The updated UnitEntity.
     * @throws DataStorageException If an error occurs while updating the unit.
     */
    @Override
    public UnitEntity update(UnitEntity entity) {
        var sql = """
                UPDATE Unit
                SET guid = ?,
                    name = ?,
                    type = ?,
                    conversionToBase = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.name());
            statement.setString(3, entity.type().toString());
            statement.setDouble(4, entity.conversionToBase());
            statement.setLong(5, entity.id());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 unit (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update unit: " + entity, ex);
        }
    }

    /**
     * Deletes a unit from the database based on its GUID.
     *
     * @param guid the GUID of the unit to delete
     * @throws DataStorageException if the unit is not found or if multiple units are deleted
     */
    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 unit (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "Deletion includes units which are still being used");
            throw new DataStorageException("Failed to delete unit, guid: " + guid, ex);
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete unit, guid: " + guid, ex);
        }
    }

    /**
     * Deletes all units from the "Unit" table in the database.
     *
     * @throws DataStorageException if an error occurs while deleting the units
     */
    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Unit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all units", ex);
        }
    }

    /**
     * Checks if a unit with the given GUID exists in the data storage.
     *
     * @param guid the GUID of the unit to be checked
     * @return true if a unit with the given GUID exists, false otherwise
     * @throws DataStorageException if there is an error while checking for the unit
     */
    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if unit exists, guid: " + guid, ex);
        }
    }

    /**
     * Create a UnitEntity object from the given ResultSet.
     *
     * @param resultSet The ResultSet containing the unit data.
     * @return The UnitEntity object created from the ResultSet.
     * @throws SQLException If an error occurs while reading the ResultSet.
     */
    private static UnitEntity unitFromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("name"),
                UnitType.valueOf(resultSet.getString("type")),
                resultSet.getDouble("conversionToBase")
        );
    }
}
