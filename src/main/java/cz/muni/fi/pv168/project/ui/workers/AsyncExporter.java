package cz.muni.fi.pv168.project.ui.workers;

import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Collection;
import java.util.Objects;

import java.util.concurrent.TimeUnit;


/**
 * Implementation of asynchronous exporter for UI.
 */
public class AsyncExporter {

    private final GenericExportService exportService;
    private final Runnable onFinish;
    private final JProgressBar progressBar = new JProgressBar();


    public AsyncExporter(GenericExportService exportService, Runnable onFinish) {
        this.exportService = Objects.requireNonNull(exportService);
        this.onFinish = onFinish;
    }

    public Collection<Format> getFormats() {
        return exportService.getFormats();
    }

    public void exportData(String filePath) {
        JFrame progressBarWindow = new JFrame("Progress");
        progressBarWindow.setMinimumSize(new Dimension(400, 100));
        progressBarWindow.setMaximumSize(new Dimension(400, 100));
        progressBarWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        progressBarWindow.setLocation((screenWidth - progressBarWindow.getWidth()) / 2, (screenHeight - progressBarWindow.getHeight()) / 2);

        progressBarWindow.setLayout(new BorderLayout());
        progressBarWindow.add(progressBar, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel");
        progressBarWindow.add(cancelButton, BorderLayout.SOUTH);

        progressBar.setValue(0);

        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                progressBar.setValue(exportService.getExportEntitiesCount());
                exportService.exportData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                cancelButton.setEnabled(false);
                progressBar.setValue(0);
                onFinish.run();
                progressBarWindow.dispose();
            }
        };

        cancelButton.addActionListener(e -> {
            asyncWorker.cancel(true);
            progressBarWindow.dispose();
        });

        asyncWorker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName()) && !asyncWorker.isCancelled()) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        progressBarWindow.pack();
        progressBarWindow.setVisible(true);
        asyncWorker.execute();
    }
}

