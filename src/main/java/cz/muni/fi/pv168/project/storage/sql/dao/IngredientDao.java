package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;

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
 * DAO for {@link IngredientEntity} entity.
 */
public final class IngredientDao implements DataAccessObject<IngredientEntity> {

    private final Supplier<ConnectionHandler> connections;

    /**
     * Constructs a new IngredientDao object with the given Supplier of ConnectionHandler.
     *
     * @param connections the Supplier of ConnectionHandler to be used by the IngredientDao
     */
    public IngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    /**
     * Creates a new ingredient in the data storage.
     *
     * @param newIngredient the new ingredient to create in the data storage
     * @return the created ingredient entity
     * @throws DataStorageException if an error occurs while storing the ingredient
     */
    @Override
    public IngredientEntity create(IngredientEntity newIngredient) {
        var sql = "INSERT INTO Ingredient (guid, name, calories, unit) VALUES (?, ?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newIngredient.guid());
            statement.setString(2, newIngredient.name());
            statement.setDouble(3, newIngredient.calories());
            statement.setObject(4, newIngredient.unit());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long ingredientId;

                if (keyResultSet.next()) {
                    ingredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newIngredient);
                }

                return findById(ingredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newIngredient, ex);
        }
    }

    /**
     * Retrieves all ingredients from the database.
     *
     * @return a collection of IngredientEntity objects representing all the ingredients stored in the database
     * @throws DataStorageException if there is an error retrieving the ingredients from the database
     */
    @Override
    public Collection<IngredientEntity> findAll() {
        var sql = """
                SELECT id, guid, name, calories, unit
                FROM Ingredient
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = ingredientFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients", ex);
        }
    }

    /**
     * Retrieves an ingredient from the database based on the provided ID.
     *
     * @param id the ID of the ingredient to retrieve
     * @return an Optional containing the IngredientEntity if found, or an empty Optional if not found
     * @throws DataStorageException if there is an error retrieving the ingredient
     */
    @Override
    public Optional<IngredientEntity> findById(long id) {
        var sql = """
                SELECT id, guid, name, calories, unit
                FROM Ingredient
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id: " + id, ex);
        }
    }

    /**
     * Finds an ingredient entity by its GUID.
     *
     * @param guid the GUID of the ingredient to find
     * @return an Optional containing the ingredient entity if found, or an empty Optional if not found
     * @throws DataStorageException if there was an error accessing the data storage
     */
    @Override
    public Optional<IngredientEntity> findByGuid(String guid) {
        var sql = """
                SELECT id, guid, name, calories, unit
                FROM Ingredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {

                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by guid: " + guid, ex);
        }
    }

    /**
     * Updates an ingredient entity in the data storage.
     *
     * @param entity the ingredient entity to update
     * @return the updated ingredient entity
     * @throws DataStorageException if there is an error updating the ingredient
     */
    @Override
    public IngredientEntity update(IngredientEntity entity) {
        var sql = """
                UPDATE Ingredient
                SET guid = ?,
                    name = ?,
                    calories = ?,
                    unit = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.name());
            statement.setDouble(3, entity.calories());
            statement.setObject(4, entity.unit());
            statement.setObject(5, entity.id());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ingredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient: " + entity, ex);
        }
    }

    /**
     * Deletes an ingredient from the database based on its GUID.
     * Throws a DataStorageException if the ingredient is not found or if more than one ingredient is deleted.
     *
     * @param guid The GUID of the ingredient to be deleted.
     * @throws DataStorageException If the ingredient is not found or if more than one ingredient is deleted.
     * @throws SQLException If there is an error executing the SQL statement.
     */
    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Ingredient
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ingredient (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "Deletion contains ingredients that are still being used");
            throw new DataStorageException("Failed to delete Ingredient, guid: " + guid, ex);
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient, guid: " + guid, ex);
        }
    }

    /**
     * Deletes all ingredients from the database.
     *
     * @throws DataStorageException if there is an error in the data storage or database operation
     */
    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Ingredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all ingredients", ex);
        }
    }

    /**
     * Checks if an ingredient with the given GUID exists in the data storage.
     *
     * @param guid The GUID of the ingredient to check for existence.
     * @return {@code true} if the ingredient exists, {@code false} otherwise.
     * @throws DataStorageException If an error occurs while checking the existence of the ingredient.
     */
    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Ingredient
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
            throw new DataStorageException("Failed to check if ingredient exists, guid: " + guid, ex);
        }
    }

    /**
     * This method converts a ResultSet object to an IngredientEntity object.
     *
     * @param resultSet the ResultSet object to convert
     * @return a new IngredientEntity object with data from the ResultSet
     * @throws SQLException if an error occurs while retrieving data from the ResultSet
     */
    private static IngredientEntity ingredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("name"),
                resultSet.getDouble("calories"),
                resultSet.getObject("unit", Unit.class)
        );
    }
}
