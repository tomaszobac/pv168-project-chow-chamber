package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
// TODO: Finish the changes
public class IngredientsTable extends MyTable {
    private final List<List<String>> infoTables = new ArrayList<>();
    private JFrame ingredientInfoFrame = null;
    private JTabbedPane ingredientInfoTabs = null;
    private int ingredientInTabs = 0;
    public IngredientsTable(AbstractTableModel model) {
        super(model);
    }

    public void setMouseListener(MyTable ingredientTable) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(ingredientTable);
                    if(index != -1) {
                        MainWindowUtilities.switchToRecipeTab(index, ingredientInfoTabs);
                        ingredientInfoFrame.setVisible(true);
                    } else{
                        ingredientInTabs++;
                        List<String> newRecipe = new ArrayList<>();
                        newRecipe.add(getValueAt(getSelectedRow(), 0).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 1).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 2).toString());
                        infoTables.add(newRecipe);
                        openInfoWindow(ingredientTable);
                    }
                }
            }
        });
    }

    private int findTable(MyTable ingredientTable){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).get(i).equals(ingredientTable.getValueAt(ingredientTable.getSelectedRow(), i).toString())) {
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
     * @param ingredientTable represents table of stored recipes.
     */
    private void openInfoWindow(MyTable ingredientTable) {
        if (ingredientInfoFrame == null) {
            ingredientInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            ingredientInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ingredientInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 0), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Type:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 1).toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((Double.toString((Double) ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 2))), 1));

        // Add more labels for other recipe attributes here
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");
        // creates and handles tabs of singleRecipeInfo
        createNewTab(singleRecipeInfo, ingredientTable.getValueAt(ingredientTable.getSelectedRow(), 0).toString());
        MainWindowUtilities.switchToRecipeTab(ingredientInTabs - 1, ingredientInfoTabs);
        ingredientInfoFrame.add(ingredientInfoTabs);
        ingredientInfoFrame.pack();
        ingredientInfoFrame.setVisible(true);
    }

    private void createNewTab(JTabbedPane singleRecipeInfo, String name) {
        // Create a custom tab component with a close button
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getJButton(singleRecipeInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        // Add the tab to the tabbed pane with the custom tab component
        ingredientInfoTabs.addTab(null, singleRecipeInfo);
        int tabIndex = ingredientInfoTabs.indexOfComponent(singleRecipeInfo);
        ingredientInfoTabs.setTabComponentAt(tabIndex, customTabComponent);
        // Set the selected tab
        ingredientInfoTabs.setSelectedIndex(tabIndex);
    }

    private JButton getJButton(JTabbedPane singleRecipeInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
            // Handle tab removal when the close button is clicked
            int tabIndex = ingredientInfoTabs.indexOfComponent(singleRecipeInfo);
            if (tabIndex != -1) {
                ingredientInfoTabs.remove(tabIndex);
                infoTables.remove(tabIndex);
                ingredientInTabs--; // TODO: fix recipe scuffed numbering when removed not last tab
                if (ingredientInTabs == 0) {
                    ingredientInfoFrame.dispose();
                }
            }
        });
        return closeButton;
    }
}
