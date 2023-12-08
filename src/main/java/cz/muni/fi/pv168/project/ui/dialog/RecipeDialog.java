package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class RecipeDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private JComboBox<RecipeCategory> categoryComboBox;
    private final JTextField timeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final JTextArea instructionsArea = new JTextArea("Instructions go here!");
    private final Recipe recipe;
    private boolean returnedOK = false;

    public RecipeDialog(Recipe recipe, JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        this.recipe = recipe;
        setValues();
        addFields(ingredientTable, unitTable, recipeIngredientsTable, filter);
    }

    private void setValues() {
        nameField.setText(recipe.getName());

        categoryComboBox = new JComboBox<>();
        Arrays.stream(RecipeCategory.values()).forEach(categoryComboBox::addItem);
        categoryComboBox.setRenderer(new CategoryRenderer());

        timeField.setText(recipe.getTime().toString());
        portionsField.setText(Integer.toString(recipe.getPortions()));
        if (!recipe.getInstructions().isBlank()) {
            instructionsArea.setText(recipe.getInstructions());
        }
        instructionsArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (instructionsArea.getText().equals("Instructions go here!")) {
                    instructionsArea.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (instructionsArea.getText().isEmpty()) {
                    instructionsArea.setText("Instructions go here!");
                }
            }
        });
        instructionsArea.setLineWrap(true);
    }

    private void addFields(JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time:", timeField);
        add("Portions:", portionsField);

        JButton customIngredientButton = createCustomIngredientButton(ingredientTable, unitTable, recipeIngredientsTable, filter);
        add("", customIngredientButton);

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
    private JButton createCustomIngredientButton(JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        JButton customIngredientButton = new JButton("Add ingredient(s) to recipe");
        customIngredientButton.setOpaque(true);
        customIngredientButton.setForeground(Color.WHITE);
        Font buttonFont = customIngredientButton.getFont();
        customIngredientButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        customIngredientButton.setBackground(new Color(26, 72, 93));
        customIngredientButton.addActionListener(e -> createCustomIngredient(ingredientTable, unitTable, recipeIngredientsTable, filter));
        return customIngredientButton;
    }

    private void createCustomIngredient(JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        JFrame addIngredientsFrame = new JFrame();
        CustomIngredientDialog customIngredientDialog = new CustomIngredientDialog(addIngredientsFrame, recipe, ingredientTable, unitTable, recipeIngredientsTable, filter);
        customIngredientDialog.setVisible(true);

        List<RecipeIngredient> list = customIngredientDialog.getNewRecipeIngredients();
        RecipeIngredientsTableModel model = (RecipeIngredientsTableModel) recipeIngredientsTable.getModel();
        if (!customIngredientDialog.getExitThroughDone()) {
            System.out.println("EXIT WAS NOT THROUGH DONE!");
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                RecipeIngredient recipeIngredient = (RecipeIngredient) model.getValueAt(i, 0);
                if (list.contains(recipeIngredient)) {
                    model.deleteRow(i);
                }
            }
        }
    }

    public boolean getReturnedOK() {
        return returnedOK;
    }

    public String getRecipeGuid() {
        return recipe.getGuid();
    }

    private LocalTime getTime(JTextField timeField) {
        int hours = 0;
        int minutes = 0;
        String timeString = timeField.getText();
        if (timeString.isBlank()) {
            return LocalTime.of(hours, minutes);
        }
        try {

            if (timeString.matches("\\d+")) {
                minutes = Integer.parseInt(timeString);
            } else {
                String[] parts = timeString.split(":");

                if (parts.length == 2) {
                    hours = Integer.parseInt(parts[0]);
                    minutes = Integer.parseInt(parts[1]);
                } else {
                    throw new IllegalArgumentException("Invalid time format: " + timeString);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString, e);
        }

        if (minutes > 59) {
            hours += minutes / 60;
            minutes = minutes % 60;
        } else if (minutes < 0)
            minutes = 0;

        if (hours > 23)
            hours = 23;
        else if (hours < 0)
            hours = 0;

        return LocalTime.of(hours, minutes);
    }

    @Override
    Recipe getEntity() {
        returnedOK = true;
        recipe.setName(nameField.getText());
        recipe.setCategory((RecipeCategory) categoryComboBox.getSelectedItem());
        recipe.setInstructions(instructionsArea.getText());
        try {
            recipe.setTime(getTime(timeField));
            recipe.setPortions(Integer.parseInt(portionsField.getText()));
        } catch (DateTimeException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
        return recipe;
    }
}
