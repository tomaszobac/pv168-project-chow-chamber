package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import javax.swing.*;
import java.util.Map;

public class CategoryRenderer extends AbstractRenderer<RecipeCategories> {
    public CategoryRenderer() {
        super(RecipeCategories.class);
    }

    @Override
    protected void updateLabel(JLabel label, RecipeCategories category) {
        if (category != null) {
            label.setText(category.toString());
        }
    }
}
