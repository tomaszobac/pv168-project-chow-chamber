package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.model.tables.IngredientsTable;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeIngredientsTable;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeTable;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.renderers.MyFrame;
import cz.muni.fi.pv168.project.ui.renderers.MyTable;
import cz.muni.fi.pv168.project.ui.renderers.UnifiedTableCellRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalTime;

public class MainWindowUtilities {
    public static void switchToTab(int tabIndex, JTabbedPane openRecipesTab) {
        if (tabIndex >= 0 && tabIndex < openRecipesTab.getTabCount()) {
            openRecipesTab.setSelectedIndex(tabIndex);
        }
    }

    public static JLabel createLabel(String text, int fontType) {
        Font font = fontType == 0 ? new Font(null, Font.BOLD, 14) : new Font(null, Font.PLAIN, 14);
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    public static MyTable createTable(MyTable MTable, ListSelectionListener rowSelectionChanged) {
        MTable.getSelectionModel().addListSelectionListener(rowSelectionChanged);
        UnifiedTableCellRenderer renderer = new UnifiedTableCellRenderer();
        MTable.setDefaultRenderer(Object.class, renderer);
        MTable.setDefaultRenderer(Integer.class, renderer);
        MTable.setDefaultRenderer(LocalTime.class, renderer);
        MTable.setRowSorter(new TableRowSorter<>(MTable.getModel()));
        return MTable;
    }

    public static MyTable createTableFromModel(AbstractTableModel model, Integer table, ListSelectionListener rowSelectionChanged) {
        return switch (table) {
            case 0 -> createTable(new RecipeTable(model), rowSelectionChanged);
            case 1 -> createTable(new IngredientsTable(model), rowSelectionChanged);
            case 2 -> createTable(new RecipeIngredientsTable(model), rowSelectionChanged);
            case 3 -> createTable(new UnitTable(model), rowSelectionChanged);
            default -> throw new IllegalStateException("Unexpected value: " + table);
        };
    }

    public static MyFrame createFrame(Dimension min_size, Dimension max_size, String name) {
        MyFrame Mframe = new MyFrame();
        Mframe.setTitle(name);
        Mframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Mframe.setVisible(true);

        if (min_size == null) {
            min_size = new Dimension(800, 400);
        }
        if (max_size == null) {
            max_size = new Dimension(1920, 1080);
        }
        Mframe.setMinimumSize(min_size);
        Mframe.setMaximumSize(max_size);

        return Mframe;
    }

}