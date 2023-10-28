package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;
import cz.muni.fi.pv168.project.ui.renderers.UnifiedTableCellRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class MainWindow {
    private final JFrame mainFrame;
    private final Action quitAction = new QuitAction();
    private final Action addAction;
    private final Action editAction;
    private final Action deleteAction;
    private final Action importAction;
    private final Action exportAction;
    private final Action filterAction;
    private final List<List<String>> infoTables = new ArrayList<>();
    private JFrame recipesInfoFrame = null;

    private JTabbedPane recipesInfoTabs = null;
    private int recipeInTabs = 0;

    public MainWindow() {
        mainFrame = MainWindowUtilities.createFrame(null, null, "ChowChamber");
        mainFrame.setIconImage(new ImageIcon("src/main/resources/cz/muni/fi/pv168/project/ui/resources/chowcham-logo1.png").getImage());
        MyTable recipeTable = createTable(new RecipeTableModel(TestTable.getTableOne()));
        MyTable unitTable = createTable(new UnitTableModel(TestTable.getTableTwo()));
        MyTable ingredientTable = createTable(new IngredientTableModel(TestTable.getTableThree()));


        addAction = new AddAction(recipeTable);
        editAction = new EditAction(recipeTable);
        deleteAction = new DeleteAction(recipeTable);
        importAction = new ImportAction();
        exportAction = new ExportAction();
        filterAction = new FilterAction();

        // tables tabs
        JTabbedPane mainFrameTabs = new JTabbedPane();
        mainFrameTabs.setOpaque(true);
        mainFrameTabs.addTab("<html><b>Recipes table</b></html>", new JScrollPane(recipeTable));
        mainFrameTabs.addTab("<html><b>Units</b></html>", new JScrollPane(unitTable));
        mainFrameTabs.addTab("<html><b>Ingredients</b></html>", new JScrollPane(ingredientTable));
        mainFrame.add(mainFrameTabs, BorderLayout.CENTER);

        // toolbar
        mainFrame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        mainFrame.setJMenuBar(createMenuBar());

        // recipe info popup window
        recipeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int index = findTable(recipeTable);
                    if(index != -1) {
                        MainWindowUtilities.switchToRecipeTab(index, recipesInfoTabs);
                        recipesInfoFrame.setVisible(true);
                    } else{
                        recipeInTabs++;
                        List<String> newRecipe = new ArrayList<>();
                        newRecipe.add(recipeTable.getValueAt(recipeTable.getSelectedRow(), 0).toString());
                        newRecipe.add(recipeTable.getValueAt(recipeTable.getSelectedRow(), 1).toString());
                        newRecipe.add(recipeTable.getValueAt(recipeTable.getSelectedRow(), 2).toString());
                        newRecipe.add(recipeTable.getValueAt(recipeTable.getSelectedRow(), 3).toString());
                        infoTables.add(newRecipe);
                        openRecipeInfoWindow(recipeTable);
                    }
                }
            }
        });
        mainFrame.pack();
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
        MainWindowUtilities.switchToRecipeTab(recipeInTabs - 1, recipesInfoTabs);
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

    public void show() {
        mainFrame.setVisible(true);
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.add(quitAction);
        toolbar.addSeparator();
        toolbar.add(addAction);
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.addSeparator();
        toolbar.add(importAction);
        toolbar.add(exportAction);
        toolbar.addSeparator();
        toolbar.add(filterAction);
        return toolbar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('e');
        editMenu.add(addAction);
        editMenu.add(editAction);
        editMenu.add(deleteAction);
        editMenu.addSeparator();
        editMenu.add(quitAction);

        JMenu dataMenu = new JMenu("Data");
        dataMenu.setMnemonic('d');
        dataMenu.add(importAction);
        dataMenu.add(exportAction);
        dataMenu.addSeparator();
        dataMenu.add(filterAction);

        menuBar.add(editMenu);
        menuBar.add(dataMenu);
        return menuBar;
    }

    private MyTable createTable(AbstractTableModel model) {
        MyTable MTable = new MyTable(model);
        MTable.setAutoCreateRowSorter(true);
        MTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        UnifiedTableCellRenderer renderer = new UnifiedTableCellRenderer();
        MTable.setDefaultRenderer(Object.class, renderer);
        MTable.setDefaultRenderer(Integer.class, renderer);
        MTable.setDefaultRenderer(LocalTime.class, renderer);
        MTable.setRowSorter(new TableRowSorter<>(model));
        return MTable;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        if (selectionModel.isSelectionEmpty()) {
            editAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else if (selectionModel.getSelectedItemsCount() == 1) {
            editAction.setEnabled(true);
            deleteAction.setEnabled(true);
        } else if (selectionModel.getSelectedItemsCount() > 1) {
            editAction.setEnabled(false);
            deleteAction.setEnabled(true);
        }
    }
}
