package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UnitTable extends MyTable {
    private final List<List<String>> infoTables = new ArrayList<>();
    private JFrame unitInfoFrame = null;
    private JTabbedPane unitInfoTabs = null;
    private int unitInTabs = 0;
    public UnitTable(AbstractTableModel model) {
        super(model);
    }

    public void setMouseListener(MyTable unitTable) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(unitTable);
                    if(index != -1) {
                        MainWindowUtilities.switchToTab(index, unitInfoTabs);
                        unitInfoFrame.setVisible(true);
                    } else{
                        unitInTabs++;
                        List<String> newRecipe = new ArrayList<>();
                        newRecipe.add(getValueAt(getSelectedRow(), 0).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 1).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 2).toString());
                        infoTables.add(newRecipe);
                        openInfoWindow(unitTable);
                    }
                }
            }
        });
    }

    private int findTable(MyTable unitTable){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).get(i).equals(unitTable.getValueAt(unitTable.getSelectedRow(), i).toString())) {
                    flag = true;
                    break;
                }
                flag = false;
            }
            if (!flag) {
                return j;
            }
        }
        return -1;
    }
    /**
     * This method opens new window(s) upon clicking on recipe(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one recipe.
     *
     * @param unitTable represents table of stored recipes.
     */
    private void openInfoWindow(MyTable unitTable) {
        if (unitInfoFrame == null) {
            unitInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Unit");
            unitInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            unitInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();
        Unit unit = (Unit) unitTable.getValueAt(unitTable.getSelectedRow(), 0);

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(unit.getName(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Type:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(unit.getType().toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Base:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((Double.toString(unit.getConversionToBase())), 1));

        // Add more labels for other recipe attributes here
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");
        // creates and handles tabs of singleRecipeInfo
        createNewTab(singleRecipeInfo, unit.getName());
        MainWindowUtilities.switchToTab(unitInTabs - 1, unitInfoTabs);
        unitInfoFrame.add(unitInfoTabs);
        unitInfoFrame.pack();
        unitInfoFrame.setVisible(true);
    }

    private void createNewTab(JTabbedPane singleRecipeInfo, String name) {
        // Create a custom tab component with a close button
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getJButton(singleRecipeInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        // Add the tab to the tabbed pane with the custom tab component
        unitInfoTabs.addTab(null, singleRecipeInfo);
        int tabIndex = unitInfoTabs.indexOfComponent(singleRecipeInfo);
        unitInfoTabs.setTabComponentAt(tabIndex, customTabComponent);
        // Set the selected tab
        unitInfoTabs.setSelectedIndex(tabIndex);
    }

    private JButton getJButton(JTabbedPane singleRecipeInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
            // Handle tab removal when the close button is clicked
            int tabIndex = unitInfoTabs.indexOfComponent(singleRecipeInfo);
            if (tabIndex != -1) {
                unitInfoTabs.remove(tabIndex);
                infoTables.remove(tabIndex);
                unitInTabs--;
                if (unitInTabs == 0) {
                    unitInfoFrame.dispose();
                }
            }
        });
        return closeButton;
    }
}
