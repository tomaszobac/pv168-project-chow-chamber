package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalTime;

public class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<RecipeCategories> categoryField = new DefaultComboBoxModel<>(RecipeCategories.values());
    private final JTextField timeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final JTextArea instructionsArea = new JTextArea();
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
        instructionsArea.setText("Instructions go here!");
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
        createCustomIngredientButton.setForeground(Color.WHITE); // Set the text color to white
        Font buttonFont = createCustomIngredientButton.getFont();
        createCustomIngredientButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize())); // Make the text bold
        createCustomIngredientButton.setBackground(new Color(26, 72, 93));
        createCustomIngredientButton.addActionListener(e -> createCustomIngredient());
        add("", createCustomIngredientButton);

        // Add instructions area
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setPreferredSize(new Dimension(400, 300)); // Set your desired dimensions
        add("Instructions:", instructionsScrollPane, "wmin 250lp, grow, gapy 10");
    }

    private void createCustomIngredient() {
        JFrame addIngredientsFrame = new JFrame();
        CustomIngredientDialog customIngredientDialog = new CustomIngredientDialog(addIngredientsFrame, recipe);
        customIngredientDialog.setVisible(true);
    }

    @Override
    Recipe getEntity() {
        recipe.setName(nameField.getText());
        recipe.setCategory((RecipeCategories) categoryField.getSelectedItem());
        recipe.setInstructions(instructionsArea.getText());
        try {
            recipe.setTime(LocalTime.parse(timeField.getText()));
            recipe.setPortions(Integer.parseInt(portionsField.getText()));
        } catch (DateTimeException | NumberFormatException e) {
            return null;
        }
        return recipe;
    }
}
