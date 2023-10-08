package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.Recipe;

import javax.swing.*;

public class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final JTextField categoryField = new JTextField();
    private final JTextField timeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final Recipe recipe;

    public RecipeDialog(Recipe recipe) {
        this.recipe = recipe;
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        categoryField.setText(recipe.getCategory());
        timeField.setText(recipe.getTime());
        portionsField.setText(recipe.getPortions());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Category:", categoryField);
        add("Time:", timeField);
        add("Portions:", portionsField);
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setCategory(categoryField.getText());
        recipe.setTime(timeField.getText());
        recipe.setPortions(portionsField.getText());
        return recipe;
    }
}
