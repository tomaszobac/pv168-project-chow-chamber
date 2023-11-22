package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.business.model.Ingredient;

import javax.swing.JLabel;

public class IngredientComboBoxRenderer extends AbstractRenderer<Ingredient> {
    public IngredientComboBoxRenderer() {
        super(Ingredient.class);
        }

    @Override
    protected void updateLabel(JLabel label, Ingredient ingredient) {
        if (ingredient != null) {
            label.setText(ingredient.getName());
        }
    }
}
