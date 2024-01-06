package cz.muni.fi.pv168.project.ui.workers;

import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Objects;

public class AsyncImporter {
    private final Component parent;
    private final ImportService importService;
    private final Runnable onFinish;
    private final JProgressBar progressBar = new JProgressBar();


    public AsyncImporter(Component parent, ImportService importService, Runnable onFinish) {
        this.parent = parent;
        this.importService = Objects.requireNonNull(importService);
        this.onFinish = onFinish;
    }

    public Collection<Format> getFormats() {
        return importService.getFormats();
    }

    public void importData(String filePath) {
        JFrame progressBarWindow = new JFrame("Progress");
        progressBarWindow.setUndecorated(true);
        progressBarWindow.setMinimumSize(new Dimension(150, 50));
        progressBarWindow.setMaximumSize(new Dimension(150, 50));
        progressBarWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        progressBarWindow.setLocation((screenWidth - progressBarWindow.getWidth()) / 2, (screenHeight - progressBarWindow.getHeight()) / 2);

        progressBarWindow.setLayout(new BorderLayout());
        progressBarWindow.add(progressBar, BorderLayout.CENTER);

        JButton cancelButton = new JButton("Cancel");
        progressBarWindow.add(cancelButton, BorderLayout.SOUTH);

        progressBar.setIndeterminate(true);

        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                importService.importData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                cancelButton.setEnabled(false);
                progressBar.setIndeterminate(false);
                JOptionPane.showMessageDialog(parent, "Import has successfully finished.");
                onFinish.run();
                progressBarWindow.dispose();
            }
        };

        cancelButton.addActionListener(e -> {
            asyncWorker.cancel(true);
            progressBar.setIndeterminate(false);
            progressBarWindow.dispose();
        });

        progressBarWindow.pack();
        progressBarWindow.setVisible(true);
        asyncWorker.execute();
    }
}
