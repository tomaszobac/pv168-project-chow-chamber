package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnitDaoTest {

    private static UnitDao unitDao;

    @BeforeAll
    static void setUp() {
        TestDependencyProvider provider = new TestDependencyProvider();
        DatabaseManager databaseManager = provider.getDatabaseManager();
        var transactionManager = new TransactionManagerImpl(databaseManager);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);
        unitDao = new UnitDao(transactionConnectionSupplier);
    }

    @AfterEach
    void cleanUp() {
        unitDao.deleteAll();
    }

    @Test
    void testCreateAndFindById() {
        UnitEntity unit = new UnitEntity(null, "guid1", "Kilogram", UnitType.Weight, 1.0);
        UnitEntity createdUnit = unitDao.create(unit);

        assertNotNull(createdUnit.id());

        Optional<UnitEntity> foundUnit = unitDao.findById(createdUnit.id());
        assertTrue(foundUnit.isPresent());
        assertEquals(createdUnit, foundUnit.get());
    }

    @Test
    void testCreateAndFindByGuid() {
        UnitEntity unit = new UnitEntity(null, "guid2", "Liter", UnitType.Volume, 1.5);
        UnitEntity createdUnit = unitDao.create(unit);

        assertNotNull(createdUnit.id());

        Optional<UnitEntity> foundUnit = unitDao.findByGuid("guid2");
        assertTrue(foundUnit.isPresent());
        assertEquals(createdUnit, foundUnit.get());
    }

    @Test
    void testUpdate() {
        UnitEntity unit = new UnitEntity(null, "guid3", "Piece", UnitType.Weight, 2.0);
        UnitEntity createdUnit = unitDao.create(unit);

        createdUnit = new UnitEntity(createdUnit.id(), "guid3", "Piece", UnitType.Weight, 3.0);
        UnitEntity updatedUnit = unitDao.update(createdUnit);

        assertEquals(createdUnit, updatedUnit);
    }

    @Test
    void testDeleteByGuid() {
        UnitEntity unit = new UnitEntity(null, "guid4", "Meter", UnitType.Weight, 4.0);
        UnitEntity createdUnit = unitDao.create(unit);

        assertNotNull(createdUnit.id());

        unitDao.deleteByGuid("guid4");

        Optional<UnitEntity> deletedUnit = unitDao.findByGuid("guid4");
        assertTrue(deletedUnit.isEmpty());
    }

    @Test
    void testFindAll() {
        UnitEntity unit1 = new UnitEntity(1L, "guid5", "Pound", UnitType.Weight, 5.0);
        UnitEntity unit2 = new UnitEntity(2L, "guid6", "Gallon", UnitType.Volume, 6.0);

        unitDao.create(unit1);
        unitDao.create(unit2);

        Collection<UnitEntity> units = unitDao.findAll();
        assertEquals(2, units.size());
        assertTrue(units.stream().anyMatch(u -> u.guid().equals(unit1.guid())));
        assertTrue(units.stream().anyMatch(u -> u.guid().equals(unit2.guid())));
    }

    @Test
    void testExistsByGuid() {
        UnitEntity unit = new UnitEntity(null, "guid7", "Ounce", UnitType.Weight, 7.0);
        unitDao.create(unit);

        assertTrue(unitDao.existsByGuid("guid7"));
        assertFalse(unitDao.existsByGuid("nonexistentGuid"));
    }

    @Test
    void testDeleteAll() {
        UnitEntity unit1 = new UnitEntity(null, "guid8", "Inch", UnitType.Weight, 8.0);
        UnitEntity unit2 = new UnitEntity(null, "guid9", "Fluid Ounce", UnitType.Volume, 9.0);

        unitDao.create(unit1);
        unitDao.create(unit2);

        unitDao.deleteAll();

        Collection<UnitEntity> units = unitDao.findAll();
        assertEquals(0, units.size());
    }
}
