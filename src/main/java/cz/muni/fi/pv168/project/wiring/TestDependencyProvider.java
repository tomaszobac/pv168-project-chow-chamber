package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;

/**
 * Dependency provider for production environment.
 */
public class TestDependencyProvider extends CommonDependencyProvider {

    public TestDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createTestInstance();
        databaseManager.initSchema();

        return databaseManager;
    }
}
