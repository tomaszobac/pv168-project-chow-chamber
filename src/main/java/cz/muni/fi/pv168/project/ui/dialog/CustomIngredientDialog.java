package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.MainWindowUtilities;
import cz.muni.fi.pv168.project.ui.action.ingredient.EditIngredientAction;
import cz.muni.fi.pv168.project.ui.action.recipeIngredient.DeleteRecipeIngredientAction;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeIngredientsTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class CustomIngredientDialog extends JDialog {
    private final JComboBox<Ingredient> ingredientComboBox = new JComboBox<>();
    private final JComboBox<Unit> unitComboBox = new JComboBox<>();
    private final JTextField amountTextField = new JTextField();
    private final RecipeIngredientsTableModel recipeIngredientsTableModel;
    // edit action for possible editing, don't know if needed
    private final Action editAction;
    private final Action deleteAction;

    public CustomIngredientDialog(JFrame parentFrame, Recipe recipe) {
        super(parentFrame, "Recipe ingredients", true);
        setLayout(new BorderLayout());
        for (Ingredient ingredient : TestTable.getTableThree()) {
            ingredientComboBox.addItem(ingredient);
        }
        for (Unit unit : TestTable.getTableTwo()) {
            unitComboBox.addItem(unit);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Select Ingredient: "));
        topPanel.add(ingredientComboBox);
        topPanel.add(new JLabel("amount:"));
        topPanel.add(amountTextField);
        topPanel.add(new JLabel("in unit:"));
        topPanel.add(unitComboBox);

        JButton addButton = new JButton("Add Ingredient");
        addButton.setForeground(Color.WHITE); // Set the text color to white
        Font buttonFont = addButton.getFont();
        addButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize())); // Make the text bold
        addButton.setBackground(new Color(42, 162, 26));

        recipeIngredientsTableModel = new RecipeIngredientsTableModel(recipe.getIngredients());
        RecipeIngredientsTable recipeIngredientsTable = (RecipeIngredientsTable) MainWindowUtilities.createTableFromModel(recipeIngredientsTableModel, 2, this::rowSelectionChanged);
        // recipeIngredientsTable.setMouseListener(recipeIngredientsTable);
        editAction = new EditIngredientAction(recipeIngredientsTable);
        deleteAction = new DeleteRecipeIngredientAction(recipeIngredientsTable);
        add(new JScrollPane(recipeIngredientsTable), BorderLayout.CENTER);
        addButton.addActionListener(e -> {
            Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
            Unit selectedUnit = (Unit) unitComboBox.getSelectedItem();
            String amount = amountTextField.getText();
            if (selectedIngredient != null && !amount.isEmpty() && (selectedIngredient.getUnit().getName().equals(selectedUnit.getName()))) {
                Ingredient newIngredient = new Ingredient(selectedIngredient.getName(), selectedIngredient.getCalories(), selectedIngredient.getUnit(), Double.parseDouble(amountTextField.getText()));
                recipeIngredientsTableModel.addRow(newIngredient);
                recipe.addIngredient(newIngredient);
            } else {
                // Display an error dialog
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, amount.isEmpty() ? "Please fill in amount" : "Selected unit type must match ingredient unit type", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        topPanel.add(addButton, BorderLayout.SOUTH);

        JButton deleteButton = new JButton("Delete Ingredient");
        deleteButton.setForeground(Color.WHITE); // Set the text color to white
        deleteButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize())); // Make the text bold
        deleteButton.setBackground(new Color(182, 28, 28));
        topPanel.add(deleteButton, BorderLayout.SOUTH);
        deleteButton.addActionListener(deleteAction);
        deleteButton.addActionListener(e -> {
            int selectedRow = recipeIngredientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                Ingredient ingredientToDelete = recipeIngredientsTableModel.getEntity(selectedRow);
                if (ingredientToDelete != null) {
                    recipeIngredientsTableModel.deleteRow(selectedRow);
                    recipe.getIngredients().remove(ingredientToDelete);
                }
            } else {
                // Display an error dialog if no ingredient is selected
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, "Please select an ingredient to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(topPanel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        if (selectionModel.isSelectionEmpty()) {
            editAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else if (selectionModel.getSelectedItemsCount() == 1) {
            editAction.setEnabled(true);
            deleteAction.setEnabled(true);
        } else if (selectionModel.getSelectedItemsCount() > 1) {
            editAction.setEnabled(false);
            deleteAction.setEnabled(true);
        }
    }
}
