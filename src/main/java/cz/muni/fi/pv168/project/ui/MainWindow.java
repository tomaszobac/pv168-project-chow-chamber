package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.ConvertAction;
import cz.muni.fi.pv168.project.ui.action.ExportAction;
import cz.muni.fi.pv168.project.ui.action.ImportAction;
import cz.muni.fi.pv168.project.ui.action.QuitAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.AddIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.DeleteIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.EditIngredientAction;
import cz.muni.fi.pv168.project.ui.action.ingredient.FilterIngredientAction;
import cz.muni.fi.pv168.project.ui.action.recipe.AddRecipeAction;
import cz.muni.fi.pv168.project.ui.action.recipe.DeleteRecipeAction;
import cz.muni.fi.pv168.project.ui.action.recipe.EditRecipeAction;
import cz.muni.fi.pv168.project.ui.action.recipe.FilterRecipeAction;
import cz.muni.fi.pv168.project.ui.action.unit.AddUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.DeleteUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.EditUnitAction;
import cz.muni.fi.pv168.project.ui.action.unit.FilterUnitAction;
import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.UnitTableFilter;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.model.tables.IngredientsTable;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeIngredientsTable;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeTable;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.renderers.MyFrame;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Component;

public class MainWindow {
    private final MyFrame mainFrame;
    private final Action quitAction;
    private Action addAction;
    private Action editAction;
    private Action deleteAction;
    private final Action importAction;
    private final Action exportAction;
    private final Action convertAction;
    private Action filterAction;
    private JToolBar toolbar;
    private JMenuBar menubar;
    private final RecipeTable recipeTable;
    private final UnitTable unitTable;
    private final IngredientsTable ingredientTable;
    private final RecipeIngredientsTable recipeIngredientsTable;
    private final RecipeTableFilter recipeTableFilter;
    private final UnitTableFilter unitTableFilter;
    private final IngredientTableFilter ingredientTableFilter;
    private final RecipeIngredientTableFilter recipeIngredientsTableFilter;

    public MainWindow(DependencyProvider dependencyProvider) {
        mainFrame = MainWindowUtilities.createFrame(null, null, "ChowChamber");
        mainFrame.setIconImage(new ImageIcon("src/main/resources/cz/muni/fi/pv168/project/ui/resources/chowcham-logo1.png").getImage());

        recipeTable = (RecipeTable) MainWindowUtilities.createTableFromModel(
                new RecipeTableModel(dependencyProvider.getRecipeCrudService()),
                0,
                this::rowSelectionChanged);
        unitTable = (UnitTable) MainWindowUtilities.createTableFromModel(
                new UnitTableModel(dependencyProvider.getUnitCrudService()),
                3,
                this::rowSelectionChanged);
        ingredientTable = (IngredientsTable) MainWindowUtilities.createTableFromModel(
                new IngredientTableModel(dependencyProvider.getIngredientCrudService()),
                1,
                this::rowSelectionChanged);
        recipeIngredientsTable = (RecipeIngredientsTable) MainWindowUtilities.createTableFromModel(
                new RecipeIngredientsTableModel(dependencyProvider.getRecipeIngredientCrudService(),
                        dependencyProvider.getRecipeRepository(),
                        dependencyProvider.getIngredientRepository()),
                2,
                this::rowSelectionChanged);

        MainWindowUtilities.hideFirstColumn(recipeTable);
        MainWindowUtilities.hideFirstColumn(ingredientTable);
        MainWindowUtilities.hideFirstColumn(unitTable);
        MainWindowUtilities.hideFirstColumn(recipeIngredientsTable);

        TableRowSorter<RecipeTableModel> recipeRowSorter = new TableRowSorter<>((RecipeTableModel) recipeTable.getModel());
        RecipeTableFilter recipeFilter = new RecipeTableFilter(recipeRowSorter);
        recipeTable.setRowSorter(recipeRowSorter);
        this.recipeTableFilter = recipeFilter;

        TableRowSorter<UnitTableModel> unitRowSorter = new TableRowSorter<>((UnitTableModel) unitTable.getModel());
        UnitTableFilter unitFilter = new UnitTableFilter(unitRowSorter);
        unitTable.setRowSorter(unitRowSorter);
        this.unitTableFilter = unitFilter;

        TableRowSorter<IngredientTableModel> ingredientRowSorter = new TableRowSorter<>((IngredientTableModel) ingredientTable.getModel());
        IngredientTableFilter ingredientFilter = new IngredientTableFilter(ingredientRowSorter);
        ingredientTable.setRowSorter(ingredientRowSorter);
        this.ingredientTableFilter = ingredientFilter;

        TableRowSorter<RecipeIngredientsTableModel> recipeIngredientRowSorter = new TableRowSorter<>((RecipeIngredientsTableModel) recipeIngredientsTable.getModel());
        RecipeIngredientTableFilter recipeIngredientFilter = new RecipeIngredientTableFilter(recipeIngredientRowSorter);
        recipeIngredientsTable.setRowSorter(recipeIngredientRowSorter);
        this.recipeIngredientsTableFilter = recipeIngredientFilter;

        addAction = new AddRecipeAction(recipeTable, ingredientTable, unitTable, recipeIngredientsTable, recipeIngredientFilter);
        editAction = new EditRecipeAction(recipeTable, ingredientTable, unitTable, recipeIngredientsTable, recipeIngredientFilter);
        deleteAction = new DeleteRecipeAction(recipeTable);

        importAction = new ImportAction(dependencyProvider.getImportService(), this::refresh, dependencyProvider.getTransactionExecutor());
        exportAction = new ExportAction(dependencyProvider.getExportService());

        filterAction = new FilterRecipeAction(recipeTable, recipeTableFilter);
        convertAction = new ConvertAction(unitTable);

        quitAction = new QuitAction(dependencyProvider.getExportService());

        JTabbedPane mainFrameTabs = new JTabbedPane();
        mainFrameTabs.setOpaque(true);
        mainFrameTabs.addTab("<html><b>Recipes</b></html>", new JScrollPane(recipeTable));
        mainFrameTabs.addTab("<html><b>Units</b></html>", new JScrollPane(unitTable));
        mainFrameTabs.addTab("<html><b>Ingredients</b></html>", new JScrollPane(ingredientTable));
        mainFrame.add(mainFrameTabs, BorderLayout.CENTER);

        this.toolbar = createToolbar();
        mainFrame.add(this.toolbar, BorderLayout.BEFORE_FIRST_LINE);

        this.menubar = createMenuBar();
        mainFrame.setJMenuBar(this.menubar);

        recipeTable.setMouseListener(recipeTable,
                dependencyProvider.getRecipeIngredientCrudService(),
                dependencyProvider.getIngredientRepository(),
                dependencyProvider.getRecipeRepository(),
                dependencyProvider.getGuidProvider());
        unitTable.setMouseListener(unitTable);
        ingredientTable.setMouseListener(ingredientTable);

        mainFrameTabs.addChangeListener(e -> updateActions(mainFrameTabs.getSelectedIndex()));
        refresh();
        mainFrame.pack();
    }

