package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeIngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link RecipeIngredientEntity} entity.
 */
public final class RecipeIngredientDao implements DataAccessObject<RecipeIngredientEntity> {

    private final Supplier<ConnectionHandler> connections;

    /**
     * Constructs a new RecipeIngredientDao object with the given Supplier of ConnectionHandler.
     *
     * @param connections the Supplier of ConnectionHandler to be used by the RecipeIngredientDao
     */
    public RecipeIngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    /**
     * Creates a new recipeIngredient in the data storage.
     *
     * @param newRecipeIngredient the new recipeIngredient to create in the data storage
     * @return the created recipeIngredient entity
     * @throws DataStorageException if an error occurs while storing the recipeIngredient
     */
    @Override
    public RecipeIngredientEntity create(RecipeIngredientEntity newRecipeIngredient) {
        var sql = "INSERT INTO RecipeIngredient (guid, recipeGuid, ingredientGuid, unit, amount) VALUES (?, ?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRecipeIngredient.guid());
            statement.setString(2, newRecipeIngredient.recipeGuid());
            statement.setString(3, newRecipeIngredient.ingredientGuid());
            statement.setObject(4, newRecipeIngredient.unit());
            statement.setDouble(5, newRecipeIngredient.amount());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long recipeIngredientId;

                if (keyResultSet.next()) {
                    recipeIngredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRecipeIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRecipeIngredient);
                }

                return findById(recipeIngredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRecipeIngredient, ex);
        }
    }

    /**
     * Retrieves all recipeIngredients from the database.
     *
     * @return a collection of RecipeIngredientEntity objects representing all the recipeIngredients stored in the database
     * @throws DataStorageException if there is an error retrieving the recipeIngredients from the database
     */
    @Override
    public Collection<RecipeIngredientEntity> findAll() {
        var sql = """
                SELECT id, guid, recipeGuid, ingredientGuid, unit, amount
                FROM RecipeIngredient
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipeIngredient = recipeIngredientFromResultSet(resultSet);
                    recipeIngredients.add(recipeIngredient);
                }
            }

            return recipeIngredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipeIngredients", ex);
        }
    }

    /**
     * Retrieves an recipeIngredient from the database based on the provided ID.
     *
     * @param id the ID of the recipeIngredient to retrieve
     * @return an Optional containing the RecipeIngredientEntity if found, or an empty Optional if not found
     * @throws DataStorageException if there is an error retrieving the recipeIngredient
     */
    @Override
    public Optional<RecipeIngredientEntity> findById(long id) {
        var sql = """
                SELECT id, guid, recipeGuid, ingredientGuid, unit, amount
                FROM RecipeIngredient
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeIngredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipeIngredient by id: " + id, ex);
        }
    }

    /**
     * Finds an recipeIngredient entity by its GUID.
     *
     * @param guid the GUID of the recipeIngredient to find
     * @return an Optional containing the recipeIngredient entity if found, or an empty Optional if not found
     * @throws DataStorageException if there was an error accessing the data storage
     */
    @Override
    public Optional<RecipeIngredientEntity> findByGuid(String guid) {
        var sql = """
                SELECT id, guid, recipeGuid, ingredientGuid, unit, amount
                FROM RecipeIngredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeIngredientFromResultSet(resultSet));
            } else {

                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipeIngredient by guid: " + guid, ex);
        }
    }

    /**
     * Updates an recipeIngredient entity in the data storage.
     *
     * @param entity the recipeIngredient entity to update
     * @return the updated recipeIngredient entity
     * @throws DataStorageException if there is an error updating the recipeIngredient
     */
    @Override
    public RecipeIngredientEntity update(RecipeIngredientEntity entity) {
        var sql = """
                UPDATE RecipeIngredient
                SET recipeGuid = ?,
                    ingredientGuid = ?,
                    unit = ?,
                    amount = ?,
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.recipeGuid());
            statement.setString(3, entity.ingredientGuid());
            statement.setObject(4, entity.unit());
            statement.setDouble(5, entity.amount());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("RecipeIngredient not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipeIngredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipeIngredient: " + entity, ex);
        }
    }

    /**
     * Deletes an recipeIngredient from the database based on its GUID.
     * Throws a DataStorageException if the recipeIngredient is not found or if more than one recipeIngredient is deleted.
     *
     * @param guid The GUID of the recipeIngredient to be deleted.
     * @throws DataStorageException If the recipeIngredient is not found or if more than one recipeIngredient is deleted.
     * @throws SQLException If there is an error executing the SQL statement.
     */
    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM RecipeIngredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("RecipeIngredient not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipeIngredient (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipeIngredient, guid: " + guid, ex);
        }
    }

    /**
     * Deletes all recipeIngredients from the database.
     *
     * @throws DataStorageException if there is an error in the data storage or database operation
     */
    @Override
    public void deleteAll() {
        var sql = "DELETE FROM RecipeIngredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipeIngredients", ex);
        }
    }

    /**
     * Checks if an recipeIngredient with the given GUID exists in the data storage.
     *
     * @param guid The GUID of the recipeIngredient to check for existence.
     * @return {@code true} if the recipeIngredient exists, {@code false} otherwise.
     * @throws DataStorageException If an error occurs while checking the existence of the recipeIngredient.
     */
    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM RecipeIngredient
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
            throw new DataStorageException("Failed to check if recipeIngredient exists, guid: " + guid, ex);
        }
    }

    /**
     * This method converts a ResultSet object to an RecipeIngredientEntity object.
     *
     * @param resultSet the ResultSet object to convert
     * @return a new RecipeIngredientEntity object with data from the ResultSet
     * @throws SQLException if an error occurs while retrieving data from the ResultSet
     */
    private static RecipeIngredientEntity recipeIngredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeIngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("recipeGuid"),
                resultSet.getString("ingredientGuid"),
                resultSet.getObject("unit", Unit.class),
                resultSet.getDouble("amount")
        );
    }
}
