package cz.muni.fi.pv168.project.ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class MyTable extends JTable {

    public MyTable(TableModel model) {
        super(model);
        UnifiedTableCellRenderer renderer = new UnifiedTableCellRenderer();
        setDefaultRenderer(Object.class, renderer);
        getTableHeader().setDefaultRenderer(renderer);
        setFocusable(false);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);

        if (!isRowSelected(row)) {
            if (row % 2 != 0) {
                component.setBackground(new Color(100, 100, 100));
            } else {
                component.setBackground(getBackground());
            }
        }
        return component;
    }
}