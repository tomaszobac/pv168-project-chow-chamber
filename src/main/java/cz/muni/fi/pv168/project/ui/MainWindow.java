package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.List;

public class MainWindow {
    private final JFrame frame;
    private final Action quitAction = new QuitAction();
    private final Action addAction;
    private final Action editAction;
    private final Action deleteAction;
    private final Action importAction;
    private final Action exportAction;
    private final Action filterAction;
    public MainWindow() {
        frame = createFrame(null, null, "Best recipes app"); // TODO: agree on name of our app
        MyTable recipeTable = createTable(TestTable.getTableOne());

        addAction = new AddAction(recipeTable);
        editAction = new EditAction(recipeTable);
        deleteAction = new DeleteAction(recipeTable);
        importAction = new ImportAction();
        exportAction = new ExportAction();
        filterAction = new FilterAction();

        frame.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        recipeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) { // TODO: open recipe different way
                    openRecipeInfoWindow(recipeTable);
                }
            }
        });
        frame.pack();
    }

    /**
     * This method opens new window(s) upon clicking on recipe(s). One window contains two tabs, first tab contains basic info,
     * second tab contains more info about one recipe.
     *
     * @param recipeTable represents table of stored recipes.
     */
    private void openRecipeInfoWindow(MyTable recipeTable) {
        JFrame infoFrame = createFrame(new Dimension(400, 200), new Dimension(960, 540), "Recipe");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create a JPanel to display the recipe information
        JPanel infoPanel = new JPanel(new GridLayout(2, 2,  10, 10));
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 0).toString())));
        infoPanel.add(new JLabel("Category:"));
        infoPanel.add(new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 1).toString())));
        infoPanel.add(new JLabel("Time:"));
        infoPanel.add(new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 2).toString())));
        infoPanel.add(new JLabel("Portions:"));
        infoPanel.add(new JTextField((recipeTable.getValueAt(recipeTable.getSelectedRow(), 3).toString())));
        // Add more labels for other recipe attributes here
        tabbedPane.addTab("Basic info", null, infoPanel, "First Tab");

        // TODO: add listener for edits in JTextFields so that changes can be stored

        JPanel tab2 = new JPanel();
        tab2.add(new JLabel("The missile knows where it is at all times. It knows this because it knows where it isn't.")); // TODO: add more info about each recipe
        tabbedPane.addTab("More", null, tab2, "Second Tab");

        infoFrame.add(tabbedPane);
        infoFrame.pack();
        infoFrame.setVisible(true);
    }


    public void show() {
        frame.setVisible(true);
    }


    private JFrame createFrame(Dimension min_size, Dimension max_size, String name) {
        MyFrame Mframe = new MyFrame();
        Mframe.setTitle(name);
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setVisible(true);

        if (min_size == null) {min_size = new Dimension(800, 400);}
        if (max_size == null) {max_size = new Dimension(1920, 1080);}
        Mframe.setMinimumSize(min_size);
        Mframe.setMaximumSize(max_size);

        return Mframe;
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
