package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.UnitMapper;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IngredientDaoTest {

    private static IngredientDao ingredientDao;
    private static UnitDao unitDao;
    private static Unit testUnit;

    @BeforeAll
    static void setUp() {
        TestDependencyProvider provider = new TestDependencyProvider();
        DatabaseManager databaseManager = provider.getDatabaseManager();
        var transactionManager = new TransactionManagerImpl(databaseManager);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);
        unitDao = new UnitDao(transactionConnectionSupplier);
        ingredientDao = new IngredientDao(transactionConnectionSupplier, unitDao);

        // Creating a test unit to be used in ingredient tests
        testUnit = new Unit("testUnitGuid", "TestUnit", UnitType.Weight, 1.0);
        var unitMapper = new UnitMapper();
        var testUnitEntity = unitMapper.mapExistingEntityToDatabase(testUnit, 1L);
        unitDao.create(testUnitEntity);
    }

    @AfterEach
    public void cleanUpIngredients() {
        ingredientDao.deleteAll();
    }

    @AfterAll
    public static void cleanUpUnits() {
        unitDao.deleteAll();
    }

    @Test
    public void testCreateAndFindById() {
        IngredientEntity ingredient = new IngredientEntity(null, "guid1", "Sugar", 100.0, testUnit);
        IngredientEntity createdIngredient = ingredientDao.create(ingredient);

        assertNotNull(createdIngredient.id());

        Optional<IngredientEntity> foundIngredient = ingredientDao.findById(createdIngredient.id());
        assertTrue(foundIngredient.isPresent());
        assertEquals(createdIngredient, foundIngredient.get());
    }

    @Test
    public void testCreateAndFindByGuid() {
        IngredientEntity ingredient = new IngredientEntity(null, "guid2", "Flour", 200.0, testUnit);
        IngredientEntity createdIngredient = ingredientDao.create(ingredient);

        assertNotNull(createdIngredient.id());

        Optional<IngredientEntity> foundIngredient = ingredientDao.findByGuid("guid2");
        assertTrue(foundIngredient.isPresent());
        assertEquals(createdIngredient, foundIngredient.get());
    }

    @Test
    public void testUpdate() {
        IngredientEntity ingredient = new IngredientEntity(null, "guid3", "Salt", 50.0, testUnit);
        IngredientEntity createdIngredient = ingredientDao.create(ingredient);

        createdIngredient = new IngredientEntity(createdIngredient.id(), "guid3", "Salt", 75.0, testUnit);
        IngredientEntity updatedIngredient = ingredientDao.update(createdIngredient);

        assertEquals(createdIngredient, updatedIngredient);
    }

    @Test
    public void testDeleteByGuid() {
        IngredientEntity ingredient = new IngredientEntity(null, "guid4", "Pepper", 30.0, testUnit);
        IngredientEntity createdIngredient = ingredientDao.create(ingredient);

        assertNotNull(createdIngredient.id());

        ingredientDao.deleteByGuid("guid4");

        Optional<IngredientEntity> deletedIngredient = ingredientDao.findByGuid("guid4");
        assertTrue(deletedIngredient.isEmpty());
    }

    @Test
    public void testFindAll() {
        IngredientEntity ingredient1 = new IngredientEntity(1L, "guid5", "Coffee", 5.0, testUnit);
        IngredientEntity ingredient2 = new IngredientEntity(2L, "guid6", "Tea", 10.0, testUnit);

        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);

        Collection<IngredientEntity> ingredients = ingredientDao.findAll();
        assertEquals(2, ingredients.size());
        assertTrue(ingredients.stream().anyMatch(i -> i.guid().equals(ingredient1.guid())));
        assertTrue(ingredients.stream().anyMatch(i -> i.guid().equals(ingredient2.guid())));
    }

    @Test
    public void testExistsByGuid() {
        IngredientEntity ingredient = new IngredientEntity(null, "guid7", "Vanilla", 15.0, testUnit);
        ingredientDao.create(ingredient);

        assertTrue(ingredientDao.existsByGuid("guid7"));
        assertFalse(ingredientDao.existsByGuid("nonexistentGuid"));
    }

    @Test
    public void testDeleteAll() {
        IngredientEntity ingredient1 = new IngredientEntity(null, "guid8", "Chocolate", 20.0, testUnit);
        IngredientEntity ingredient2 = new IngredientEntity(null, "guid9", "Sugar", 25.0, testUnit);

        ingredientDao.create(ingredient1);
        ingredientDao.create(ingredient2);

        ingredientDao.deleteAll();

        Collection<IngredientEntity> ingredients = ingredientDao.findAll();
        assertEquals(0, ingredients.size());
    }
}