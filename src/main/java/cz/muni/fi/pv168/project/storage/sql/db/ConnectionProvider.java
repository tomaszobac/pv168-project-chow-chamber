package cz.muni.fi.pv168.project.storage.sql.db;

import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Execute the statements/operation in the transaction
 */
public final class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataStorageException("Unable to get a new connection", e);
        }
    }

    public Connection getTransactionalConnection() {
        var connection = getConnection();

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DataStorageException("Unable to disable auto-commit for connection", e);
        }

        return connection;
    }
}
