package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import javax.swing.*;
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
        var genderComboBox = new JComboBox<>(categoryField);
        add("Name:", nameField);
        add("Category:", genderComboBox);
        add("Time:", timeField);
        add("Portions:", portionsField);
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setCategory((RecipeCategories) categoryField.getSelectedItem());
        try{
            recipe.setTime(LocalTime.parse(timeField.getText()));
            recipe.setPortions(Integer.parseInt(portionsField.getText()));
        } catch (DateTimeException | NumberFormatException e){
            return null;
        }
        return recipe;
    }
}
