package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class IngredientsTable extends MyTable<Ingredient> {
    public IngredientsTable(AbstractTableModel model) {
        super(model);
    }

    /**
     * This method opens new window(s) upon clicking on Ingredient(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one Ingredient.
     *
     * @param ingredientTable represents table of stored Ingredients.
     */
    @Override
    protected void openInfoWindow(MyTable<Ingredient> ingredientTable) {
        if (infoFrame == null) {
            infoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Ingredient");
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoTabs = new JTabbedPane();
        }
        JTabbedPane singleIngredientInfo = new JTabbedPane();
        Ingredient ingredient = (Ingredient) ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 0);

        JPanel infoPanel = createInfoPanel(ingredient);

        singleIngredientInfo.addTab("Basic info", null, infoPanel, "First Tab");
        createNewTab(singleIngredientInfo, ingredient.getName());
        MainWindowUtilities.switchToTab(inTabs - 1, infoTabs);
        infoFrame.add(infoTabs);
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    private JPanel createInfoPanel(Ingredient ingredient) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(ingredient.getName(), 0));
        infoPanel.add(MainWindowUtilities.createLabel("Calories:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(Double.toString(ingredient.getCalories()), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Unit:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(ingredient.getUnit().getName(), 1));
        return infoPanel;
    }
}
