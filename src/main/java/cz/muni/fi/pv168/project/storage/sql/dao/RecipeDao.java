package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link RecipeEntity} entity.
 */
public final class RecipeDao implements DataAccessObject<RecipeEntity> {

    private final Supplier<ConnectionHandler> connections;

    /**
     * Constructs a new RecipeDao object with the given supplier of ConnectionHandler objects.
     *
     * @param connections a supplier of ConnectionHandler objects to be used for obtaining database connections
     */
    public RecipeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    /**
     * Creates a new recipe in the database.
     *
     * @param newRecipe The recipe entity to create.
     * @return The created recipe entity.
     * @throws DataStorageException If an error occurs while storing the recipe.
     */
    @Override
    public RecipeEntity create(RecipeEntity newRecipe) {
        var sql = """
                INSERT INTO Recipe(
                    guid,
                    name,
                    category,
                    time,
                    portions,
                    instructions
                )
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRecipe.guid());
            statement.setString(2, newRecipe.name());
            statement.setString(3, newRecipe.category().toString());
            statement.setTime(4, Time.valueOf(newRecipe.time()));
            statement.setInt(5, newRecipe.portions());
            statement.setString(6, newRecipe.instructions());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRecipe);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRecipe);
                }

                return findById(recipeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRecipe, ex);
        }
    }

    /**
     * Retrieves all recipes from the database.
     *
     * @return a collection of RecipeEntity objects representing the fetched recipes
     * @throws DataStorageException if there is an error while loading the recipes from the database
     */
    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        category,
                        time,
                        portions,
                        instructions
                FROM Recipe
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipe = recipeFromResultSet(resultSet);
                    recipes.add(recipe);
                }
            }

            return recipes;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipes", ex);
        }
    }

    /**
     * Finds a recipe by its ID.
     *
     * @param id the ID of the recipe to find
     * @return an Optional containing the found RecipeEntity if present, or an empty Optional if no recipe is found
     * @throws DataStorageException if there is an error accessing the data storage
     */
    @Override
    public Optional<RecipeEntity> findById(long id) {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        category,
                        time,
                        portions,
                        instructions
                FROM Recipe
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by id", ex);
        }
    }

    /**
     * Retrieves a recipe entity from the database by its GUID.
     *
     * @param guid the GUID of the recipe to find
     * @return an optional containing the recipe entity if found, or empty otherwise
     * @throws DataStorageException if there is an error accessing the database
     */
    @Override
    public Optional<RecipeEntity> findByGuid(String guid) {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        category,
                        time,
                        portions,
                        instructions
                FROM Recipe
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by id", ex);
        }
    }

    /**
     * Updates a recipe entity in the database.
     *
     * @param entity The recipe entity to update
     * @return The updated recipe entity
     * @throws DataStorageException if an error occurs while updating the recipe
     */
    @Override
    public RecipeEntity update(RecipeEntity entity) {
        var sql = """
                UPDATE Recipe
                SET guid = ?,
                    name = ?,
                    category = ?,
                    time = ?,
                    portions = ?,
                    instructions = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.name());
            statement.setString(3, entity.category().toString());
            statement.setString(4, entity.time().toString());
            statement.setInt(5, entity.portions());
            statement.setString(6, entity.instructions());
            statement.setLong(7, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe: " + entity, ex);
        }
    }

    /**
     * Deletes a recipe from the database based on the given GUID.
     *
     * @param guid the GUID of the recipe to be deleted
     * @throws DataStorageException if the recipe is not found or if more than one recipe is deleted
     */
    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Recipe WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe, guid: " + guid, ex);
        }
    }

    /**
     * Deletes all recipes from the database.
     *
     * @throws DataStorageException if there was an error deleting the recipes
     */
    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Recipe";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipes", ex);
        }
    }

    /**
     * Checks if a recipe with the given GUID exists in the data storage.
     *
     * @param guid the GUID of the recipe to check
     * @return true if a recipe with the given GUID exists, false otherwise
     * @throws DataStorageException if an error occurs while checking the existence of the recipe
     */
    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Recipe
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if recipe exists: " + guid, ex);
        }
    }

    /**
     * Convert a ResultSet to a RecipeEntity object.
     *
     * @param resultSet the ResultSet containing the recipe data
     * @return the RecipeEntity object
     * @throws SQLException if there is an error accessing the ResultSet
     */
    private static RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("guid"),
                    resultSet.getString("name"),
                    RecipeCategory.valueOf(resultSet.getString("category")),
                    resultSet.getTimestamp("time").toLocalDateTime().toLocalTime(),
                    resultSet.getInt("portions"),
                    resultSet.getString("instructions")
        );
    }
}
