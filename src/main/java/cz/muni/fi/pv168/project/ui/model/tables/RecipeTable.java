package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
        Recipe recipe = (Recipe) recipeTable.getValueAt(recipeTable.getSelectedRow(), 0);

        // Create a JPanel to display the recipe information
        JPanel infoPanel = createInfoPanel(recipe);

        // Add more labels for other recipe attributes here
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "Basic information of the recipe");

        // Use GridBagConstraints to control resizing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; // Allows horizontal resizing
        gbc.weighty = 1.0; // Allows vertical resizing

        JPanel ingredientsTab = new JPanel(new GridBagLayout());
        RecipeIngredientsTableModel recipeIngredientsTableModel = new RecipeIngredientsTableModel(recipe.getIngredients());
        RecipeIngredientsTable recipeIngredientsTable = (RecipeIngredientsTable) MainWindowUtilities.createTableFromModel(recipeIngredientsTableModel, 2, null);
        JScrollPane recipeIngredientsScrollPane = new JScrollPane(recipeIngredientsTable);

        ingredientsTab.add(recipeIngredientsScrollPane, gbc);

        singleRecipeInfo.addTab("Ingredients", null, ingredientsTab, "Ingredients used in the recipe");



        JPanel instructionTab = new JPanel(new GridBagLayout());

        JTextArea textArea = new JTextArea(recipe.getInstructions());
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(new Color(255, 255, 255));
        textArea.setPreferredSize(new Dimension(200, 300));
        JScrollPane instructionsScrollPane = new JScrollPane(textArea);
        gbc.insets = new Insets(20, 20, 20, 20); // Gives it space between border and the content
        instructionTab.add(instructionsScrollPane, gbc);

        singleRecipeInfo.addTab("Instructions", null, instructionTab, "Instructions on how to make the recipe");


        // creates and handles tabs of singleRecipeInfo
        createNewRecipeTab(singleRecipeInfo, recipe.getName());
        MainWindowUtilities.switchToTab(recipeInTabs - 1, recipesInfoTabs);
        recipesInfoFrame.add(recipesInfoTabs);
        recipesInfoFrame.pack();
        recipesInfoFrame.setVisible(true);
    }

    private JPanel createInfoPanel(Recipe recipe) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getName(), 0), 1);
        infoPanel.add(MainWindowUtilities.createLabel("Category:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getCategory().getCategory(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Time:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getTime().toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(Integer.toString(recipe.getPortions()), 1));
        return infoPanel;
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
                recipeInTabs--;
                if (recipeInTabs == 0) {
                    recipesInfoFrame.dispose();
                }
            }
        });
        return closeButton;
    }
}
