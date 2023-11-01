package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalTime;

public class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<RecipeCategories> categoryField = new DefaultComboBoxModel<>(RecipeCategories.values());
    private final JTextField timeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final Recipe recipe;

    public RecipeDialog(Recipe recipe) {
        this.recipe = recipe;
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        categoryField.setSelectedItem(recipe.getCategory());
        timeField.setText(recipe.getTime().toString());
        portionsField.setText(Integer.toString(recipe.getPortions()));
    }

    private void addFields() {
        var categoryComboBox = new JComboBox<>(categoryField);
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time:", timeField);
        add("Portions:", portionsField);

        // Add a button to create a custom ingredient
        JButton createCustomIngredientButton = new JButton("Add ingredient(s) to recipe");
        createCustomIngredientButton.setOpaque(true);
        createCustomIngredientButton.setBackground(new Color(26, 72, 93));
        createCustomIngredientButton.addActionListener(e -> createCustomIngredient());
        add("", createCustomIngredientButton);
    }

    private void createCustomIngredient() {
        JFrame addIngredientsFrame =  new JFrame();
        CustomIngredientDialog customIngredientDialog = new CustomIngredientDialog(addIngredientsFrame, recipe);
        customIngredientDialog.setVisible(true);
        DefaultTableModel ingredientListModel = customIngredientDialog.getIngredientListModel();
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setCategory((RecipeCategories) categoryField.getSelectedItem());
        try {
            recipe.setTime(LocalTime.parse(timeField.getText()));
            recipe.setPortions(Integer.parseInt(portionsField.getText()));
        } catch (DateTimeException | NumberFormatException e) {
            return null;
        }
        return recipe;
    }
}
