package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ImportAction extends AbstractAction {
    private final ImportService importService;
    private final Runnable callback;

    public ImportAction(ImportService importService, Runnable callback) {
        super("Import", Icons.IMPORT_ICON);
        this.importService = importService;
        this.callback = callback;

        putValue(SHORT_DESCRIPTION, "Imports data");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(null);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();

            importService.importData(importFile.getAbsolutePath());

            callback.run();
            JOptionPane.showMessageDialog(null, "Import was done");
        }
    }
}
