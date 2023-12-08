package cz.muni.fi.pv168.project.ui.model.tables;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.RecipeIngredientCrudService;
import cz.muni.fi.pv168.project.business.service.validation.RecipeIngredientValidator;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
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
import java.util.stream.Collectors;

public class RecipeTable extends MyTable {
    private final List<Recipe> infoTables = new ArrayList<>();
    private JFrame recipesInfoFrame = null;
    private JTabbedPane recipesInfoTabs = null;
    private int recipeInTabs = 0;

    public RecipeTable(AbstractTableModel model) {
        super(model);
    }
    public void setMouseListener(MyTable recipeTable, RecipeIngredientCrudService recIngCrud, Repository<Ingredient> ingRepository, Repository<Recipe> recRepository, GuidProvider provider) {
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
                        infoTables.add((Recipe) getValueAt(getSelectedRow(), 0));
                        openRecipeInfoWindow(recipeTable, recIngCrud, ingRepository, recRepository, provider);
                    }
                }
            }
        });
    }

    private int findTable(MyTable recipeTable){
        boolean flag = true;
        for (int j = 0; j < infoTables.size(); j++) {
            for (int i = 0; i < 4; i++) {
                if (!infoTables.get(j).equals(recipeTable.getValueAt(recipeTable.getSelectedRow(), 0))) {
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
    private void openRecipeInfoWindow(MyTable recipeTable, RecipeIngredientCrudService recIngCrud, Repository<Ingredient> ingRepository, Repository<Recipe> recRepository, GuidProvider provider) {
        if (recipesInfoFrame == null) {
            recipesInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            recipesInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            recipesInfoFrame.setMinimumSize(new Dimension(500, 300));
            recipesInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();
        Recipe recipe = (Recipe) recipeTable.getValueAt(recipeTable.getSelectedRow(), 0);

        JPanel infoPanel = createInfoPanel(recipe);

        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel ingredientsTab = new JPanel(new GridBagLayout());

        RecipeIngredientValidator validator = new RecipeIngredientValidator();
        RecipeIngredientCrudService crud = new RecipeIngredientCrudService(new InMemoryRepository<>(List.of()), validator, provider);
        RecipeIngredientsTableModel recIngTableModel = new RecipeIngredientsTableModel(crud , recRepository, ingRepository);
        getIngredients(recipe, recIngCrud).forEach(recIngTableModel::addRow);
        JTable table = MainWindowUtilities.createTableFromModel(recIngTableModel, 2, null);
        JScrollPane recipeIngredientsScrollPane = new JScrollPane(table);
        MainWindowUtilities.hideFirstColumn(table);

        ingredientsTab.add(recipeIngredientsScrollPane, gbc);

        singleRecipeInfo.addTab("Ingredients", null, ingredientsTab, "Second Tab");

        JPanel instructionTab = new JPanel(new GridBagLayout());

        JTextArea textArea = new JTextArea(recipe.getInstructions());
        textArea.setEnabled(false);
        textArea.setDisabledTextColor(new Color(255, 255, 255));
        textArea.setPreferredSize(new Dimension(200, 300));
        JScrollPane instructionsScrollPane = new JScrollPane(textArea);
        gbc.insets = new Insets(20, 20, 20, 20);
        instructionTab.add(instructionsScrollPane, gbc);

        gbc.insets = new Insets(20, 20, 20, 20);
        singleRecipeInfo.addTab("Instructions", null, instructionTab, "Third Tab");

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
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getCategoryName(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Time:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(recipe.getTime().toString(), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel(Integer.toString(recipe.getPortions()), 1));
        return infoPanel;
    }

    private void createNewRecipeTab(JTabbedPane singleRecipeInfo, String name) {
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel(name);
        JButton closeButton = getjButton(singleRecipeInfo);
        customTabComponent.add(titleLabel);
        customTabComponent.add(closeButton);
        recipesInfoTabs.addTab(null, singleRecipeInfo);
        int tabIndex = recipesInfoTabs.indexOfComponent(singleRecipeInfo);
        recipesInfoTabs.setTabComponentAt(tabIndex, customTabComponent);
        recipesInfoTabs.setSelectedIndex(tabIndex);
    }

    private JButton getjButton(JTabbedPane singleRecipeInfo) {
        JButton closeButton = new JButton("X");
        closeButton.setPreferredSize(new Dimension(16, 16));

        closeButton.addActionListener(e -> {
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

    private List<RecipeIngredient> getIngredients(Recipe recipe, RecipeIngredientCrudService crud) {
        return crud.findAll()
                .stream()
                .filter(recipeIngredient -> recipeIngredient.getRecipeGuid().equals(recipe.getGuid()))
                .collect(Collectors.toList());
    }
}
