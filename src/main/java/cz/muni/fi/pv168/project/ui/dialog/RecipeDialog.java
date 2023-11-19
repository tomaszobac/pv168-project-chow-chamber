package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.DateTimeException;
import java.time.LocalTime;

public class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<RecipeCategories> categoryField = new DefaultComboBoxModel<>(RecipeCategories.values());
    private final JTextField timeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final JTextArea instructionsArea = new JTextArea();
    private final Recipe recipe;

    public RecipeDialog(Recipe recipe, JTable ingredientTable, JTable unitTable) {
        this.recipe = recipe;
        setValues();
        addFields(ingredientTable, unitTable);
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        categoryField.setSelectedItem(recipe.getCategory());
        timeField.setText(recipe.getTime().toString());
        portionsField.setText(Integer.toString(recipe.getPortions()));
        instructionsArea.setText("Instructions go here!");
    }

    private void addFields(JTable ingredientTable, JTable unitTable) {
        var categoryComboBox = new JComboBox<>(categoryField);
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time:", timeField);
        add("Portions:", portionsField);

        // Add a button to create a custom ingredient
        JButton createCustomIngredientButton = new JButton("Add ingredient(s) to recipe");
        createCustomIngredientButton.setOpaque(true);
        createCustomIngredientButton.setForeground(Color.WHITE);
        Font buttonFont = createCustomIngredientButton.getFont();
        createCustomIngredientButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        createCustomIngredientButton.setBackground(new Color(26, 72, 93));
        createCustomIngredientButton.addActionListener(e -> createCustomIngredient(ingredientTable, unitTable));
        add("", createCustomIngredientButton);

        // Add instructions area
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setPreferredSize(new Dimension(400, 300));
        add("Instructions:", instructionsScrollPane, "wmin 250lp, grow, gapy 10");
    }

    private void createCustomIngredient(JTable ingredientTable, JTable unitTable) {
        JFrame addIngredientsFrame = new JFrame();
        CustomIngredientDialog customIngredientDialog = new CustomIngredientDialog(addIngredientsFrame, recipe, ingredientTable, unitTable);
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
