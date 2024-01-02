package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.business.error.FatalException;

import java.io.Serial;

public class FatalDataStorageException extends DataStorageException implements FatalException {

    @Serial
    private static final long serialVersionUID = 4512050973972830440L;

    public FatalDataStorageException(String message) {
        super(message);
    }

    public FatalDataStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalDataStorageException(String userMessage, String message, Throwable cause) {
        super(userMessage, message, cause);
    }
}

