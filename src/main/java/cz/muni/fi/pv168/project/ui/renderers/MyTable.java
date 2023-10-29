package cz.muni.fi.pv168.project.ui.renderers;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
// TODO: make the whole tables package with MyTable more abstract to avoid code duplication
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
        Color alternateColor = new Color(100, 100, 100);
        Color mainColor = getBackground();

        if(!component.getBackground().equals(getSelectionBackground())) {
            Color rowColor = (row % 2 != 0 ? alternateColor : mainColor);
            component.setBackground(rowColor);
            rowColor = null;
        }

        return component;
    }
}