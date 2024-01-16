package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.ui.workers.AsyncImporter;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ImportAction extends AbstractAction {
    private final AsyncImporter importer;

    public ImportAction(Component parent, ImportService importService, Runnable update) {
        super("Import", Icons.IMPORT_ICON);
        this.importer = new AsyncImporter(parent, importService, update);

        putValue(SHORT_DESCRIPTION, "Imports data");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importer.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(null);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String importFile = selectedFile.getAbsolutePath();

            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                importFile = ((Filter) filter).decorate(importFile);
            }

            importer.importData(importFile);
        }
    }
}
