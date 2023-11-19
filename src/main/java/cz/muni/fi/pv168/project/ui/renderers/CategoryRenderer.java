package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import javax.swing.JLabel;

public class CategoryRenderer extends AbstractRenderer<RecipeCategory> {
    public CategoryRenderer() {
        super(RecipeCategory.class);
    }

    @Override
    protected void updateLabel(JLabel label, RecipeCategory category) {
        if (category != null) {
            label.setText(category.toString());
        }
    }
}
