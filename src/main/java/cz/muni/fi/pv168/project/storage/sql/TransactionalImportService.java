package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import java.util.Collection;

public class TransactionalImportService implements ImportService {

    private final ImportService importService;

    private final TransactionExecutor transactionExecutor;

    public TransactionalImportService(ImportService importService, TransactionExecutor transactionExecutor) {
        this.importService = importService;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public void importData(String filePath) {
        transactionExecutor.executeInTransaction(() -> importService.importData(filePath));
    }

    @Override
    public Collection<Format> getFormats() {
        return importService.getFormats();
    }
}
