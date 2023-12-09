package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.action.recipeIngredient.DeleteRecipeIngredientAction;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.renderers.IngredientComboBoxRenderer;
import cz.muni.fi.pv168.project.ui.renderers.UnitComboBoxRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomIngredientDialog extends JDialog {
    private final JComboBox<Ingredient> ingredientComboBox = new JComboBox<>();
    private final JComboBox<Unit> unitComboBox = new JComboBox<>();
    private final JTextField amountTextField = new JTextField();
    private final List<RecipeIngredient> newRecipeIngredients = new ArrayList<>();
    private boolean exitThroughDone = false;

    public CustomIngredientDialog(JFrame parentFrame, Recipe recipe, JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        super(parentFrame, "Recipe ingredients", true);
        setLayout(new BorderLayout());

        for (int i = 0; i < ingredientTable.getRowCount(); i++) {
            ingredientComboBox.addItem((Ingredient) ingredientTable.getValueAt(i, 0));
        }
        ingredientComboBox.setRenderer(new IngredientComboBoxRenderer());
        if (ingredientComboBox.getItemCount() > 0) {
            ingredientComboBox.setSelectedIndex(0);
        }
        ingredientComboBox.addActionListener( e -> updateUnitComboBox(unitTable, false));

        updateUnitComboBox(unitTable, true);
        unitComboBox.setRenderer(new UnitComboBoxRenderer());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Select Ingredient: "));
        topPanel.add(ingredientComboBox);
        topPanel.add(new JLabel("amount:"));
        topPanel.add(amountTextField);
        topPanel.add(new JLabel("in unit:"));
        topPanel.add(unitComboBox);

        JButton addButton = new JButton("Add Ingredient");
        addButton.setForeground(Color.WHITE);
        Font buttonFont = addButton.getFont();
        addButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        addButton.setBackground(new Color(42, 162, 26));

        filter.filterGuid(recipe.getGuid());

        Action deleteAction = new DeleteRecipeIngredientAction(recipeIngredientsTable);
        add(new JScrollPane(recipeIngredientsTable), BorderLayout.CENTER);
        addButton.addActionListener(e -> {
            Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
            Unit selectedUnit = (Unit) unitComboBox.getSelectedItem();
            String amount = amountTextField.getText();
            if (selectedIngredient != null && !amount.isEmpty() && (selectedIngredient.getUnit().getType().equals(selectedUnit.getType()))) {
                RecipeIngredient newIngredient = new RecipeIngredient(recipe.getGuid(),  selectedIngredient.getGuid(), selectedUnit, Double.parseDouble(amountTextField.getText()));
                newRecipeIngredients.add(newIngredient);
                ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).addRow(newIngredient);
            } else {
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, amount.isEmpty() ? "Please fill in amount" : "Selected unit type must match ingredient unit type", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        topPanel.add(addButton, BorderLayout.SOUTH);

        JButton deleteButton = new JButton("Delete Ingredient");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        deleteButton.setBackground(new Color(182, 28, 28));
        topPanel.add(deleteButton, BorderLayout.SOUTH);
        deleteButton.addActionListener(deleteAction);
        deleteButton.addActionListener(e -> {
            int selectedRow = recipeIngredientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                RecipeIngredient ingredientToDelete = ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).getEntity(selectedRow);
                if (ingredientToDelete != null) {
                    ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).deleteRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, "Please select an ingredient to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            exitThroughDone = true;
            dispose();
        });
        bottomPanel.add(doneButton);
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parentFrame);
        updateUnitComboBox(unitTable, false);
    }

    private void updateUnitComboBox(JTable unitTable, boolean all) {
        Ingredient selectedItem = (Ingredient) ingredientComboBox.getSelectedItem();
        if (Objects.isNull(selectedItem)) {
            return;
        }
        unitComboBox.removeAllItems();
        for (int i = 0; i < unitTable.getRowCount(); i++) {
            Unit unit = (Unit) unitTable.getValueAt(i, 0);
            if(all || selectedItem.getUnit().getType().equals(unit.getType())) {
                unitComboBox.addItem(unit);
            }
        }
    }

    public boolean getExitThroughDone() {
        return exitThroughDone;
    }

    public List<RecipeIngredient> getNewRecipeIngredients() {
        return newRecipeIngredients;
    }
}
