package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import javax.swing.*;

public class UnitTypeRenderer extends AbstractRenderer<UnitType> {
    public UnitTypeRenderer() {
        super(UnitType.class);
    }

    @Override
    protected void updateLabel(JLabel label, UnitType category) {
        if (category != null) {
            label.setText(category.toString());
        }
    }
}
