package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.AddAction;
import cz.muni.fi.pv168.project.ui.action.DeleteAction;
import cz.muni.fi.pv168.project.ui.action.EditAction;
import cz.muni.fi.pv168.project.ui.action.QuitAction;
import cz.muni.fi.pv168.project.ui.action.ImportAction;
import cz.muni.fi.pv168.project.ui.action.ExportAction;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

public class MainWindow {
    private final JFrame frame;
    private final Action quitAction = new QuitAction();
    private final Action addAction;
    private final Action editAction;
    private final Action deleteAction;
    private final Action importAction;
    private final Action exportAction;
    public MainWindow() {
        frame = createFrame();

        addAction = new AddAction();
        editAction = new EditAction();
        deleteAction = new DeleteAction();
        importAction = new ImportAction();
        exportAction = new ExportAction();

        // ### START OF NEW CODE ###
        JTable recipeTable = createRecipeTable();
        frame.add(new JScrollPane(recipeTable), BorderLayout.CENTER); // TODO: Add our table into the JScroll Pane
        // ### END OF NEW CODE ###
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    private JFrame createFrame() {
        JFrame jFrame = new JFrame("Recipe DB");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        Dimension min_size = new Dimension(800, 400);
        jFrame.setMinimumSize(min_size);
        return jFrame;
    }

    private JTable createRecipeTable() {
        var model = new RecipeTableModel(new ArrayList<>()); // TODO: Fix empty list here
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        return table;
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

        menuBar.add(editMenu);
        menuBar.add(dataMenu);
        return menuBar;
    }
}
