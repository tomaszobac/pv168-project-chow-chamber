package cz.muni.fi.pv168.project.ui.renderers;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class UnifiedTableCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (row == -1) {
            ((JLabel) component).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY),
                    BorderFactory.createEmptyBorder(0, 10, 0, 0)));
            component.setFont(component.getFont().deriveFont(Font.BOLD).deriveFont(13f));
        }
        else {
            ((JLabel) component).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY),
                    BorderFactory.createEmptyBorder(0, 10, 0, 0)));
            component.setFont(component.getFont().deriveFont(12f));
        }
        component.setForeground(Color.WHITE);
        ((JLabel) component).setHorizontalAlignment(SwingConstants.LEFT);

        return component;
    }
}