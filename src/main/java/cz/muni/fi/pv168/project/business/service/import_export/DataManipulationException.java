package cz.muni.fi.pv168.project.business.service.import_export;

/**
 * Exception thrown if there is some problem with data import/import_export.
 */
public final class DataManipulationException extends RuntimeException {

    public DataManipulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataManipulationException(String message) {
        super(message);
    }
}
