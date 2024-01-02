package cz.muni.fi.pv168.project.ui.workers;

import cz.muni.fi.pv168.project.business.service.import_export.ExportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

import javax.swing.SwingWorker;
import java.util.Collection;
import java.util.Objects;

import java.util.concurrent.TimeUnit;


/**
 * Implementation of asynchronous exporter for UI.
 */
public class AsyncExporter {

    private final ExportService exportService;
    private final Runnable onFinish;

    public AsyncExporter(ExportService exportService, Runnable onFinish) {
        this.exportService = Objects.requireNonNull(exportService);
        this.onFinish = onFinish;
    }

    public Collection<Format> getFormats() {
        return exportService.getFormats();
    }

    public void exportData(String filePath) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {

                exportService.exportData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                onFinish.run();
            }
        };
        asyncWorker.execute();
    }
}

