package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.*;
import cz.muni.fi.pv168.project.ui.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
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
        frame = createFrame();
        JTable recipeTable = createRecipeTable(); // TODO: this method take a list of (test) data

        addAction = new AddAction(recipeTable);
        editAction = new EditAction();
        deleteAction = new DeleteAction();
        importAction = new ImportAction();
        exportAction = new ExportAction();
        filterAction = new FilterAction();

        frame.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    private JFrame createFrame() {
        JFrame jFrame = new JFrame("Recipe DB");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        Dimension min_size = new Dimension(800, 400);
        Dimension max_size = new Dimension(1920, 1080);
        jFrame.setMinimumSize(min_size);
        jFrame.setMaximumSize(max_size);

        return jFrame;
    }

    /*private JFrame createFrame() {
        MyFrame Mframe = new MyFrame();
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setVisible(true);

        Dimension min_size = new Dimension(800, 400);
        Dimension max_size = new Dimension(1920, 1080);
        Mframe.setMinimumSize(min_size);
        Mframe.setMaximumSize(max_size);

        return Mframe;
    }*/

    private JTable createRecipeTable() {
        List<Recipe> recipes = List.of(new Recipe("vomáčka", "příloha", "00:10","4"));
        var model = new RecipeTableModel(recipes); // TODO: Fix empty list here
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
}
