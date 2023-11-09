package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.service.export.ExportService;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ExportAction extends AbstractAction {

    private final Component parent;
    private final ExportService exportService;

    public ExportAction(Component parent, ExportService exportService) {
        super("Export", Icons.EXPORT_ICON);
        this.parent = parent;
        this.exportService = exportService;

        putValue(SHORT_DESCRIPTION, "Exports data");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        exportService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showSaveDialog(parent);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            String exportFile = fileChooser.getSelectedFile().getAbsolutePath();
            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                exportFile = ((Filter) filter).decorate(exportFile);
            }

            exportService.exportData(exportFile);

            JOptionPane.showMessageDialog(parent, "Export has successfully finished.");
        }
    }
}
