package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecipeDaoTest {
    private static RecipeDao recipeDao;
    private static int guid = 0;

    @BeforeAll
    static public void setUp() {
        TestDependencyProvider provider = new TestDependencyProvider();
        DatabaseManager databaseManager = provider.getDatabaseManager();
        var transactionManager = new TransactionManagerImpl(databaseManager);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);
        recipeDao = new RecipeDao(transactionConnectionSupplier);
    }

    @AfterEach
    public void cleanUp() {
        recipeDao.deleteAll();
    }

    @Test
    public void createRecipe() {
        RecipeEntity recipe = createSampleRecipe();
        RecipeEntity createdRecipe = recipeDao.create(recipe);

        assertNotNull(createdRecipe.id());
        assertEquals(recipe.guid(), createdRecipe.guid());
        assertEquals(recipe.name(), createdRecipe.name());
        assertEquals(recipe.category(), createdRecipe.category());
        assertEquals(recipe.time(), createdRecipe.time());
        assertEquals(recipe.portions(), createdRecipe.portions());
        assertEquals(recipe.instructions(), createdRecipe.instructions());
    }

    @Test
    public void findAllRecipes() {
        RecipeEntity recipe1 = createSampleRecipe();
        RecipeEntity recipe2 = createSampleRecipe();

        recipeDao.create(recipe1);
        recipeDao.create(recipe2);

        Collection<RecipeEntity> recipes = recipeDao.findAll();

        assertEquals(2, recipes.size());
        assertTrue(recipes.stream().anyMatch(r -> r.guid().equals(recipe1.guid())));
        assertTrue(recipes.stream().anyMatch(r -> r.guid().equals(recipe2.guid())));
    }

    @Test
    public void findRecipeById() {
        RecipeEntity recipe = createSampleRecipe();
        RecipeEntity createdRecipe = recipeDao.create(recipe);

        Optional<RecipeEntity> foundRecipe = recipeDao.findById(createdRecipe.id());

        assertTrue(foundRecipe.isPresent());
        assertEquals(createdRecipe, foundRecipe.get());
    }

    @Test
    public void findRecipeByGuid() {
        RecipeEntity recipe = createSampleRecipe();
        RecipeEntity createdRecipe = recipeDao.create(recipe);

        Optional<RecipeEntity> foundRecipe = recipeDao.findByGuid(createdRecipe.guid());

        assertTrue(foundRecipe.isPresent());
        assertEquals(createdRecipe, foundRecipe.get());
    }

    @Test
    public void updateRecipe() {
        RecipeEntity recipe = createSampleRecipe();
        RecipeEntity createdRecipe = recipeDao.create(recipe);

        // Modify the recipe
        RecipeEntity updatedRecipe = new RecipeEntity(
                createdRecipe.id(),
                createdRecipe.guid(),
                "Updated Name",
                RecipeCategory.ZAKUSEK,
                LocalTime.of(0, 30),
                5,
                "Updated Instructions"
        );

        RecipeEntity result = recipeDao.update(updatedRecipe);

        assertEquals(updatedRecipe, result);

        Optional<RecipeEntity> foundRecipe = recipeDao.findById(createdRecipe.id());
        assertTrue(foundRecipe.isPresent());
        assertEquals(updatedRecipe, foundRecipe.get());
    }

    @Test
    public void deleteRecipeByGuid() {
        RecipeEntity recipe = createSampleRecipe();
        RecipeEntity createdRecipe = recipeDao.create(recipe);

        recipeDao.deleteByGuid(createdRecipe.guid());

        Optional<RecipeEntity> foundRecipe = recipeDao.findById(createdRecipe.id());
        assertTrue(foundRecipe.isEmpty());
    }

    @Test
    public void deleteAllRecipes() {
        RecipeEntity recipe1 = createSampleRecipe();
        RecipeEntity recipe2 = createSampleRecipe();

        recipeDao.create(recipe1);
        recipeDao.create(recipe2);

        recipeDao.deleteAll();

        Collection<RecipeEntity> recipes = recipeDao.findAll();
        assertTrue(recipes.isEmpty());
    }

    private RecipeEntity createSampleRecipe() {
        guid += 1;
        return new RecipeEntity(
                null,
                "guid" + guid,
                "Spaghetti Bolognese",
                RecipeCategory.HLAVNI_JIDLO,
                LocalTime.of(1, 0),
                4,
                "Cook the spaghetti, prepare the Bolognese sauce, and enjoy!"
        );
    }
}