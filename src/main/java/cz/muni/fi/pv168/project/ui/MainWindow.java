package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.action.ExportAction;
import cz.muni.fi.pv168.project.ui.action.ImportAction;
import cz.muni.fi.pv168.project.ui.action.QuitAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.AddIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.DeleteIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.EditIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.FilterIngredientAction;
import cz.muni.fi.pv168.project.ui.action.recipe.*;
import cz.muni.fi.pv168.project.ui.action.unit.AddUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.DeleteUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.EditUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.FilterUnitAction;
import cz.muni.fi.pv168.project.ui.model.*;
import cz.muni.fi.pv168.project.ui.model.tables.IngredientsTable;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeTable;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.renderers.MyFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.*;


public class MainWindow {
    private final MyFrame mainFrame;
    private final Action quitAction = new QuitAction();
    private Action addAction;
    private Action editAction;
    private Action deleteAction;
    private final Action importAction;
    private final Action exportAction;
    private Action filterAction;
    private JToolBar toolbar;
    private final RecipeTable recipeTable;
    private final UnitTable unitTable;
    private final IngredientsTable ingredientTable;

    public MainWindow() {
        mainFrame = MainWindowUtilities.createFrame(null, null, "ChowChamber");
        mainFrame.setIconImage(new ImageIcon("src/main/resources/cz/muni/fi/pv168/project/ui/resources/chowcham-logo1.png").getImage());
        // tables
        recipeTable = (RecipeTable) MainWindowUtilities.createTableFromModel(new RecipeTableModel(TestTable.getTableOne()), 0, this::rowSelectionChanged);
        unitTable = (UnitTable) MainWindowUtilities.createTableFromModel(new UnitTableModel(TestTable.getTableTwo()), 3, this::rowSelectionChanged);
        ingredientTable = (IngredientsTable) MainWindowUtilities.createTableFromModel(new IngredientTableModel(TestTable.getTableThree()), 1, this::rowSelectionChanged);


        addAction = new AddRecipeAction(recipeTable);
        editAction = new EditRecipeAction(recipeTable);
        deleteAction = new DeleteRecipeAction(recipeTable);
        importAction = new ImportAction();
        exportAction = new ExportAction();
        filterAction = new FilterRecipeAction();

        // tables tabs
        JTabbedPane mainFrameTabs = new JTabbedPane();
        mainFrameTabs.setOpaque(true);
        mainFrameTabs.addTab("<html><b>Recipes</b></html>", new JScrollPane(recipeTable));
        mainFrameTabs.addTab("<html><b>Units</b></html>", new JScrollPane(unitTable));
        mainFrameTabs.addTab("<html><b>Ingredients</b></html>", new JScrollPane(ingredientTable));
        mainFrame.add(mainFrameTabs, BorderLayout.CENTER);

        // toolbar
        this.toolbar = createToolbar();
        mainFrame.add(this.toolbar, BorderLayout.BEFORE_FIRST_LINE);
        mainFrame.setJMenuBar(createMenuBar());

        recipeTable.setMouseListener(recipeTable);
        unitTable.setMouseListener(unitTable);
        ingredientTable.setMouseListener(ingredientTable);

        mainFrameTabs.addChangeListener(e -> updateToolbar(mainFrameTabs.getSelectedIndex()));

        mainFrame.pack();
    }

    private void updateToolbar(int selectedIndex) {
        mainFrame.remove(this.toolbar);

        switch (selectedIndex) {
            case 0:  // Recipes tab
                addAction = new AddRecipeAction(recipeTable);
                editAction = new EditRecipeAction(recipeTable);
                deleteAction = new DeleteRecipeAction(recipeTable);
                filterAction = new FilterRecipeAction();
                break;
            case 1:  // Units tab
                addAction = new AddUnitAction(unitTable);
                editAction = new EditUnitAction(unitTable);
                deleteAction = new DeleteUnitAction(unitTable);
                filterAction = new FilterUnitAction();
                break;
            case 2:  // Ingredients tab
                addAction = new AddIngredientAction(ingredientTable);
                editAction = new EditIngredientAction(ingredientTable);
                deleteAction = new DeleteIngredientAction(ingredientTable);
                filterAction = new FilterIngredientAction();
                break;
        }

        toolbar = createToolbar();
        mainFrame.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);
        mainFrame.validate();
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
