package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterUnitTypeValues;

import javax.swing.*;
import java.awt.*;

public class SpecialFilterUnitTypeValuesRenderer extends AbstractRenderer<SpecialFilterUnitTypeValues> {
    public SpecialFilterUnitTypeValuesRenderer() {
        super(SpecialFilterUnitTypeValues.class);
    }

    protected void updateLabel(JLabel label, SpecialFilterUnitTypeValues value) {
        switch (value) {
            case ALL -> renderBoth(label);
        }
    }

    private static void renderBoth(JLabel label) {
        label.setText("All unit types");
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
    }
}
