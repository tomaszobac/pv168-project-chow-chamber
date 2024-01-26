package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.service.import_export.ExportService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.ui.workers.AsyncExporter;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ExportAction extends AbstractAction {

    private final AsyncExporter exporter;

    public ExportAction(Component parent, ExportService exportService) {
        super("Export", Icons.EXPORT_ICON);
        this.exporter = new AsyncExporter(
                (GenericExportService) exportService,
                () -> JOptionPane.showMessageDialog(parent, "Export has successfully finished."));

        putValue(SHORT_DESCRIPTION, "Exports data");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        exporter.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showSaveDialog(null);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            if (selectedFile.exists()) {
                int overwriteOption = JOptionPane.showConfirmDialog(
                        null,
                        "The file already exists. Do you want to overwrite it?",
                        "File Exists",
                        JOptionPane.YES_NO_OPTION
                );

                if (overwriteOption != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            String exportFile = selectedFile.getAbsolutePath();
            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                exportFile = ((Filter) filter).decorate(exportFile);
            }

            exporter.exportData(exportFile);
        }
    }
}
