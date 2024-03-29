package cz.muni.fi.pv168.project.ui.action.ingredient;

import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteIngredientAction extends AbstractAction {
    private final JTable ingredientTable;
    public DeleteIngredientAction(JTable ingredientTable) {
        super("Delete", Icons.DELETE_ICON);
        this.ingredientTable = ingredientTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected ingredients");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var ingredientTableModel = (IngredientTableModel) ingredientTable.getModel();
        Arrays.stream(ingredientTable.getSelectedRows())
                .map(ingredientTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(ingredientTableModel::deleteRow);
    }
}
