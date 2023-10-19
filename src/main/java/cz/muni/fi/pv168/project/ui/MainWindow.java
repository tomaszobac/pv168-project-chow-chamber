package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.*;
import java.awt.*;
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
    private JTabbedPane infoTable = null;
    private JFrame recipesInfoFrame = null;

    private JTabbedPane recipesInfoTabs = null;
    private int recipeInTabs = 0;

    public MainWindow() {
        mainFrame = MainWindowUtilities.createFrame(null, null, "Best recipes app"); // TODO: agree on name of our app
        MyTable recipeTable = createTable(TestTable.getTableOne());

        addAction = new AddAction(recipeTable);
        editAction = new EditAction(recipeTable);
        deleteAction = new DeleteAction(recipeTable);
        importAction = new ImportAction();
        exportAction = new ExportAction();
        filterAction = new FilterAction();

        mainFrame.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
        mainFrame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        mainFrame.setJMenuBar(createMenuBar());

        recipeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    recipeInTabs++;
                    openRecipeInfoWindow(recipeTable);
                    //openRecipeWindow3(recipeTable);
                }
            }
        });
        mainFrame.pack();
    }

    /**
     * This method opens new window(s) upon clicking on recipe(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one recipe.
     *
     * @param recipeTable represents table of stored recipes.
     */

    private JTabbedPane openRecipeInfoWindow2(MyTable recipeTable) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JTextField name = new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 0).toString()));
        JTextField category = new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 1).toString()));
        JTextField time = new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 2).toString()));
        JTextField portions = new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 3).toString()));

        name.setEditable(false);
        category.setEditable(false);
        time.setEditable(false);
        portions.setEditable(false);

        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(name);
        infoPanel.add(new JLabel("Category:"));
        infoPanel.add(category);
        infoPanel.add(new JLabel("Time:"));
        infoPanel.add(time);
        infoPanel.add(new JLabel("Portions:"));
        infoPanel.add(portions);

        // Add more labels for other recipe attributes here
        tabbedPane.addTab("Basic info", null, infoPanel, "First Tab");

        // TODO: add listener for edits in JTextFields so that changes can be stored

        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("The missile knows where it is at all times. It knows this because it knows where it isn't.")); // TODO: add more info about each recipe
        tabbedPane.addTab("More", null, tab2, "Second Tab");
        return tabbedPane;
    }

    private void openRecipeWindow3(MyTable recipeTable) {
        if (this.infoTable == null) {
            JFrame infoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            this.recipesInfoFrame = infoFrame;
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JTabbedPane tabbedPane = openRecipeInfoWindow2(recipeTable);
            this.infoTable = tabbedPane;
            infoFrame.add(tabbedPane);
            infoFrame.pack();
            infoFrame.setVisible(true);
            infoFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    infoTable = null;
                }
            });
        } else {
            this.recipesInfoFrame.toFront();
        }
    }

    private void openRecipeInfoWindow(MyTable recipeTable) {
        if (recipesInfoFrame == null) {
            recipesInfoFrame = MainWindowUtilities.createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
            recipesInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            recipesInfoTabs = new JTabbedPane();
        }
        JTabbedPane singleRecipeInfo = new JTabbedPane();

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Use a variable number of rows (0) for flexibility
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        infoPanel.add(MainWindowUtilities.createLabel("Name:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 0), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Category:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 1), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Time:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 2), 1));
        infoPanel.add(MainWindowUtilities.createLabel("Portions:", 0));
        infoPanel.add(MainWindowUtilities.createLabel((String) recipeTable.getValueAt(recipeTable.getSelectedRow(), 3), 1));

        // Add more labels for other recipe attributes here
        singleRecipeInfo.addTab("Basic info", null, infoPanel, "First Tab");
        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("The missile knows where it is at all times. It knows this because it knows where it isn't.")); // TODO: add more info about each recipe
        singleRecipeInfo.addTab("More", null, tab2, "Second Tab");

        // creates and handles tabs of singleRecipeInfo
        createNewRecipeTab(singleRecipeInfo);
        MainWindowUtilities.switchToRecipeTab(recipeInTabs - 1, recipesInfoTabs);

        recipesInfoFrame.add(recipesInfoTabs);
        recipesInfoFrame.pack();
        recipesInfoFrame.setVisible(true);
    }

    private void createNewRecipeTab(JTabbedPane singleRecipeInfo) {
        // Create a custom tab component with a close button
        JPanel customTabComponent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel titleLabel = new JLabel("recipe " + recipeInTabs);
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

    private MyTable createTable(List<Recipe> recipes) {
        var model = new RecipeTableModel(recipes); // TODO: Fix empty list here
        MyTable MTable = new MyTable(model);
        MTable.setAutoCreateRowSorter(true);
        MTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
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
