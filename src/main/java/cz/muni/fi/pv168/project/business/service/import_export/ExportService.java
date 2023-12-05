package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

import java.util.Collection;

/**
 * Generic mechanism, allowing to import_export data to a file.
 */
public interface ExportService {

    /**
     * Exports data to a file.
     *
     * @param filePath absolute path of the import_export file (to be created or overwritten)
     *
     * @throws BatchOperationException if the import_export cannot be done
     */
    void exportData(String filePath);

    /**
     * Gets all available formats for import_export.
     */
    Collection<Format> getFormats();
}
