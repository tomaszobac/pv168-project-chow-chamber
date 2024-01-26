package cz.muni.fi.pv168.project.ui.listeners;

import javax.swing.JTable;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OutsideOfBoundsListener extends MouseAdapter {
    private JTable table;

    public OutsideOfBoundsListener(JTable table) {
        this.table = table;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        int col = table.columnAtPoint(point);

        if (row == -1 || col == -1) {
            // Clicked outside the table
            table.clearSelection();
        }
    }
}

