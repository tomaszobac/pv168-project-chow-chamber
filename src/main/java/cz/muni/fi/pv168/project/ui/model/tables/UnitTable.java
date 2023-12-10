package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;

public class UnitTable extends MyTable<Unit> {
    public UnitTable(AbstractTableModel model) {
        super(model);
    }

    /**
     * This method opens new window(s) upon clicking on recipe(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one recipe.
     *
     * @param unitTable represents table of stored recipes.
     */
    @Override
    protected void openInfoWindow(MyTable<Unit> unitTable) {
        if (infoFrame == null) {
            infoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Unit");
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();
        Unit unit = (Unit) unitTable.getValueAt(unitTable.getSelectedRow(), 0);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(unit.getName(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Type:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(unit.getType().toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Base:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((Double.toString(unit.getConversionToBase())), 1));

        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");
        createNewTab(singleRecipeInfo, unit.getName());
        MainWindowUtilities.switchToTab(inTabs - 1, infoTabs);
        infoFrame.add(infoTabs);
        infoFrame.pack();
        infoFrame.setVisible(true);
    }
}
