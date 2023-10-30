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

public class RecipeTable extends MyTable {
    private final List<List<String>> infoTables = new ArrayList<>();
    private JFrame recipesInfoFrame = null;
    private JTabbedPane recipesInfoTabs = null;
    private int recipeInTabs = 0;

    public RecipeTable(AbstractTableModel model) {
        super(model);
    }
    public void setMouseListener(MyTable recipeTable) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(recipeTable);
                    if(index != -1) {
                        MainWindowUtilities.switchToTab(index, recipesInfoTabs);
                        recipesInfoFrame.setVisible(true);
                    } else{
                        recipeInTabs++;
                        List<String> newRecipe = new ArrayList<>();
                        newRecipe.add(getValueAt(getSelectedRow(), 0).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 1).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 2).toString());
                        newRecipe.add(getValueAt(getSelectedRow(), 3).toString());
                        infoTables.add(newRecipe);
                        openRecipeInfoWindow(recipeTable);
                    }
                }
            }
        });
    }

    private int findTable(MyTable recipeTable){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).get(i).equals(recipeTable.getValueAt(recipeTable.getSelectedRow(), i).toString())) {
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
     * @param recipeTable represents table of stored recipes.
     */
    private void openRecipeInfoWindow(MyTable recipeTable) {
        if (recipesInfoFrame == null) {
            recipesInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            recipesInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            recipesInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 0), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Category:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 1), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Time:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipeTable.getValueAt(recipeTable.getSelectedRow(), 2).toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((Integer.toString((Integer) recipeTable.getValueAt(recipeTable.getSelectedRow(), 3))), 1));

        // Add more labels for other recipe attributes here
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");
        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("The missile knows where it is at all times. It knows this because it knows where it isn't.")); // TODO: add more info about each recipe
        singleRecipeInfo.addTab("More", null, tab2, "Second Tab");
        // creates and handles tabs of singleRecipeInfo
        createNewRecipeTab(singleRecipeInfo, recipeTable.getValueAt(recipeTable.getSelectedRow(), 0).toString());
        MainWindowUtilities.switchToTab(recipeInTabs - 1, recipesInfoTabs);
        recipesInfoFrame.add(recipesInfoTabs);
        recipesInfoFrame.pack();
        recipesInfoFrame.setVisible(true);
    }

    private void createNewRecipeTab(JTabbedPane singleRecipeInfo, String name) {
        // Create a custom tab component with a close button
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getjButton(singleRecipeInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        // Add the tab to the tabbed pane with the custom tab component
        recipesInfoTabs.addTab(null, singleRecipeInfo);
        int tabIndex = recipesInfoTabs.indexOfComponent(singleRecipeInfo);
        recipesInfoTabs.setTabComponentAt(tabIndex, customTabComponent);
        // Set the selected tab
        recipesInfoTabs.setSelectedIndex(tabIndex);
    }

    private JButton getjButton(JTabbedPane singleRecipeInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
            // Handle tab removal when the close button is clicked
            int tabIndex = recipesInfoTabs.indexOfComponent(singleRecipeInfo);
            if (tabIndex != -1) {
                recipesInfoTabs.remove(tabIndex);
                infoTables.remove(tabIndex);
                recipeInTabs--; // TODO: fix recipe scuffed numbering when removed not last tab
                if (recipeInTabs == 0) {
                    recipesInfoFrame.dispose();
                }
            }
        });
        return closeButton;
    }
}
