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

        JButton customIngredientButton = createCustomIngredientButton(ingredientTable, unitTable);
        add("", customIngredientButton);

        // Add instructions area
        JScrollPane instructionsScrollPane = new JScrollPane(instructionsArea);
        instructionsScrollPane.setPreferredSize(new Dimension(400, 300));
        add("Instructions:", instructionsScrollPane, "wmin 250lp, grow, gapy 10");
    }

    /**
     * Creates a JButton for adding ingredient(s) to a recipe.
     *
     * @param ingredientTable The JTable displaying existing ingredients.
     * @param unitTable       The JTable displaying available units for ingredients.
     *
     * @return A styled JButton configured for adding ingredient(s) to a recipe.
     */
    private JButton createCustomIngredientButton(JTable ingredientTable, JTable unitTable) {
        JButton customIngredientButton = new JButton("Add ingredient(s) to recipe");
        customIngredientButton.setOpaque(true);
        customIngredientButton.setForeground(Color.WHITE);
        Font buttonFont = customIngredientButton.getFont();
        customIngredientButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        customIngredientButton.setBackground(new Color(26, 72, 93));
        customIngredientButton.addActionListener(e -> createCustomIngredient(ingredientTable, unitTable));
        return customIngredientButton;
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