    private void updateActions(int selectedIndex) {
        mainFrame.remove(this.toolbar);
        mainFrame.remove(this.menubar);

        switch (selectedIndex) {
            case 0:  // Recipes tab
                addAction = new AddRecipeAction(recipeTable, ingredientTable, unitTable, recipeIngredientsTable, recipeIngredientsTableFilter);
                editAction = new EditRecipeAction(recipeTable, ingredientTable, unitTable, recipeIngredientsTable, recipeIngredientsTableFilter);
                deleteAction = new DeleteRecipeAction(recipeTable);
                filterAction = new FilterRecipeAction(recipeTable, recipeTableFilter);
                break;
            case 1:  // Units tab
                addAction = new AddUnitAction(unitTable);
                editAction = new EditUnitAction(unitTable);
                deleteAction = new DeleteUnitAction(unitTable);
                filterAction = new FilterUnitAction(unitTable, unitTableFilter);
                break;
            case 2:  // Ingredients tab
                addAction = new AddIngredientAction(ingredientTable, unitTable);
                editAction = new EditIngredientAction(ingredientTable, unitTable);
                deleteAction = new DeleteIngredientAction(ingredientTable);
                filterAction = new FilterIngredientAction(ingredientTable, ingredientTableFilter);
                break;
        }

        toolbar = createToolbar();
        menubar = createMenuBar();

        mainFrame.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);
        mainFrame.setJMenuBar(menubar);
        mainFrame.validate();
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private JToolBar createToolbar(Component... components) {
        JToolBar toolbar = new JToolBar();
        toolbar.add(addAction);
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.addSeparator();
        toolbar.add(importAction);
        toolbar.add(exportAction);
        toolbar.addSeparator();
        toolbar.add(filterAction);
        toolbar.addSeparator();
        toolbar.add(convertAction);

        for (Component comp: components) {
            toolbar.add(comp);
        }

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

    private void refresh() {
        ((RecipeTableModel) recipeTable.getModel()).refresh();
        ((IngredientTableModel) ingredientTable.getModel()).refresh();
        ((UnitTableModel) unitTable.getModel()).refresh();
    }
}
