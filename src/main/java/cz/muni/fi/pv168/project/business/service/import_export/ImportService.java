package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

import java.util.Collection;

/**
 * Generic mechanism, allowing to import data from a file.
 */
public interface ImportService {

    /**
     * Imports data from a file.
     *
     * @param filePath absolute path of the import_export file (to be created or overwritten)
     *
     * @throws BatchOperationException if the import cannot be done
     */
    void importData(String filePath);

    /**
     * Gets all available formats for import.
     */
    Collection<Format> getFormats();
}
