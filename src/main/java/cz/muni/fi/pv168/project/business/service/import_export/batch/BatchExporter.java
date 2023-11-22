package cz.muni.fi.pv168.project.business.service.import_export.batch;

import cz.muni.fi.pv168.project.business.service.import_export.DataManipulationException;
import cz.muni.fi.pv168.project.business.service.import_export.format.FileFormat;

/**
 * Generic mechanism, allowing to import_export a {@link Batch} of entities to a file.
 *
 * <p>Both the <i>format</i> and <i>encoding</i> of the file are to be defined and
 * documented by implementations of this interface.
 */
public interface BatchExporter extends FileFormat {

    /**
     * Exports the data from the {@link Batch} to a file.
     *
     * @param batch    entities to be exported (in the collection iteration order)
     * @param filePath absolute path of the import_export file (to be created or overwritten)
     * @throws DataManipulationException if the import_export file cannot be opened or written
     */
    void exportBatch(Batch batch, String filePath);
}
