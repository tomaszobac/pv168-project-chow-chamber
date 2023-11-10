package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.service.export.ImportService;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class ImportAction extends AbstractAction {
    private final ImportService importService;

    public ImportAction(ImportService importService) {
        super("Import", Icons.IMPORT_ICON);
        this.importService = importService;

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

            JOptionPane.showMessageDialog(null, "Import was done");
        }
    }
}
