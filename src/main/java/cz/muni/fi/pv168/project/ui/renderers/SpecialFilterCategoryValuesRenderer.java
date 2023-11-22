package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;

import javax.swing.JLabel;
import java.awt.Font;

public class SpecialFilterCategoryValuesRenderer extends AbstractRenderer<SpecialFilterCategoryValues> {
    public SpecialFilterCategoryValuesRenderer() {
        super(SpecialFilterCategoryValues.class);
    }

    protected void updateLabel(JLabel label, SpecialFilterCategoryValues value) {
        switch (value) {
            case ALL -> renderBoth(label);
        }
    }

    private static void renderBoth(JLabel label) {
        label.setText("All categories");
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
    }
}
