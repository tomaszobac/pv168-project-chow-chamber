package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DeleteAction extends AbstractAction {
    public DeleteAction() {
        super("Delete", Icons.DELETE_ICON);
        putValue(SHORT_DESCRIPTION, "Deletes selected recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Add actual performance to the DeleteAction
    }
}
