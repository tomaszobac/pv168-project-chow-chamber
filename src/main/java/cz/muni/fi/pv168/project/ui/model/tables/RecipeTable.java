package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class RecipeTable extends MyTable<Recipe> {

    public RecipeTable(AbstractTableModel model) {
        super(model);
    }

    /**
     * This method opens new window(s) upon clicking on recipe(s). One window contains three tabs, first tab contains basic info,
     * second tab contains info about ingredients and the third one contains instructions.
     *
     * @param recipeTable represents table of stored recipes.
     */
    @Override
    protected void openInfoWindow(MyTable<Recipe> recipeTable, JTable recIncTable, RecipeIngredientTableFilter filter) {
        if (infoFrame == null) {
            infoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoFrame.setMinimumSize(new Dimension(500, 300));
            infoTabs = new JTabbedPane();
        }
        Recipe recipe = (Recipe) recipeTable.getValueAt(recipeTable.getSelectedRow(), 0);

        JTabbedPane singleRecipeInfo = new JTabbedPane();
        singleRecipeInfo.addChangeListener( e -> {
            filter.filterGuid(recipe.getGuid());
        });

        JPanel infoPanel = createInfoPanel(recipe);
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel ingredientsTab = createIngredientsTab(gbc, recIncTable, filter, recipe);
        singleRecipeInfo.addTab("Ingredients", null, ingredientsTab, "Second Tab");

        JPanel instructionTab = createInstructionsTab(gbc, recipe);
        singleRecipeInfo.addTab("Instructions", null, instructionTab, "Third Tab");

        createNewTab(singleRecipeInfo, recipe.getName());
        MainWindowUtilities.switchToTab(inTabs - 1, infoTabs);
        infoFrame.add(infoTabs);
        infoFrame.pack();
        infoFrame.setVisible(true);


    }

    private JPanel createInstructionsTab(GridBagConstraints gbc, Recipe recipe) {
        JPanel instructionTab = new JPanel(new GridBagLayout());

        JTextArea textArea = new JTextArea(recipe.getInstructions());
        textArea.setFont(textArea.getFont().deriveFont(15f));
        textArea.setLineWrap(true);
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(new Color(255, 255, 255));
        textArea.setPreferredSize(new Dimension(200, 300));
        JScrollPane instructionsScrollPane = new JScrollPane(textArea);
        gbc.insets = new Insets(20, 20, 20, 20);
        instructionTab.add(instructionsScrollPane, gbc);

        gbc.insets = new Insets(20, 20, 20, 20);
        return instructionTab;
    }

    private JPanel createIngredientsTab(GridBagConstraints gbc, JTable table, RecipeIngredientTableFilter filter, Recipe recipe) {
        JPanel ingredientsTab = new JPanel(new GridBagLayout());



        var model = (RecipeIngredientsTableModel) table.getModel();
        var rowSorter = new TableRowSorter<>((RecipeIngredientsTableModel) model);
        var newTable = new RecipeIngredientsTable(model);
        var newFilter = new RecipeIngredientTableFilter(rowSorter);
        newTable.setRowSorter(rowSorter);
        JScrollPane recipeIngredientsScrollPane = new JScrollPane(newTable);
        newFilter.filterGuid(recipe.getGuid());

        ingredientsTab.add(recipeIngredientsScrollPane, gbc);
        return ingredientsTab;
    }

    private JPanel createInfoPanel(Recipe recipe) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getName(), 0), 1);
        infoPanel.add(MainWindowUtilities.createLabel("Category:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getCategoryName(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Time:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getTime().toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(Integer.toString(recipe.getPortions()), 1));
        return infoPanel;
    }
}
